/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Conference;
import java.util.ArrayList;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class ConferenceCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Conference> uniqueConferences;    
    
    public ConferenceCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueConferences = new ArrayList<>();
    }
    
    public ArrayList<Conference> getAllConferences()
    {
        return uniqueConferences;
    }    
    
    public String addConference(String conferenceName, String paperURI)
    {
        Conference con = null;
                
        String URI = "";
        
        for(Conference c : uniqueConferences)
        {
            if(c.getTitle().equalsIgnoreCase(conferenceName))
            {
                URI = c.getURI();
                con = c;
                break;
            }
        }
        
        if(URI.equals(""))
        {            
            //create new journal object
            con = new Conference(conferenceName, uniqueURIGen.generateNewURI());
            
            //URI = new URI
            URI = con.getURI();
         
            //add article
            con.addPaper(paperURI);
            
            //add to uniqueConferences
            uniqueConferences.add(con);
        }
        else
        {
            //add article
            con.addPaper(paperURI);
        }
        
        return URI;
    }    
}
