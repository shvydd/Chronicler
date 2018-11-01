package nodomain.shvydkoy.chronicler.api.subcribtions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.utils.loadfile.OpenStream;
import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Parser;
import nodomain.shvydkoy.chronicler.api.webfeed.parsingException.FailedParsingException;



public final class SubsChannel extends Channel
{
    private final static String FAILED_UPDATE_CHANNEL_CONNECTION_PROBLEM = "Failed to refresh the channel";
    private final static String FAILED_CREATE_TEMP_FILE = "Failed to save temp channel file";


    private Calendar SubsriptionDate;
    private Calendar LastUpdateDate;
    private File channelTempFile;
    


    public SubsChannel(Channel parsedChannel)
    {
        super(parsedChannel);

        LastUpdateDate = Calendar.getInstance();

        if (SubsriptionDate == null)
        {
            SubsriptionDate = Calendar.getInstance();
        }

        //TODO Delete temp file if exist

    }

    final public void update() throws UserNotifyingException
    {
        download();
        Parser parser;

        try
        {
           parser  = new Parser();
        }
        catch (XmlPullParserException e)
        {
            throw new UserNotifyingException(e.getLocalizedMessage());
        }

        Channel updatedFeed;

        try
        {
            updatedFeed = parser.parse(OpenStream.fromMedia(channelTempFile.getPath()), this.getLink().toString());
        }
        catch (FileNotFoundException e)
        {
            throw new UserNotifyingException(e.getMessage());
        }
        catch (XmlPullParserException e)
        {
            throw new UserNotifyingException(e.getMessage());
        }
        catch (FailedParsingException e)
        {
            throw new UserNotifyingException(e.getMessage());
        }
        catch (IOException e)
        {
            throw new UserNotifyingException(e.getMessage());
        }

        addNewItems (updatedFeed);


    }


    private void download() throws UserNotifyingException
    {
        HttpURLConnection channelConnection = null;

        try
        {
            channelConnection = (HttpURLConnection) this.getLink().openConnection();
            InputStream channelStream = channelConnection.getInputStream();

            if ( ! this.getLink().equals(channelConnection.getURL()) )
            {
                throw new UserNotifyingException(FAILED_UPDATE_CHANNEL_CONNECTION_PROBLEM);
            }

            ReadableByteChannel channelByteChannel = Channels.newChannel(channelStream);

            channelTempFile = getTempFile(Context, this.getLink().toString());

            FileOutputStream channelToFileStream = new FileOutputStream(channelTempFile);
            channelToFileStream.getChannel().transferFrom(channelByteChannel, 0, Long.MAX_VALUE);
        }
        catch (IOException e)
        {
            Log.d("SubsChannel.Download()", e.getMessage());
        }
        finally
        {
            try
            {
                channelConnection.disconnect();
            }
            catch (NullPointerException e)
            {
                Log.d("SubsChannel.Download()", e.getMessage());
            }
        }
    }


    private File getTempFile(Context context, String url) throws UserNotifyingException
    {
        File file;
        try
        {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        }
        catch (IOException e)
        {
            Log.d("SubsCh..l.getTempFile()", e.getMessage());
            throw new UserNotifyingException(FAILED_CREATE_TEMP_FILE);
        }
        finally
        {
            //file.delete();
        }
        return file;
    }


    private void addNewItems (Channel channel)
    {
        for (int j=0; j<channel.getItemList().size(); j++)
        {
            for(int i=0; i<this.ItemList.size(); i++)
            {
                if ( ! channel.getItemList().get(j).equals(this.ItemList.get(i)) )
                {
                    this.addItem(new SubsItem(channel.getItemList().get(j)));
                }
            }
        }
    }

    private void rewriteChannelHeader (Channel channel)
    {
        this.Title = channel.getTitle();
        this.Link = channel.getLink();

    }

}
