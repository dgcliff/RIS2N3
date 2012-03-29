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
public class Journal
{
    private ArrayList<String> articleURIList = new ArrayList<>();
    private String title;
    private String URI;
    
    public Journal(String titleVal, String URIVal)
    {
        title = titleVal;
        URI = URIVal;
    }
    
    public String getURI()
    {
        return URI;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void addArticle(String URI)
    {
        articleURIList.add(URI);
    }
    
    public ArrayList<String> getArticleURIs()
    {
        return articleURIList;
    }
}
