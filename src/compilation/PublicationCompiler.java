/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

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
public class PublicationCompiler
{
    UniqueURIGenerator uniqueURIGen;
    ArrayList<Publication> publicationList = new ArrayList<>();
    Map<String, String> VIVOpublicationList = new HashMap<>();
    
    public PublicationCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        
        //query against vivo for all publication titles, put into a list
        //querying one by one is horribly inefficient and slow
        String query = "PREFIX core: <http://vivoweb.org/ontology/core#> SELECT ?publication ?publicationTitle WHERE { ?publication a core:InformationResource ; core:title ?publicationTitle . }" ;
        VIVOpublicationList = uniqueURIGen.sparqlController.queryVIVO(query, "publication" ,"publicationTitle");
    }
    
    public boolean isTitleUnique(String title)
    {
        boolean unique = true;
        
        for(Publication p : publicationList)
        {
            if(p.getTitle().equalsIgnoreCase(title))
            {
                unique = false;
                break;
            }
        }        
        
        return unique;
    }
    
    public String checkVIVOforTitle(String title)
    {
        //Check the database
        //String query = "PREFIX core: <http://vivoweb.org/ontology/core#> SELECT ?publication WHERE { ?publication a core:InformationResource ; core:title \"" + title + "\" . }" ;
        //return uniqueURIGen.sparqlController.checkForURI(query);        
        
        String VIVOtitle = null;
        
        Iterator iterator = VIVOpublicationList.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry pairs = (Map.Entry)iterator.next();
            String s = (String) pairs.getValue();
            if(s.equalsIgnoreCase(title))
            {
                VIVOtitle = (String) pairs.getKey();
            }
        }                
        
        return VIVOtitle;
    }
    
    public void addPublication(Publication p)
    {
        String VIVOUri = checkVIVOforTitle(p.getTitle());
        
        if(VIVOUri != null)
        {
            p.setURI(VIVOUri);
            p.setExistsInVIVO(true);
        }
        {
            p.setURI(uniqueURIGen.generateNewURI());            
        }
                      
        publicationList.add(p);
    }
    
    public Publication getPublication(String title)
    {
        for(Publication p : publicationList)
        {
            if(p.getTitle().equalsIgnoreCase(title))
            {
                return p;
            }
        }        
        
        return null;
    }
    
    public ArrayList<Publication> getPublicationList()
    {
        return publicationList;
    }
}

