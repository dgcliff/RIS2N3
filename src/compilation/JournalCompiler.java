/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Journal;
import java.util.ArrayList;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class JournalCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Journal> uniqueJournals;    
    
    public JournalCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueJournals = new ArrayList<>();
    }
    
    public String checkVIVOforTitle(String title)
    {
        //Check the database
        String query = "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> PREFIX bibo: <http://purl.org/ontology/bibo/> SELECT ?journal WHERE { ?journal a bibo:Journal ; rdfs:label \"" + title + "\" . }" ;
        return uniqueURIGen.sparqlController.checkForURI(query);        
    }
    
    public ArrayList<Journal> getAllJournals()
    {
        return uniqueJournals;
    }    
    
    public String addJournal(String journalName, String articleURI)
    {
        Journal jour = null;
                
        String URI = "";
        
        for(Journal j : uniqueJournals)
        {
            if(j.getTitle().equalsIgnoreCase(journalName))
            {
                URI = j.getURI();
                jour = j;
                break;
            }
        }
        
        if(URI.equals(""))
        {            
            //create new journal object
            jour = new Journal(journalName, uniqueURIGen.generateNewURI());
            
            //URI = new URI
            URI = jour.getURI();
         
            //add article
            jour.addArticle(articleURI);
            
            //add to uniqueJournals
            uniqueJournals.add(jour);
        }
        else
        {
            //add article
            jour.addArticle(articleURI);
        }
        
        return URI;
    }    
}
