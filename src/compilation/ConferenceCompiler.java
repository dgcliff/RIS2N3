/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Conference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class ConferenceCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Conference> uniqueConferences;
    Map<String, String> VIVOpublicationList = new HashMap<>();
    
    public ConferenceCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueConferences = new ArrayList<>();
        
        //Check the database
        String query = "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> PREFIX bibo: <http://purl.org/ontology/bibo/> SELECT ?conference ?conferenceTitle WHERE { ?conference a bibo:Conference ; rdfs:label ?conferenceTitle . }" ;
        VIVOpublicationList = uniqueURIGen.sparqlController.queryVIVO(query, "conference", "conferenceTitle");
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
    
    public ArrayList<Conference> getAllConferences()
    {
        return uniqueConferences;
    }    
    
    public String addConference(String conferenceName, String paperURI)
    {
        Conference con = null;
        
        String VIVOUri = checkVIVOforTitle(conferenceName);
        
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
            if(VIVOUri != null)
            {
                //create new journal object
                con = new Conference(conferenceName, VIVOUri);
                con.setExistsInVIVO(true);
            }
            else
            {
                //create new journal object
                con = new Conference(conferenceName, uniqueURIGen.generateNewURI());                
            }
            
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
