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
import java.text.Collator;
import java.util.ArrayList;

import nodomain.shvydkoy.chronicler.api.utils.loadfile.OpenStream;
import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Parser;
import nodomain.shvydkoy.chronicler.api.webfeed.ParsingFailedException;




public final class SubsManager
{
    private final static String FAILED_CREATE_TEMP_FILE = "Failed to save temp channel file";

    final private ArrayList<SubsChannel> Subscriptions;
    final private ArrayList<SubsChannel> UnconfirmedSubscriptions;


    //TODO If channels have identical links - merge (What if link is different, but redirects to identical?)

    public SubsManager()
    {
        Subscriptions = new ArrayList<>();
        UnconfirmedSubscriptions = new ArrayList<>();
    }



    public void addChannel(final String channelFeed) throws UserNotifyingException
    {
        SubsChannel newSubsChannel;

        Log.d("addChannel", "UnconfirmedSubscriptions.size() = " + UnconfirmedSubscriptions.size());

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

        Log.d("addChannel", "No exceptions");

        for (int i=0; i<UnconfirmedSubscriptions.size(); i++)
        {
            if ( UnconfirmedSubscriptions.get(i).getLink().equals(newSubsChannel.getLink()))
            {
                //TODO show existent channel
                return;
            }
        }


        for (int i=0; i<Subscriptions.size(); i++)
        {
            if ( Subscriptions.get(i).getLink().equals(newSubsChannel.getLink()))
            {
                //TODO show existent channel
                return;
            }
        }


        if(UnconfirmedSubscriptions.size() == 0)
        {
            UnconfirmedSubscriptions.add(newSubsChannel);
            //TODO show new channel
            return;
        }
        else
        {
            Collator collator = Collator.getInstance();
            for (int i=0; i<UnconfirmedSubscriptions.size(); i++)
            {
                if (collator.compare(newSubsChannel.getTitle(), UnconfirmedSubscriptions.get(i).getTitle()) < 0)
                {
                    UnconfirmedSubscriptions.add(i, newSubsChannel);
                    //TODO show new channel
                    return;
                }
            }

            UnconfirmedSubscriptions.add(newSubsChannel);
            //TODO show new channel
            return;
        }


    }



    final void deleteConfirmedChannel(int position)
    {
        Subscriptions.remove(position);
    }


    final void deleteUnconfirmedConfirmedChannel(int position)
    {
        UnconfirmedSubscriptions.remove(position);
    }



    final void updateUnconfirmedChannel(int position, final Context context) throws UserNotifyingException
    {
        SubsChannel channel = UnconfirmedSubscriptions.get(position);
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


    final void updateConfirmedChannel(int position, final Context context) throws UserNotifyingException
    {
        SubsChannel channel = Subscriptions.get(position);
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


    final void confirmChannel(int unconfirmedChannelPosition)
    {
       SubsChannel channelToConfirm = UnconfirmedSubscriptions.get(unconfirmedChannelPosition);
       if (channelToConfirm != null)
       {
           if(Subscriptions.size() == 0)
           {
               Subscriptions.add(channelToConfirm);
           }
           else
           {
               Collator collator = Collator.getInstance();
               for (int i=0; i<Subscriptions.size(); i++)
               {
                   if (collator.compare(channelToConfirm.getTitle(), Subscriptions.get(i).getTitle()) < 0)
                   {
                       Subscriptions.add(i, channelToConfirm);
                       break;
                   }
               }

               Subscriptions.add(channelToConfirm);
           }

           UnconfirmedSubscriptions.remove(unconfirmedChannelPosition);
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


    final public ArrayList<SubsChannel> getAllSubscriptions()
    {

        Log.d("getAllSubs", "total number of UnconfirmedSubs " + UnconfirmedSubscriptions.size());

        ArrayList<SubsChannel> AllSubscriptions = new ArrayList<>(UnconfirmedSubscriptions);
        AllSubscriptions.addAll(Subscriptions);

        Log.d("getAllSubs", "total number of subs " + AllSubscriptions.size());

        return AllSubscriptions;
    }


}
