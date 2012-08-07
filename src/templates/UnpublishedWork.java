/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import entity.Author;
import entity.Publication;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class UnpublishedWork extends BaseTemplate
{
    public UnpublishedWork(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC, Publication pub)
    {
        super(uniqueNumberGen, pub.getURI());
        
        addType("<http://purl.org/ontology/bibo/Document>");
        addType("<http://vivoweb.org/ontology/core#InformationResource>");
        
        addTitle(pub.getTitle());
        
        for(Author a : pub.getAuthors())
        {
            addAuthor(a);
        }        
        
        for(String line : newEntry)
        {
            if (line.startsWith("KW"))
            {
                addKeywords(line);
            }
            else if (line.startsWith("Y1") || line.startsWith("PY"))
            {
                addDate(line);
            }   
            else if (line.startsWith("UR"))
            {
                addURL(line);
            }            
        }
        
        completeEntry();
    }    
}
