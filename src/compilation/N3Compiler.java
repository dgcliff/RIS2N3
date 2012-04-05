/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Author;
import entity.Conference;
import entity.Journal;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import templates.BaseTemplate;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class N3Compiler
{
    private ArrayList<String> n3Compilation = new ArrayList<>();
    private UniqueURIGenerator uniqueURIGen;
    
    public N3Compiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
    }
    
    public void addEntry(BaseTemplate bt)
    {
        ArrayList<String> newEntry = bt.getEntry();
        ArrayList<String> authorshipURIList = new ArrayList<>();
        ArrayList<String> authorURIList;
        
        if(!bt.hasAuthors())
        {
            //Dead Entry
            return;
        }
        
        authorURIList = bt.getAuthorURIList();

        //create new authorship and link to author and article
        for(String authorURI : authorURIList)
        {
            //create new authorship
            String authorshipURI = uniqueURIGen.generateNewURI();
            authorshipURIList.add(authorshipURI);

            n3Compilation.add(authorshipURI);
            n3Compilation.add("\t a <http://vivoweb.org/ontology/core#Authorship> ;");

            //link authorship to author                
            n3Compilation.add("\t <http://vivoweb.org/ontology/core#linkedAuthor> " + authorURI + " ;");

            //link authorship to article                
            n3Compilation.add("\t <http://vivoweb.org/ontology/core#linkedInformationResource> " + bt.getURI() + " .\n");                                                

            //link author to authorship (inverse of above)
            n3Compilation.add(authorURI);
            n3Compilation.add("\t<http://vivoweb.org/ontology/core#authorInAuthorship> "+ authorshipURI + " .\n");
        }
        
        for(String s : newEntry)
        {
            n3Compilation.add(s);
        }        

        n3Compilation.add("");

        n3Compilation.add(bt.getURI());

        //link article to authorships        
        for(int i = 0; i < authorshipURIList.size(); i++)
        {
            if(i < (authorshipURIList.size() - 1))
            {
                n3Compilation.add("\t<http://vivoweb.org/ontology/core#informationResourceInAuthorship> " + authorshipURIList.get(i) + " ;");
            }
            else
            {
                n3Compilation.add("\t<http://vivoweb.org/ontology/core#informationResourceInAuthorship> " + authorshipURIList.get(i) + " .");
            }
        }

        n3Compilation.add("");

        if(bt.hasDate())
        {
            //need to create new individual to match the URI
            n3Compilation.add(bt.getDateUri());
            n3Compilation.add("\ta <http://vivoweb.org/ontology/core#DateTimeValue> ;");
            n3Compilation.add("\t<http://vivoweb.org/ontology/core#dateTime> " + bt.getDateValue() + " .\n");
        }

        if(bt.hasURL())
        {
            //need to create new core:URLLink and associate with publication
            n3Compilation.add(bt.getURLuri());
            n3Compilation.add("\t<http://vivoweb.org/ontology/core#linkURI> \"" + bt.getURL() + "\" ;");
            n3Compilation.add("\t<http://vivoweb.org/ontology/core#webpageOf> " + bt.getURI() + " .\n");
        }
    }
    
    public void addConferences(ConferenceCompiler cC)
    {
        //for each conference, add its URI, the types and its name and label
        ArrayList<Conference> listOfAllConferences = cC.getAllConferences();
        
        for(Conference c : listOfAllConferences)
        {
            n3Compilation.add(c.getURI());                     
            
            n3Compilation.add("\ta <http://www.w3.org/2002/07/owl#Thing> ;");
            n3Compilation.add("\ta <http://purl.org/ontology/bibo/Conference> ;");
            n3Compilation.add("\ta <http://purl.org/NET/c4dm/event.owl#Event> ;");
            
            ArrayList<String> PaperURIs = c.getPaperURIs();
            
            for(String paperURI : PaperURIs)
            {
                n3Compilation.add("\t<http://purl.org/ontology/bibo/presents> " + paperURI + " ;");
            }
            
            n3Compilation.add("\t<http://www.w3.org/2000/01/rdf-schema#label> \"" + c.getTitle() + "\" .");                        
            
            n3Compilation.add("");
        }        
    }
    
    public void addJournals(JournalCompiler jC)
    {
        //for each journal, add its URI, the types and its name and label
        ArrayList<Journal> listOfAllJournals = jC.getAllJournals();
        
        for(Journal j : listOfAllJournals)
        {
            n3Compilation.add(j.getURI());                     
            
            n3Compilation.add("\ta <http://www.w3.org/2002/07/owl#Thing> ;");
            n3Compilation.add("\ta <http://purl.org/ontology/bibo/Collection> ;");            
            n3Compilation.add("\ta <http://vivoweb.org/ontology/core#InformationResource> ;");
            n3Compilation.add("\ta <http://purl.org/ontology/bibo/Journal> ;");
            n3Compilation.add("\ta <http://purl.org/ontology/bibo/Periodical> ;");
            
            ArrayList<String> ArticleURIs = j.getArticleURIs();
            
            for(String articleURI : ArticleURIs)
            {
                n3Compilation.add("\t<http://vivoweb.org/ontology/core#publicationVenueFor> " + articleURI + " ;");
            }
            
            n3Compilation.add("\t<http://www.w3.org/2000/01/rdf-schema#label> \"" + j.getTitle() + "\" .");                        
            
            n3Compilation.add("");
        }        
    }
    
    public void addAuthors(AuthorCompiler aC)
    {
        //for each author, add their URI, their types and their details
        ArrayList<Author> listOfAllAuthors = aC.getAllAuthors();
        
        for(Author au : listOfAllAuthors)
        {
            n3Compilation.add(au.getURI());
            
            n3Compilation.add("\ta <http://www.w3.org/2002/07/owl#Thing> ;");
            n3Compilation.add("\ta <http://xmlns.com/foaf/0.1/Person> ;");            
            n3Compilation.add("\ta <http://xmlns.com/foaf/0.1/Agent> ;");
            
            n3Compilation.add("\t<http://www.w3.org/2000/01/rdf-schema#label> \"" + au.getFullName() + "\" .");
            
            n3Compilation.add("");
        }
    }
    
    public void outputValues(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for(String s : n3Compilation)
            {
                out.println(s);
            }
        }        
        catch(IOException e)
        {
            System.out.println(e);
        }        
    }
}
