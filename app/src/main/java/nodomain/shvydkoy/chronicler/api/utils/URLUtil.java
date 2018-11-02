package nodomain.shvydkoy.chronicler.api.utils;


import java.net.MalformedURLException;
import java.net.URL;



final public class URLUtil
{

    /**
     * The method is introduced for providing executability both on OracleJava & Android API 14.
     * @param URLAdress
     * @return true if URLAdress is http or https one, otherwise returns false
     */
    public static boolean isHttpOrHttpsURL(final URL URLAdress)
    {
        return "http".equalsIgnoreCase(URLAdress.getProtocol()) || "https".equalsIgnoreCase(URLAdress.getProtocol());
    }

    public static boolean isValidHttpOrHttpsURL(final String link)
    {
        try
        {
            URL URLLink = new URL(link);
            if (!URLUtil.isHttpOrHttpsURL(URLLink))
            {
                return false;
            }
        }
        catch (MalformedURLException e)
        {
           return false;
        }

        return true;
    }
}