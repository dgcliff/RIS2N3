/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author dgcliff
 */
public class Author
{
    private String URI = "";
    
    private ArrayList<String> relatedItems;
    
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String firstInitial = "";
    private String middleInitial = "";
    
    private String fullName;
    
    public Author(String authorFullName, String URIstr)
    {
        URI = URIstr;
        
        relatedItems = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(authorFullName, ",");        
        
        while(st.hasMoreTokens())
        {
            String tempToken = st.nextToken();
            
            if(lastName.equals(""))
            {
                setLastName(tempToken);
            }
            else
            {
                ArrayList<String> nameValues = new ArrayList<>();
                
                //Robert Light has provided RIS files where the two initials
                //have no period seperating them. New step to identify when this
                //happens, and split them
                
                tempToken = tempToken.trim();
                
                if(tempToken.length() == 2 && !tempToken.contains(".") && Character.isUpperCase(tempToken.charAt(0)) && Character.isUpperCase(tempToken.charAt(1)))
                {
                    nameValues.add(Character.toString(tempToken.charAt(0)));
                    nameValues.add(Character.toString(tempToken.charAt(1)));
                }
                else
                {
                    tempToken = tempToken.replace(".", " ");
                    tempToken = tempToken.trim();
                    String[] spaceSplit = tempToken.split(" ");                

                    for(final String s : spaceSplit)
                    {
                        if(!s.equals(" ") && !s.equals(""))
                        {
                            nameValues.add(s);
                        }
                    }
                }

                //do we have a middle and first?
                if(nameValues.size() == 1)
                {
                    //is it an initial or full first name?
                    //if(spaceSplit[0].length() == 1)
                    if(nameValues.get(0).length() == 1)
                    {
                        setFirstInitial(nameValues.get(0));
                    }
                    else
                    {
                        setFirstName(nameValues.get(0));
                    }

                    break;
                }
                else
                {
                    //is it an initial or full first name?
                    if(nameValues.get(0).length() == 1)
                    {
                        setFirstInitial(nameValues.get(0));
                    }
                    else
                    {
                        setFirstName(nameValues.get(0));
                    }

                    //is it an initial or full middle name?
                    if(nameValues.get(1).length() == 1)
                    {
                        setMiddleInitial(nameValues.get(1));
                    }
                    else
                    {
                        setMiddleName(nameValues.get(1));
                    }

                    break;
                }
            }
        }
        
        compileFullName();
    }
    
    public String getURI()
    {
        return URI;
    }
    
    public String getFullName()
    {        
        return fullName;
    }
    
    public void addRelatedItem(String relatedItem)
    {
        relatedItems.add(relatedItem);
    }
    
    public ArrayList<String> getRelatedItems()
    {
        return relatedItems;
    }
    
    private void compileFullName()
    {
        fullName = lastName;
        
        //if we have a full first name add it
        if(!firstName.equals(""))
        {
            fullName = fullName + ", " + firstName;
        }
        else
        {
            //else add first initial
            fullName = fullName + ", " + firstInitial + ".";
        }
        
        //if we have a full middle name add it
        if(!middleName.equals(""))
        {
            fullName = fullName + " " + middleName;
        }
        else if(!middleInitial.equals(""))
        {
            //else add middle initial (if we have it)
            fullName = fullName  + " " + middleInitial + ".";
        }                
        
        fullName = fullName.trim();
    }
    
    private void setFirstName(String nameStr)
    {
        firstName = nameStr;
        
        if(firstInitial.equals(""))
        {
            setFirstInitial(firstName.substring(0,1));
        }
    }
    
    private void setMiddleName(String nameStr)
    {
        middleName = nameStr;
        
        if(middleInitial.equals(""))
        {
            setFirstInitial(middleName.substring(0,1));
        }        
    }    
    
    private void setLastName(String nameStr)
    {
        lastName = nameStr;
    }
    
    private void setFirstInitial(String initialStr)
    {
        firstInitial = initialStr;
    }
    
    private void setMiddleInitial(String initialStr)
    {
        middleInitial = initialStr;
    }
    
    //--------------------------------------------------------------------------
    
    public String getFirstName()
    {        
        return firstName;
    }
    
    public String getMiddleName()
    {
        return middleName;       
    }    
    
    public String getLastName()
    {
        return lastName;
    }
    
    public String getFirstInitial()
    {
        return firstInitial;
    }
    
    private String getMiddleInitial()
    {
        return middleInitial;
    }     
    
    public boolean isTheSamePersonAs(Author externalAuthor)
    {
        //this method needs to log every time it assumes Person A is equal to
        //Person B when their details aren't exactly the same
        
        //we're taking a Best Guess approach with last name and first initial
        //and in the event that we're wrong, we need an audit trail of when the
        //assumption was made
        
        boolean equality = false;
        
        //make comparison        
        //same last name?
        if(externalAuthor.getLastName().equalsIgnoreCase(getLastName()))
        {
            if(externalAuthor.getFirstInitial().equalsIgnoreCase(getFirstInitial()))
            //same first initial?
            {
                equality = true;

                //do they have more information than this version of the author?
                if(getFirstName().equals("") && !externalAuthor.getFirstName().equals(""))
                {
                    //if they do, make this author more complete
                    setFirstName(externalAuthor.getFirstName());
                }
                
                if(getMiddleName().equals("") && !externalAuthor.getMiddleName().equals(""))
                {
                    //if they do, make this author more complete
                    setMiddleName(externalAuthor.getFirstName());
                }
                
                if(getMiddleInitial().equals("") && !externalAuthor.getMiddleInitial().equals(""))
                {
                    //if they do, make this author more complete
                    setMiddleInitial(externalAuthor.getFirstName());
                }
                
                compileFullName();
            }
        }
        
        return equality;
    }
}
