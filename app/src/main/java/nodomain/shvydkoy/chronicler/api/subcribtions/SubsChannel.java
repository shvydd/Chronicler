package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Item;

import static nodomain.shvydkoy.chronicler.api.utils.StringUtil.isBlank;

@SuppressWarnings("unused")
final public class SubsChannel extends Channel
{
    private final static String BLANK_STRING = "Field must contain at least one alphabetical or numerical symbol";
    private final static int ITEM_HASHMAP_INITIAL_CAPACITY = 50;
    private final static float ITEM_HASHMAP_LOAD_FACTOR = (float)0.8;
    private final static long DEFAULT_ITEM_STORAGE_TIME_IN_MILLI_S = 7*24*60*60*1000;

    private Calendar SubsriptionDate;
    private Calendar LastUpdateDate;

    final private LinkedHashMap<String ,SubsItem> SubsItemList;

    private String UserDefinedTitle;
    private String UserDefinedDescription;
    private String UserDefinedCategory;
    private long   ItemStorageTimeInMilliS; //if ItemStorageTimeInHours=-1 - until manually deleted

    private boolean subsriptionConfirmed;
    private boolean lastUpdateSuccessful;
    private int unreadItemsNumber;

    private File LastFeedFile;




    SubsChannel(final Channel parsedChannel)
    {
        super(parsedChannel,false);

        LastUpdateDate = Calendar.getInstance();
        if (SubsriptionDate == null)
        {
            SubsriptionDate = Calendar.getInstance();
        }

        this.ItemList = null;
        SubsItemList = new LinkedHashMap<>(ITEM_HASHMAP_INITIAL_CAPACITY, ITEM_HASHMAP_LOAD_FACTOR, false);
        addItems(parsedChannel);

        UserDefinedTitle = null;
        UserDefinedDescription = null;
        UserDefinedCategory = null;

        ItemStorageTimeInMilliS = DEFAULT_ITEM_STORAGE_TIME_IN_MILLI_S;

        subsriptionConfirmed = false;
        lastUpdateSuccessful = true;

        LastFeedFile = null;

        unreadItemsNumber = SubsItemList.size();
    }



    final void deleteItem(final String itemHashString)
    {
        synchronized (SubsItemList)
        {
            SubsItemList.remove(itemHashString);
        }
    }


    final void scheduledItemCleaning()
    {
        if (ItemStorageTimeInMilliS == -1)
        {
            return;
        }

        Calendar now = Calendar.getInstance();
        long timeGap;

        synchronized (SubsItemList)
        {
            for (LinkedHashMap.Entry<String, SubsItem> entry : SubsItemList.entrySet())
            {
                SubsItem subsItem = entry.getValue();
                timeGap = now.getTimeInMillis() - subsItem.getRecievedDate().getTimeInMillis();

                if ( !subsItem.isStarred() && timeGap > ItemStorageTimeInMilliS)
                {
                    deleteItem(entry.getKey());
                }

            }
        }
    }



    final void makeItemStarred(final String itemHashString)
    {
        SubsItem item = SubsItemList.get(itemHashString);

        if (null != item)
        {
            item.makeStarred();
        }
    }

    final void makeItemNotStarred(final String itemHashString)
    {
        SubsItem item = SubsItemList.get(itemHashString);

        if (null != item)
        {
            item.makeNotStarred();
        }
    }


    //Will be used on when user pushs "delete news item"
    final void hideItem(final String itemHashString)
    {
        SubsItem item = SubsItemList.get(itemHashString);

        if (null != item)
        {
            item.makeHidden();
        }
    }


    private void addItem(final Item item)
    {
        synchronized (SubsItemList)
        {
            if (null == SubsItemList.get(item.hashString()))
            {
                SubsItem newSubsItem = new SubsItem(item);
                SubsItemList.put(newSubsItem.hashString(), newSubsItem);
            }
        }
    }


    private void addItems (final Channel parsedChannel)
    {
        for (int i=parsedChannel.getItemList().size()-1; i>=0; i--)
        {
            addItem(parsedChannel.getItemList().get(i));
        }
    }


    final boolean updateAndReturnTrueIfTitleChanged(final Channel parsedChannel)
    {
        boolean titleChanged;

        titleChanged = !this.Title.equals(parsedChannel.getTitle());

        this.Title = parsedChannel.getTitle();
        this.Link = parsedChannel.getLink();
        this.Description = parsedChannel.getDescription();
        this.Language = parsedChannel.getLanguage();
        this.Copyright = parsedChannel.getCopyright();
        this.ManagingEditor = parsedChannel.getManagingEditor();
        this.WebMaster = parsedChannel.getWebMaster();
        this.PubDate = parsedChannel.getPubDate();
        this.LastBuildDate = parsedChannel.getLastBuildDate();
        this.Category = parsedChannel.getCategory();
        this.Cloud = parsedChannel.getCloud();
        this.TTL = parsedChannel.getTTL();
        this.Image = parsedChannel.getImage();
        this.SkipHours = parsedChannel.getSkipHours();
        this.SkipDays = parsedChannel.getSkipDays();

        addItems(parsedChannel);

        return titleChanged;
    }



    final void changeUserTitle(final String newTitle) throws UserNotifyingException
    {
        if (isBlank(newTitle))
        {
            throw new UserNotifyingException(BLANK_STRING);
        }

        UserDefinedTitle = newTitle;
    }


    final void restoreUserTitle()
    {
        UserDefinedTitle = null;
    }


    final void changeUserDescription(final String newDecription)
    {
        UserDefinedTitle = newDecription;
    }


    final void restoreUserDescription()
    {
        UserDefinedDescription = null;
    }


    final void changeUserCategory(final String newCategory)
    {
        UserDefinedCategory = newCategory;
    }


    final void restoreUserCategory()
    {
        UserDefinedCategory = null;
    }

    final void setItemStorageTime(long itemStorageTimeInMilliS)
    {
        ItemStorageTimeInMilliS = itemStorageTimeInMilliS;
    }

    final void setDefaultItemStorageTime()
    {
        ItemStorageTimeInMilliS = DEFAULT_ITEM_STORAGE_TIME_IN_MILLI_S;
    }



    final public String getUserTitle()
    {
        if (null != UserDefinedTitle)
        {
            return UserDefinedTitle;
        }
        else
        {
            return Title;
        }
    }


    final public String getUserDescription()
    {
        if (null != UserDefinedDescription)
        {
            return UserDefinedDescription;
        }
        else
        {
            return Description;
        }
    }


    final public String getUserCategory()
    {
        if (null != UserDefinedCategory)
        {
            return UserDefinedCategory;
        }
        else
        {
            return Category;
        }
    }


    final public boolean isConfirmed()
    {
        return subsriptionConfirmed;
    }

    final public void makeConfirmed()
    {
        this.subsriptionConfirmed = true;
    }

    final public void makeUnconfirmed()
    {
        this.subsriptionConfirmed = false;
    }

    final public File getLastFeedFile()
    {
        return LastFeedFile;
    }

    final public void setLastFeedFile(final File lastFeedFile)
    {
        LastFeedFile = lastFeedFile;
    }

    final public Date getLastUpdateDate()
    {
        return LastUpdateDate.getTime();
    }

    public final boolean isLastUpdateSuccessful()
    {
        return lastUpdateSuccessful;
    }

    public final Integer getUnreadItemsNumber()
    {
        return unreadItemsNumber;
    }

    public final void updateTempFile(final File newTempFile)
    {
        if (LastFeedFile != null)
        {
            LastFeedFile.delete();
        }
        LastFeedFile = newTempFile;
    }
}
