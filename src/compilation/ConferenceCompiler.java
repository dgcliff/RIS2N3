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
    
    public String checkVIVOforTitle(String title)
    {
        //Check the database
        String query = "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> PREFIX bibo: <http://purl.org/ontology/bibo/> SELECT ?conference WHERE { ?conference a bibo:Conference ; rdfs:label \"" + title + "\" . }" ;
        return uniqueURIGen.sparqlController.checkForURI(query);        
    }
    
    public ArrayList<Conference> getAllConferences()
    {
        return uniqueConferences;
    }    
    
    public String addConference(String conferenceName, String paperURI)
    {
        Conference con = null;
        
        String VIVOUri = checkVIVOforTitle(p.getTitle());
        
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
