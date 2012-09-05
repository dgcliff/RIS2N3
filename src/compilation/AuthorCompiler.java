/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Author;
import entity.Publication;
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
            String vivoName = (String) pairs.getValue();
            
            Author tempVIVOauthor = new Author(vivoName, uniqueURIGen.generateTempURI());
            Author tempAuthor = new Author(vivoName, uniqueURIGen.generateTempURI());
            
            if(tempAuthor.isTheSamePersonAs(tempVIVOauthor))
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
        }
                
    }
        
}
