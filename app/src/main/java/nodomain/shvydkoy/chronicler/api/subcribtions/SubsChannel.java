package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import nodomain.shvydkoy.chronicler.api.webfeed.Channel;
import nodomain.shvydkoy.chronicler.api.webfeed.Item;

import static nodomain.shvydkoy.chronicler.api.utils.StringUtil.isBlank;

@SuppressWarnings("unused")
final public class SubsChannel extends Channel
{
    private final static String BLANK_STRING = "Field must contain at least one alphabetical or numerical symbol";
    private final static long DEFAULT_ITEM_STORAGE_TIME_IN_MILLI_S = 7*24*60*60*1000;

    private Calendar SubsriptionDate;
    private Calendar LastUpdateDate;

    final private List<SubsItem> SubsItemList;

    private String UserDefinedTitle;
    private String UserDefinedDescription;
    private String UserDefinedCategory;
    private long   ItemStorageTimeInMilliS; //if ItemStorageTimeInHours=-1 - until manually deleted

    private boolean subsriptionConfirmed;
    private boolean lastUpdateSuccessful;
    private int unreadItemsNumber;

    private File LastFeedFile;
    private File ImageFile; //TODO Make image file infrastructure




    SubsChannel(final Channel parsedChannel)
    {
        super(parsedChannel,false);
        this.ItemList = null;

        LastUpdateDate = Calendar.getInstance();
        if (SubsriptionDate == null)  { SubsriptionDate = Calendar.getInstance(); }

        SubsItemList = Collections.synchronizedList(new LinkedList<SubsItem>());
        addItems(parsedChannel);

        UserDefinedTitle = null;
        UserDefinedDescription = null;
        UserDefinedCategory = null;

        ItemStorageTimeInMilliS = DEFAULT_ITEM_STORAGE_TIME_IN_MILLI_S;

        subsriptionConfirmed = false;
        lastUpdateSuccessful = true;

        LastFeedFile = null;
        ImageFile = null;

        unreadItemsNumber = SubsItemList.size();
    }



    final void deleteItem(int position)
    {
        synchronized (SubsItemList)
        {
            if ( !SubsItemList.get(position).isRead() )
            {
                unreadItemsNumber--;
            }
            SubsItemList.remove(position);
        }
    }


    final void makeItemStarred(int position)
    {
        SubsItemList.get(position).makeStarred();
    }


    final void makeItemNotStarred(int position)
    {
        SubsItemList.get(position).makeNotStarred();
    }


    final void hideItem(int position)
    {
        synchronized (SubsItemList)
        {
            if (!SubsItemList.get(position).isRead())
            {
                unreadItemsNumber--;
            }
            SubsItemList.get(position).makeHidden();
        }
    }


    private void addItem(final Item item)
    {
        synchronized (SubsItemList)
        {
            final ListIterator<SubsItem> existingSubsItemIt = SubsItemList.listIterator();
            SubsItem existingSubsItem;

            while (existingSubsItemIt.hasNext())
            {
                existingSubsItem = existingSubsItemIt.next();

                if (item.equals(existingSubsItem))
                {
                    existingSubsItem.makeReObservedAtLastUpdate();
                    return;
                }

                existingSubsItem.makeCheckedAtLastUpdate();
            }
            SubsItemList.add(0, new SubsItem(item));
            unreadItemsNumber++;
        }
    }


    private void makeUncheckedItemsNotReobserved()
    {
        synchronized (SubsItemList)
        {
            for (SubsItem item : SubsItemList)
            {
                if ( !item.isCheckedAtLastUpdate() )
                {
                    item.makeNotReObservedAtLastUpdate();
                }
                else
                {
                    item.makeNotCheckedAtLastUpdate();
                }
            }
        }
    }


    private void addItems (final Channel parsedChannel)
    {
        for (Item item : parsedChannel.getItemList())
        {
            addItem(item);
        }

        makeUncheckedItemsNotReobserved();
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

        this.LastUpdateDate = Calendar.getInstance();
        addItems(parsedChannel);

        return titleChanged;
    }



    final void hiddenItemsRemoval()
    {
        if (ItemStorageTimeInMilliS == -1) { return; }


        synchronized (SubsItemList)
        {
            Calendar now = Calendar.getInstance();
            long itemStorageTime;

            final ListIterator<SubsItem> subsItemIt = SubsItemList.listIterator();
            SubsItem subsItem;
            while (subsItemIt.hasNext())
            {
                subsItem = subsItemIt.next();

                if (subsItem.isStarred())
                {
                    continue;
                }

                //If News Item was reobserved in last update or channel had not been updated after the News Item was reobserved.
                if (subsItem.isReObservedAtLastUpdate() || LastUpdateDate.getTimeInMillis() <= subsItem.getRecievedDate().getTimeInMillis()  )
                {
                    continue;
                }

                if ( (now.getTimeInMillis() - subsItem.getRecievedDate().getTimeInMillis()) > ItemStorageTimeInMilliS)
                {
                    subsItemIt.remove();
                }
            }
        }
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

    final void makeConfirmed()
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

    void setLastUpdateSuccessful(boolean status)
    {
        lastUpdateSuccessful = status;
    }

    public final boolean isLastUpdateSuccessful()
    {
        return lastUpdateSuccessful;
    }

    public final Integer getUnreadItemsNumber()
    {
        return unreadItemsNumber;
    }

    final void updateTempFile(final File newTempFile)
    {
        if (LastFeedFile != null)
        {
            LastFeedFile.delete();
        }
        LastFeedFile = newTempFile;
    }
}
