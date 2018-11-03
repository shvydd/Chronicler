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
import java.util.Hashtable;

import nodomain.shvydkoy.chronicler.api.utils.loadfile.OpenStream;
import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Parser;
import nodomain.shvydkoy.chronicler.api.webfeed.ParsingFailedException;




public final class SubsManager
{
    private final static String FAILED_CREATE_TEMP_FILE = "Failed to save temp channel file";
    private final static int CHANNEL_HASHMAP_INITIAL_CAPACITY = 25;
    private final static float CHANNEL_HASHMAP_LOAD_FACTOR = (float)0.8;

    final private Hashtable<String, SubsChannel> Subscriptions;
    final private Hashtable<String, SubsChannel> UnconfirmedSubscriptions;


    //TODO If channels have identical links - merge (What if link is different, but redirects to identical?)

    SubsManager()
    {
        Subscriptions = new Hashtable<>(CHANNEL_HASHMAP_INITIAL_CAPACITY, CHANNEL_HASHMAP_LOAD_FACTOR);
        UnconfirmedSubscriptions = new Hashtable<>(CHANNEL_HASHMAP_INITIAL_CAPACITY, CHANNEL_HASHMAP_LOAD_FACTOR);
    }



    private void addChannel(final String channelFeed) throws UserNotifyingException
    {
        SubsChannel newSubsChannel;

        try
        {
            newSubsChannel = new SubsChannel(new Parser().parse(OpenStream.fromString(channelFeed), null));
        }
        catch (XmlPullParserException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (ParsingFailedException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }

        if (null != Subscriptions.get(newSubsChannel.hashString()))
        {
            //TODO show existent channel
        }
        else if (null != UnconfirmedSubscriptions.get(newSubsChannel.hashString()))
        {
            //TODO show existent channel
        }
        else if (null == UnconfirmedSubscriptions.get(newSubsChannel.hashString()))
        {
            UnconfirmedSubscriptions.put(newSubsChannel.hashString(), newSubsChannel);
            //TODO show new channel
        }

    }



    final void deleteConfirmedChannel(final String channelHashString)
    {
        Subscriptions.remove(channelHashString);
    }


    final void deleteUnconfirmedConfirmedChannel(final String channelHashString)
    {
        UnconfirmedSubscriptions.remove(channelHashString);
    }



    final void updateUnconfirmedChannel(final String channelHashString, final Context context) throws UserNotifyingException
    {
        SubsChannel channel = UnconfirmedSubscriptions.get(channelHashString);
        File channelTempFile;

        try
        {
            if (null != channel)
            {
                channelTempFile = downloadFeedFile(context, channel);
            }
            else
            {
                return;
            }
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }

        InputStream channelUnputStream;

        try
        {
            channelUnputStream = OpenStream.fromMedia(channelTempFile.getPath());
        }
        catch(FileNotFoundException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }


        Channel parsedChannel;

        try
        {
            parsedChannel = new Parser().parse(channelUnputStream, channel.getLink().toString());
        }
        catch (XmlPullParserException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (ParsingFailedException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }

        boolean titleChanged = channel.refreshAndReturnTrueIfTitleChanged(parsedChannel);

        if (titleChanged)
        {
            //TODO Notify user
        }

    }


    final void updateConfirmedChannel(final String channelHashString, final Context context) throws UserNotifyingException
    {
        SubsChannel channel = Subscriptions.get(channelHashString);
        File channelTempFile;

        try
        {
            if (null != channel)
            {
                channelTempFile = downloadFeedFile(context, channel);
            }
            else
            {
                return;
            }
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }

        InputStream channelUnputStream;

        try
        {
            channelUnputStream = OpenStream.fromMedia(channelTempFile.getPath());
        }
        catch(FileNotFoundException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }


        Channel parsedChannel;

        try
        {
            parsedChannel = new Parser().parse(channelUnputStream, channel.getLink().toString());
        }
        catch (XmlPullParserException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (ParsingFailedException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }

        boolean titleChanged = channel.refreshAndReturnTrueIfTitleChanged(parsedChannel);

        if (titleChanged)
        {
            //TODO Notify user
        }
    }


    private File downloadFeedFile(final Context context, final SubsChannel channel) throws UserNotifyingException  //TODO Get context
    {
        HttpURLConnection connection = null;
        File channelTempFile;

        try
        {
            connection = (HttpURLConnection) channel.getLink().openConnection();
            InputStream channelStream = connection.getInputStream();

            //TODO check if file at the link is feedfile.

            ReadableByteChannel channelByteChannel = Channels.newChannel(channelStream);

            channelTempFile = getTempFile(context , channel.getLink().toString());

            FileOutputStream channelToFileStream = new FileOutputStream(channelTempFile);
            channelToFileStream.getChannel().transferFrom(channelByteChannel, 0, Long.MAX_VALUE);
        }
        catch (IOException e)
        {
            Log.d("SubsChannel.Download()", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }
        finally
        {
            try
            {
                if (null != connection)
                    connection.disconnect();
            }
            catch (NullPointerException e)
            {
                Log.d("SubsChannel.Download()", e.getMessage());
            }
        }

        return channelTempFile;
    }


    private File getTempFile(final Context context, final String url) throws UserNotifyingException
    {
        File file = null;
        String fileName = Uri.parse(url).getLastPathSegment();

        try
        {
            file = File.createTempFile(fileName, null, context.getCacheDir());
        }
        catch (IOException e)
        {
            Log.d("SubsM...r.getTempFile()", e.getMessage());
            throw new UserNotifyingException(FAILED_CREATE_TEMP_FILE);
        }
        finally
        {
            if (null != file && file.isFile())
                file.delete(); //TODO why "ignored" warning?
        }

        return file;
    }



}
