/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Journal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class JournalCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Journal> uniqueJournals;
    Map<String, String> VIVOpublicationList = new HashMap<>();
    
    public JournalCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueJournals = new ArrayList<>();
        
        //Check the database
        String query = "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> PREFIX bibo: <http://purl.org/ontology/bibo/> SELECT ?journal ?journalTitle WHERE { ?journal a bibo:Journal ; rdfs:label ?journalTitle . }" ;
        VIVOpublicationList = uniqueURIGen.sparqlController.queryVIVO(query, "journal", "journalTitle");        
    }
    
    public String checkVIVOforTitle(String title)
    {
        String URI = null;
        
        Iterator iterator = VIVOpublicationList.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry pairs = (Map.Entry)iterator.next();
            String s = (String) pairs.getValue();
            if(s.equalsIgnoreCase(title))
            {
                URI = (String) pairs.getKey();
            }
        }                
        
        return URI;        
    }
    
    public ArrayList<Journal> getAllJournals()
    {
        return uniqueJournals;
    }    
    
    public String addJournal(String journalName, String articleURI)
    {
        Journal jour = null;
                
        String VIVOUri = checkVIVOforTitle(journalName);
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
            if(VIVOUri != null)
            {
                //create new journal object
                jour = new Journal(journalName, "<" +  VIVOUri + ">");
                jour.setExistsInVIVO(true);
            }
            else
            {
                //create new journal object
                jour = new Journal(journalName, uniqueURIGen.generateNewURI());                
            }                    
            
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
