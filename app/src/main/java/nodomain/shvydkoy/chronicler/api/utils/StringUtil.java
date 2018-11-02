package nodomain.shvydkoy.chronicler.api.utils;

//import java.util.regex.Pattern;


final public class StringUtil
{
    /*
     * @return true if string does not contain at least one alphabetical or numerical symbol
     */
    public static boolean isBlank(final String string)
    {
        if (null != string)
        {
            for (int i=0; i<string.length(); i++)
            {
                if(Character.isLetter(string.charAt(i)) ||  Character.isDigit(string.charAt(i)) )
                {
                    return false;
                }
            }
        }

        return true;
    }


    /*//https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
    final static private String USER_NON_MEANINGFUL_REGEX = "[\\s|\\p{Punct}]+";


    final public static boolean isBlank(final String string)
    {
        boolean result = true;


        if (null != string)
        {
            return string.matches(USER_NON_MEANINGFUL_REGEX);
        }

        return result;
    }*/


    /*
     * @return better readable in singleline textfields String
     */
    public static String removeBlankEdgesAndLineBreaks(final String string)
    {
        if (null != string)
        {
            return string.trim().replaceAll("\r", "").replaceAll("\n", "");
        }

        return null;
    }


}