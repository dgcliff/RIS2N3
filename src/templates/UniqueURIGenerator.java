/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

/**
 *
 * @author dgcliff
 */
public class UniqueURIGenerator
{
    private static String BASE_URI = "http://vivo-trunk.indiana.edu/individual/";
    private int num = 1000;
    
    private int getUniqueNumber()
    {
        num += 1;
        return num;        
    }
    
    public String generateNewURI()
    {
        return "<" + BASE_URI + "a" + getUniqueNumber() + ">";
    }
}
