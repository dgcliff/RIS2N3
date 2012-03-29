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
