/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import entity.Author;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class BaseTemplate
{
    private String URI;
    
    private ArrayList<String> n3Values = new ArrayList<>();
    private ArrayList<String> authorURIList = new ArrayList<>();
    private UniqueURIGenerator uniqueURIGen;
    
    private boolean hasDate = false;
    private boolean hasAuthors = false;
    private boolean hasURL = false;
    
    private String URL;
    private String URLuri;
    
    private String dateURI;
    private String dateValue;
    
    public BaseTemplate(UniqueURIGenerator uUg, String URIVal)
    {
        uniqueURIGen = uUg;
        URI = URIVal;
        n3Values.add(URI);
    }
    
    public String getURI()
    {
        return URI;
    }
    
    public boolean hasURL()
    {
        return hasURL;
    }
    
    public boolean hasDate()
    {
        return hasDate;
    }
    
    public boolean hasAuthors()
    {
        return hasAuthors;
    }
    
    public String getURL()
    {
        return URL;
    }
    
    public String getURLuri()
    {
        return URLuri;
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
    
    public void addN3(String N3Str)
    {
        n3Values.add(N3Str);
    }
    
    public void addURL(String line)
    {
        hasURL = true;
        
        URL = (line.substring(line.indexOf("-") + 1, line.length())).trim();        
        URLuri = uniqueURIGen.generateNewURI();
        
        n3Values.add("\t<http://vivoweb.org/ontology/core#webpage> " + URLuri + " ;");
    }
    
    public void addTitle(String line)
    {
        String title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
        n3Values.add("\t<http://vivoweb.org/ontology/core#title> \"" + title + "\" ;");
        n3Values.add("\t<http://www.w3.org/2000/01/rdf-schema#label> \"" + title + "\" ;");
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
    
    public void addAuthor(Author au)
    {
        authorURIList.add(au.getURI());
        hasAuthors = true;
    }
    
    public ArrayList<String> getAuthorURIList()
    {
        return authorURIList;
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
