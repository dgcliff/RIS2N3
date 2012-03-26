/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class BaseTemplate
{    
    private String URI;
    
    private ArrayList<String> n3Values = new ArrayList<>();
    private UniqueURIGenerator uniqueURIGen;
    
    private boolean hasDate = false;
    private String dateURI;
    private String dateValue;
    
    public BaseTemplate(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        URI = uniqueURIGen.generateNewURI();
        n3Values.add(URI);
    }
    
    public boolean hasDate()
    {
        return hasDate;
    }
    
    public String getDateUri()
    {
        return dateURI;        
    }
    
    public String getDateValue()
    {
        return dateValue;
    }
    
    public void addType(String line)
    {
        n3Values.add("\ta "+ line + " ;");
    }
    
    public void addTitle(String line)
    {
        String title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
        n3Values.add("\t<http://vivoweb.org/ontology/core#title> \"" + title + "\" ;");
    }
    
    public void addKeywords(String line)
    {
        String keywords = (line.substring(line.indexOf("-") + 1, line.length())).trim();
        n3Values.add("\t<http://vivoweb.org/ontology/core#freetextKeyword> \"" + keywords + "\" ;");                
    }
    
    public void addDate(String line)
    {
        hasDate = true;
        
        String[] dateArray = ((line.substring(line.indexOf("-") + 1, line.length())).trim()).split("/");
        
        String year = dateArray[0];
        String month = "01";
        String day = "01";
        
        if(dateArray.length > 1)
        {
            month = dateArray[1];
        }
        if(dateArray.length > 2)
        {
            day = dateArray[2];
        }
        
        dateValue = "\"" + year + "-" + month + "-" + day + "T00:00:00" + "\"" + "^^<http://www.w3.org/2001/XMLSchema#dateTime>";        
        dateURI = uniqueURIGen.generateNewURI();        
        
        n3Values.add("\t<http://vivoweb.org/ontology/core#dateTimeValue> " + dateURI + " ;");
    }
    
    public void addAuthor(String line)
    {
        throw new UnsupportedOperationException("addAuthor hasn't been implemented yet");
        //n3Values.add("\t<http://vivoweb.org/ontology/core#informationResourceInAuthorship> \"" + titleStr + "\" ;");
    }
    
    public void completeEntry()
    {
        String finalEntry = n3Values.get(n3Values.size() - 1);
        finalEntry = finalEntry.replace(" ;", " .");
        n3Values.set(n3Values.size() - 1, finalEntry);
    }        
    
    public ArrayList<String> getEntry()
    {
        return n3Values;
    }
}
