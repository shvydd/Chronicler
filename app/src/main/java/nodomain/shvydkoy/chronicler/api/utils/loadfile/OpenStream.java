package nodomain.shvydkoy.chronicler.api.utils.loadfile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import nodomain.shvydkoy.chronicler.api.utils.URLUtil;



final public class OpenStream
{
    final static private String EXCEPTION_DESCRIPTION_IF_URL_NOT_HTTP_HTTPS = "The URL string is not an http or https adress: ";



    public static InputStream fromHttpURL(final String httpAdress) throws MalformedURLException, IOException
    {
        URL httpURL = new URL(httpAdress);


        if (!URLUtil.isHttpOrHttpsURL(httpURL))
        {
            throw new MalformedURLException(EXCEPTION_DESCRIPTION_IF_URL_NOT_HTTP_HTTPS + httpAdress);
        }

        InputStream URLInputStream = null;
        try
        {
            URLInputStream = httpURL.openStream();
            return URLInputStream;
        }
        finally
        {
            if (null != URLInputStream)
                    URLInputStream.close();
        }



    }

    public static InputStream fromMedia(final String filePath) throws FileNotFoundException, SecurityException
    {
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream(filePath);
            return fis;
        }
        finally
        {
            if (null != fis)
            {
                try
                {
                    fis.close();
                }
                catch(IOException e)
                {

                }
            }

        }
    }

    public static InputStream fromString(final String channelFeed) throws FileNotFoundException, SecurityException
    {
        return new ByteArrayInputStream(channelFeed.getBytes());
    }


}