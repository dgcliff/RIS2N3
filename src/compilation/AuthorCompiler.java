/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Author;
import entity.Publication;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class AuthorCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Author> uniqueAuthors;
    Map<String, String> VIVOpublicationList = new HashMap<>();
    
    public AuthorCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueAuthors = new ArrayList<>();
        
        //Check the database
        String query = "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> PREFIX foaf: <http://xmlns.com/foaf/0.1/> SELECT ?author ?authorName WHERE { ?author a foaf:Person ; rdfs:label ?authorName . }" ;
        VIVOpublicationList = uniqueURIGen.sparqlController.queryVIVO(query, "author", "authorName");
    }    
    
    public String checkVIVOforAuthor(String fullName)
    {
        String URI = null;
        
        Iterator iterator = VIVOpublicationList.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry pairs = (Map.Entry)iterator.next();
            String s = (String) pairs.getValue();
            if(s.equalsIgnoreCase(fullName))
            {
                URI = (String) pairs.getKey();
            }
        }                
        
        return URI;        
    }
    
    public String getAuthorURI(String authorName)
    {        
        Author au = new Author(authorName, uniqueURIGen.generateTempURI());
        
        for(Author listedAuthor: uniqueAuthors)
        {
            if(listedAuthor.isTheSamePersonAs(au))
            {
                au = listedAuthor;
                break;
            }
        }
        
        return au.getURI();
    }
    
    public ArrayList<Author> getAllAuthors()
    {
        return uniqueAuthors;
    }
    
    public void addPublication(String title, ArrayList<String> authorList, Publication p, ArrayList<String> rPe)
    {        
        p.setTitle(title);
        p.setRawPublicationEntry(rPe);
        
        for(String authorName : authorList)
        {
            boolean uniqueAuthor = true;
            
            Author au = new Author(authorName, uniqueURIGen.generateNewURI());
            
            //for loop - if au is the same person (programmatically defined) as
            //an existing author do not add to list, but add the publication
            //else add both                    
            
            for(Author listedAuthor: uniqueAuthors)
            {
                if(listedAuthor.isTheSamePersonAs(au))
                {
                    uniqueAuthor = false;
                    au = listedAuthor;
                    break;
                }
            }
            
            if(uniqueAuthor)
            {
                uniqueAuthors.add(au);
            }
            
            p.addAuthor(au);
            
            //is the publication already added for this author?
            boolean uniqueEntry = true;
            ArrayList<String> authorsPublications = au.getRelatedItems();            
            
            for(String enteredTitle : authorsPublications)
            {
                if(title.equalsIgnoreCase(enteredTitle))
                {
                    uniqueEntry = false;
                }
            }
            
            if(uniqueEntry)
            {
                au.addRelatedItem(title);
            }
        }
                
    }
    
    public void printAuthorListToFile(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for(Author au : uniqueAuthors)
            {
                out.println(au.getFullName() + " " +  au.getURI());
                
                //for loop for relatedItems
                ArrayList<String> relatedItems = au.getRelatedItems();
                
                for(String item : relatedItems)
                {
                    out.println("\t" + item);
                }
                
                out.println();
            }
        }        
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    
    public void outputNamesN3(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for(Author au : uniqueAuthors)
            {
                out.println(au.getURI());
                
                String givenName = au.getFirstName();
                
                if(givenName.equals(""))
                {
                    givenName = au.getFirstInitial();
                }
                
                out.println("\t<http://xmlns.com/foaf/0.1/firstName> \"" + givenName + "\" ;");
                
                out.println("\t<http://xmlns.com/foaf/0.1/lastName> \"" + au.getLastName() + "\" .\n");
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
