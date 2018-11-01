package nodomain.shvydkoy.chronicler.api.utils.loadfile;

import java.io.InputStream;
import java.net.URL;
import nodomain.shvydkoy.chronicler.api.utils.URLUtil;
import java.io.FileInputStream;

import java.io.IOException;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;



final public class OpenStream
{
    final static private String EXCEPTION_DESCRIPTION_IF_URL_NOT_HTTP_HTTPS = "The URL string is not an http or https adress: ";



    final public static InputStream fromHttpURL(final String httpAdress) throws MalformedURLException, IOException
    {
        URL httpURL = new URL(httpAdress);

        if (!URLUtil.isHttpOrHttpsURL(httpURL))
        {
            MalformedURLException exception = new MalformedURLException(EXCEPTION_DESCRIPTION_IF_URL_NOT_HTTP_HTTPS + httpAdress);
            throw exception;
        }

        return httpURL.openStream();
    }

    final public static InputStream fromMedia(final String filePath) throws FileNotFoundException, SecurityException
    {
        return new FileInputStream(filePath);
    }


}