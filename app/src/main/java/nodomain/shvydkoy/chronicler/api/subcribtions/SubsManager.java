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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import nodomain.shvydkoy.chronicler.api.utils.loadfile.OpenStream;
import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Parser;
import nodomain.shvydkoy.chronicler.api.webfeed.ParsingFailedException;


//TODO make Subscriptions list synchronized. Replace for loops ( with Subscriptions.get(i) )  with iterator loops
//TODO If channel has been updated and get a link, identical to other channel link - notify user and merge Items. If identical link belongs to unconfirmed channel - remove reference to the unconfirmed channel (What if link is different, but redirects to identical?)


public final class SubsManager
{
    private final static String FAILED_CREATE_TEMP_FILE = "Failed to save temp channel file";

    final private List<SubsChannel> Subscriptions;



    public SubsManager()
    {
        Subscriptions = Collections.synchronizedList(new LinkedList<SubsChannel>());
    }



    public void addChannel(final String channelFeed) throws UserNotifyingException
    {
        final SubsChannel newSubsChannel;


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



        if(Subscriptions.size() == 0)
        {
            Subscriptions.add(newSubsChannel);
            //TODO show new channel
            Log.d("addChannel", "Subscriptions.size() == 0 block");
        }
        else
        {
            final Collator collator = Collator.getInstance();

            synchronized (Subscriptions)
            {
                final ListIterator<SubsChannel> existingSubsChannelIt = Subscriptions.listIterator();
                SubsChannel existingSubsChannel;

                while (existingSubsChannelIt.hasNext())
                {
                    existingSubsChannel = existingSubsChannelIt.next();

                    if (existingSubsChannel.getLink().equals(newSubsChannel.getLink()))
                    {
                        //TODO show existent channel
                        return;
                    }


                    if (existingSubsChannel.isConfirmed()  ||  collator.compare(newSubsChannel.getTitle(), existingSubsChannel.getTitle()) < 0)
                    {
                        existingSubsChannelIt.previous();
                        existingSubsChannelIt.add(newSubsChannel);
                        //TODO show new channel
                        return;
                    }

                }
            }
            //If there are only unconfirmed channels in Subscriptions, and title of newChannel is alphabetically bigger than titles of existent channels
            Subscriptions.add(newSubsChannel);
            //TODO show new channel
        }
    }



    final void deleteChannel(int position)
    {
        synchronized (Subscriptions)
        {
            Subscriptions.remove(position);
        }
    }



    final public void confirmChannel(int unconfirmedChannelPosition)
    {
        synchronized (Subscriptions)
        {
            final SubsChannel channelToConfirm = Subscriptions.get(unconfirmedChannelPosition);


            if (channelToConfirm != null)
            {
                if ( !channelToConfirm.isConfirmed() )
                {
                    channelToConfirm.makeConfirmed();
                    Subscriptions.remove(unconfirmedChannelPosition);

                    final Collator collator = Collator.getInstance();


                    final ListIterator<SubsChannel> existingSubsChannelIt = Subscriptions.listIterator();
                    SubsChannel existingSubsChannel;

                    while (existingSubsChannelIt.hasNext())
                    {
                        existingSubsChannel = existingSubsChannelIt.next();

                        if (!existingSubsChannel.isConfirmed())
                        {
                            continue;
                        }

                        if (collator.compare(channelToConfirm.getTitle(), existingSubsChannel.getTitle()) < 0)
                        {
                            existingSubsChannelIt.previous();
                            existingSubsChannelIt.add(channelToConfirm);
                            return;
                        }
                    }
                    //If the title of the confirmed channed is alphabetically bigger than titles of other confirmed channels
                    Subscriptions.add(channelToConfirm);
                }
            }
        }
    }



    final void updateChannel(int position, final Context context) throws UserNotifyingException
    {
        final SubsChannel channel = Subscriptions.get(position);


        final File channelTempFile;
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


        final InputStream channelInputStream;
        try
        {
            channelInputStream = OpenStream.fromMedia(channelTempFile.getPath());
        }
        catch(FileNotFoundException e)
        {
            Log.d("addChannel", e.getMessage());
            throw new UserNotifyingException(e.getMessage());
        }


        final Channel parsedChannel;
        try
        {
            parsedChannel = new Parser().parse(channelInputStream, channel.getLink().toString());
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

        channel.updateAndReturnTrueIfTitleChanged(parsedChannel);
        channel.updateTempFile(channelTempFile);
    }







    private File downloadFeedFile(final Context context, final SubsChannel channel) throws UserNotifyingException
    {
        HttpURLConnection connection = null;
        final File channelTempFile;
        FileOutputStream channelToFileStream = null;

        try
        {
            connection = (HttpURLConnection) channel.getLink().openConnection();
            final InputStream channelStream = connection.getInputStream();

            //TODO check if file at the link is feedfile. Add reconnects

            final ReadableByteChannel channelByteChannel = Channels.newChannel(channelStream);

            channelTempFile = getTempFile(context , channel.getLink().toString());

            channelToFileStream = new FileOutputStream(channelTempFile);
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

            try
            {
                if (null != channelToFileStream)
                    channelToFileStream.close();
            }
            catch (IOException e)
            {
                Log.d("SubsChannel.Download()", e.getMessage());
            }


        }

        return channelTempFile;
    }


    private File getTempFile(final Context context, final String url) throws UserNotifyingException
    {
        File file;
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
        // file must not been deleted in "finally", because it can be used during next app run

        return file;
    }


    final public List<SubsChannel> getSubscriptions()
    {
        Log.d("getAllSubs", "total number of subs " + Subscriptions.size());

        return Subscriptions;
    }


}
