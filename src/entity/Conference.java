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
public class Conference
{
    private ArrayList<String> articleURIList = new ArrayList<>();
    private String title;
    private String URI;
    private boolean existsInVIVO = false;
    
    public Conference(String titleVal, String URIVal)
    {
        title = titleVal;
        URI = URIVal;
    }
    
    public void setExistsInVIVO(boolean b)
    {
        existsInVIVO = b;
    }
    
    public boolean getExistsInVIVO()
    {
        return existsInVIVO;
    }    
    
    public String getURI()
    {
        return URI;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void addPaper(String URI)
    {
        articleURIList.add(URI);
    }
    
    public ArrayList<String> getPaperURIs()
    {
        return articleURIList;
    }
}
