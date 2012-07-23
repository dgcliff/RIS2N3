package extraction;

import compilation.AuthorCompiler;
import compilation.ConferenceCompiler;
import compilation.JournalCompiler;
import compilation.N3Compiler;
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

    public RISExtractor(File dir, UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                fileList.add(dir.getAbsolutePath() + "/" + children[i]);
            }
        }
    }

    public AuthorCompiler extractAuthorNames(boolean totalList)
    {
        AuthorCompiler aC = new AuthorCompiler(uniqueURIGen);

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
                        String title = "";
                        ArrayList<String> authorList = new ArrayList<>();                        
                        
                        String line = reader.readLine();
                        
                        boolean hasAuthors = false;
                        
                        while (line != null)
                        {                            
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
                            aC.addPublication(title, authorList);
                        }
                        else
                        {
                            System.out.println("Dead entry - " + title);
                        }
                    }
                    
                    topLine = reader.readLine();
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }        
        }

        return aC;
    }

    public void extractToN3(String outputFile, AuthorCompiler aC)
    {
        N3Compiler n3c = new N3Compiler(uniqueURIGen);
        JournalCompiler jC = new JournalCompiler(uniqueURIGen);
        ConferenceCompiler cC = new ConferenceCompiler(uniqueURIGen);
        
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
                    ArrayList<String> newEntry = new ArrayList<>();
                    
                    //extract type
                    if (topLine.startsWith("TY"))
                    {
                        newEntry.add(topLine);
                                
                        String type = (topLine.substring(topLine.indexOf("-") + 1, topLine.length())).trim();
                        
                        String line = reader.readLine();
                        
                        while (line != null)
                        {
                            if (line.startsWith("TY")) //improperly ended entry, should've encountered ER before now
                            {
                                line = null;
                                break;
                            }
                            else if (line.startsWith("ER"))
                            {
                                completeEntry = true;
                                newEntry.add(line);
                                line = null;
                                break;
                            }
                            else
                            {
                                newEntry.add(line);
                                line = reader.readLine();
                            }                                                        
                        }
                        
                        if(completeEntry)
                        {
                            switch (type)
                            {
                                case "ABST": case "INPR": case "JFULL": case "JOUR": JournalArticle jt = new JournalArticle(uniqueURIGen, newEntry, aC, jC); n3c.addEntry(jt); break;
                                case "CONF":    ConferenceProceedings cpt = new ConferenceProceedings(uniqueURIGen, newEntry, aC, cC); n3c.addEntry(cpt); break;
                                case "UNPB":    UnpublishedWork uwt = new UnpublishedWork(uniqueURIGen, newEntry, aC); n3c.addEntry(uwt); break;
                                case "BOOK":    Book bt = new Book(uniqueURIGen, newEntry, aC); n3c.addEntry(bt); break;
                                case "CHAP":    BookSection bst = new BookSection(uniqueURIGen, newEntry, aC); n3c.addEntry(bst); break;
                                case "THES":    Thesis tt = new Thesis(uniqueURIGen, newEntry, aC); n3c.addEntry(tt); break;
                                case "GEN":     Generic gt = new Generic(uniqueURIGen, newEntry, aC); n3c.addEntry(gt); break;
                                case "RPRT":    Report rt = new Report(uniqueURIGen, newEntry, aC); n3c.addEntry(rt); break;
                                default:        System.out.println("Missing classfication! - " + type); break;
                            }
                        }                                                
                    }
                    
                    topLine = reader.readLine();
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
