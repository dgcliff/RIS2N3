/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class Publication
{
    private String URI = "";
    private String title = "";
    private ArrayList<Author> authors = new ArrayList<>();
    private ArrayList<String> rawPublicationEntry;    
    
    public void setURI(String URIval)
    {
        URI = URIval;
    }
    
    public void addAuthor(Author a)
    {
        //is author already in our list?
        boolean unique = true;
        
        for(Author x : authors)
        {
            if(x == a)
            {
                unique = false;
            }
        }
        
        if(unique)
        {
            authors.add(a);
        }
    }
    
    public ArrayList<Author> getAuthors()
    {
        return authors;
    }
    
    public void setTitle(String titleStr)
    {
        title = titleStr;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getURI()
    {
        return URI;
    }
    
    public void setRawPublicationEntry(ArrayList<String> rPe)
    {
        rawPublicationEntry = rPe;
    }
    
    public ArrayList<String> getRawPublicationEntry()
    {
        return rawPublicationEntry;
    }
}
