package extraction;

import compilation.*;
import database.SPARQLController;
import entity.Publication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import templates.*;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author dgcliff
 */
public class RISExtractor
{
    UniqueURIGenerator uniqueURIGen;
    ArrayList<String> fileList = new ArrayList<>();
    
    AuthorCompiler aC;
    PublicationCompiler pC;
    
    public RISExtractor(File dir, SPARQLController sC)
    {
        uniqueURIGen = new UniqueURIGenerator(sC);
        aC = new AuthorCompiler(uniqueURIGen);
        pC = new PublicationCompiler(uniqueURIGen);
        
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                fileList.add(dir.getAbsolutePath() + "/" + children[i]);
            }
        }
    }
    
    public PublicationCompiler getPublicationCompiler()
    {
        return pC;
    }

    public void extractAuthorNames()
    {
        int i = 0;
        
        //for loop for each file
        for (String filename : fileList)
        {            
            try
            {
                FileReader input = new FileReader(filename);
                BufferedReader reader = new BufferedReader(input);
                
                String topLine = reader.readLine();
                
                boolean completeEntry = false;
                
                //inner loop for for file, go through line by line
                while (topLine != null)
                {                    
                    //extract type
                    if (topLine.startsWith("TY"))
                    {
                        ArrayList<String> rawPublicationEntry = new ArrayList<>();
                        rawPublicationEntry.add(topLine);
                        
                        String title = "";
                        ArrayList<String> authorList = new ArrayList<>();
                        
                        String line = reader.readLine();
                        
                        boolean hasAuthors = false;
                        
                        while (line != null)
                        {
                            rawPublicationEntry.add(line);
                            
                            if (line.startsWith("TY")) //improperly ended entry, should've encountered ER before now
                            {
                                line = null;
                                break;
                            }
                            else if (line.startsWith("ER"))
                            {
                                completeEntry = true;                                
                                line = null;
                                break;
                            }
                            else
                            {
                                if (line.startsWith("T1") || line.startsWith("TI"))
                                {
                                    title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
                                }
                                
                                if (line.startsWith("AU") || line.startsWith("A1") || line.startsWith("A2") || line.startsWith("A3") || line.startsWith("A4"))
                                {
                                    String author = (line.substring(line.indexOf("-") + 1, line.length())).trim();
                                    
                                    Pattern p = Pattern.compile(".*, .*"); //RIS author pattern
                                    Matcher m = p.matcher(author);
                                    
                                    if(m.matches())
                                    {
                                        hasAuthors = true;
                                        authorList.add(author);
                                    }                                                                                                            
                                }
                                                                
                                line = reader.readLine();
                            }                                                        
                        }
                        
                        if(completeEntry && hasAuthors && !title.equals(""))
                        {
                            //is the title unique?
                            if(pC.isTitleUnique(title))
                            {
                                Publication p = new Publication();
                                aC.addPublication(title, authorList, p, rawPublicationEntry);
                                pC.addPublication(p);
                            }
                            else
                            {
                                aC.addPublication(title, authorList, pC.getPublication(title), rawPublicationEntry);
                            }
                        }
                        else
                        {
                            System.out.println("Dead entry - " + title);
                        }
                    }
                    
                    topLine = reader.readLine();
                    //System.out.printf(i + "\tlines done.\r");
                    i++;
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }        
        }
    }
    
    public void extractToN3(String outputFile)
    {
        N3Compiler n3c = new N3Compiler(uniqueURIGen);
        JournalCompiler jC = new JournalCompiler(uniqueURIGen);
        ConferenceCompiler cC = new ConferenceCompiler(uniqueURIGen);
        
        ArrayList<Publication> publicationList = pC.getPublicationList();
        
        n3c.checkAuthors(aC);
        
        for (Publication p : publicationList)
        {
            ArrayList<String> newEntry = p.getRawPublicationEntry();
            String topLine = newEntry.get(0);
            String type = (topLine.substring(topLine.indexOf("-") + 1, topLine.length())).trim();
            
            try
            {
                switch (type)
                {
                    case "ABST": case "INPR": case "JFULL": case "JOUR": JournalArticle jt = new JournalArticle(uniqueURIGen, newEntry, jC, p); n3c.addEntry(jt); break;
                    case "THES": case "DISS": Thesis tt = new Thesis(uniqueURIGen, newEntry, aC, p); n3c.addEntry(tt); break;
                    case "CONF":    ConferenceProceedings cpt = new ConferenceProceedings(uniqueURIGen, newEntry, aC, cC, p); n3c.addEntry(cpt); break;
                    case "UNPB":    UnpublishedWork uwt = new UnpublishedWork(uniqueURIGen, newEntry, aC, p); n3c.addEntry(uwt); break;
                    case "BOOK":    Book bt = new Book(uniqueURIGen, newEntry, aC, p); n3c.addEntry(bt); break;
                    case "CHAP":    BookSection bst = new BookSection(uniqueURIGen, newEntry, aC, p); n3c.addEntry(bst); break;                                
                    case "GEN":     Generic gt = new Generic(uniqueURIGen, newEntry, aC, p); n3c.addEntry(gt); break;
                    case "RPRT":    Report rt = new Report(uniqueURIGen, newEntry, aC, p); n3c.addEntry(rt); break;
                    default:        System.out.println("Missing classfication! - " + type); break;
                }                                                                                            
            }
            catch (Exception e)
            {
                System.out.println(e);
            }        
        }
        
        n3c.addAuthors(aC);
        n3c.addJournals(jC);
        n3c.addConferences(cC);
        n3c.outputValues(outputFile);
    }
}
