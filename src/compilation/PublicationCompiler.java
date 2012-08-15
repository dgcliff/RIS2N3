/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Publication;
import java.util.ArrayList;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class PublicationCompiler
{
    UniqueURIGenerator uniqueURIGen;
    ArrayList<Publication> publicationList = new ArrayList<>();
    
    public PublicationCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
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
        String query = "PREFIX core: <http://vivoweb.org/ontology/core#> SELECT ?publication WHERE { ?publication a core:InformationResource ; core:title \"" + title + "\" . }" ;
        return uniqueURIGen.sparqlController.checkForURI(query);        
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

