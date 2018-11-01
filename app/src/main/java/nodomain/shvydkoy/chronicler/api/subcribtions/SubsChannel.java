package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.util.ArrayList;
import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.webfeed.Channel;



final class SubsChannel extends Channel
{
    private final static String FAILED_UPDATE_CHANNEL_CONNECTION_PROBLEM = "Failed to refresh the channel";
    private final static String FAILED_CREATE_TEMP_FILE = "Failed to save temp channel file";


    private Calendar SubsriptionDate;
    private Calendar LastUpdateDate;
    private ArrayList<SubsItem> SubsItemList;



    SubsChannel(Channel parsedChannel)
    {
        super(parsedChannel,false);

        LastUpdateDate = Calendar.getInstance();
        if (SubsriptionDate == null)
        {
            SubsriptionDate = Calendar.getInstance();
        }

        this.ItemList = null;
        SubsItemList = new ArrayList<>();
        addNewItems(parsedChannel);
    }



    final void addNewItems (final Channel parsedChannel)
    {
        if (parsedChannel.getItemList().size()>0)
        {
            this.SubsItemList.add(new SubsItem(parsedChannel.getItemList().get(0)));

            throughNewItemsLoop: for (int i=parsedChannel.getItemList().size()-1; i>0 ; i--)
            {
                for (int j=0; j<SubsItemList.size(); j++)
                {
                    if ( parsedChannel.getItemList().get(i).equals(SubsItemList.get(j)))
                    {
                        continue throughNewItemsLoop;
                    }
                }
                this.SubsItemList.add(0, new SubsItem(parsedChannel.getItemList().get(i)));
            }

        }

    }

    /*final public void update() throws UserNotifyingException
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


    }*/


    /*private void download() throws UserNotifyingException
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
    }*/






}
