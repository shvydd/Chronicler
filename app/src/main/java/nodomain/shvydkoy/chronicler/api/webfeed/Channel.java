package nodomain.shvydkoy.chronicler.api.webfeed;

import android.support.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import nodomain.shvydkoy.chronicler.api.utils.StringUtil;
import nodomain.shvydkoy.chronicler.api.utils.URLUtil;



//http://www.rssboard.org/rss-specification#ltttlgtSubelementOfLtchannelgt
public class Channel
{
    protected String Title;
    protected URL Link;
    protected String Description;

    protected ArrayList<Item> ItemList;

    protected String Language;
    protected String Copyright;

    protected String ManagingEditor;
    protected String WebMaster;

    protected String PubDate;
    protected String LastBuildDate;

    protected String Category;

    protected String Cloud;

    protected String TTL;

    protected String Image;

    protected String SkipHours;

    protected String SkipDays;

    final static private String EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID = "Channel instantiation with invalid title or link";




    Channel(final String title, final String link, final String description, final String
            language, final String copyright, final String managingEditor, final String webMaster, final String pubDate,
            final String lastBuildDate, final String category, final String cloud, final String TTL, final String image,
            final String skipHours, final String skipDays, final ArrayList<Item> ItemList) throws IllegalArgumentException
    {
        if (StringUtil.isBlank(title))
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
        }

        URL URLLink;
        try
        {
            URLLink = new URL(link);
            if (!URLUtil.isHttpOrHttpsURL(URLLink))
            {
                throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
            }
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
        }


        Title = title;
        Link = URLLink;
        Description = description;

        Language = language;
        Copyright = copyright;
        ManagingEditor = managingEditor;
        WebMaster = webMaster;
        PubDate = pubDate;
        LastBuildDate = lastBuildDate;
        Category = category;
        Cloud = cloud;
        this.TTL = TTL;
        Image = image;
        SkipHours = skipHours;
        SkipDays = skipDays;

        this.ItemList = ItemList;
    }


    protected Channel(final Channel parsedChannel)
    {
        this.ItemList = parsedChannel.ItemList;

        this.Title = parsedChannel.Title;
        this.Link = parsedChannel.Link;

        this.Description = parsedChannel.Description;
        this.Language = parsedChannel.Language;
        this.Copyright = parsedChannel.Copyright;
        this.ManagingEditor = parsedChannel.ManagingEditor;
        this.WebMaster = parsedChannel.WebMaster;
        this.PubDate = parsedChannel.PubDate;
        this.LastBuildDate = parsedChannel.LastBuildDate;
        this.Category = parsedChannel.Category;
        this.Cloud = parsedChannel.Cloud;
        this.TTL = parsedChannel.TTL;
        this.Image = parsedChannel.Image;
        this.SkipHours = parsedChannel.SkipHours;
        this.SkipDays = parsedChannel.SkipDays;

    }

    protected Channel(final Channel parsedChannel,  boolean copyItemList)
    {
        if (copyItemList)
        {
            this.ItemList = parsedChannel.ItemList;
        }
        else
        {
            this.ItemList = null;
        }

        this.Title = parsedChannel.Title;
        this.Link = parsedChannel.Link;

        this.Description = parsedChannel.Description;
        this.Language = parsedChannel.Language;
        this.Copyright = parsedChannel.Copyright;
        this.ManagingEditor = parsedChannel.ManagingEditor;
        this.WebMaster = parsedChannel.WebMaster;
        this.PubDate = parsedChannel.PubDate;
        this.LastBuildDate = parsedChannel.LastBuildDate;
        this.Category = parsedChannel.Category;
        this.Cloud = parsedChannel.Cloud;
        this.TTL = parsedChannel.TTL;
        this.Image = parsedChannel.Image;
        this.SkipHours = parsedChannel.SkipHours;
        this.SkipDays = parsedChannel.SkipDays;

    }


    final public String getTitle()
    {
        return Title;
    }

    final public void setTitle(final String title)
    {
        if (StringUtil.isBlank(title))
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
        }
    }

    final public URL getLink()
    {
        return Link;
    }

    final public void setLink(final String link) throws IllegalArgumentException
    {
        URL URLLink;
        try
        {
            URLLink = new URL(link);
            if (!URLUtil.isHttpOrHttpsURL(URLLink))
            {
                throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
            }
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_REQUIRED_CHANNEL_FIELD_INVALID);
        }
    }

    final public String getDescription()
    {
        return Description;
    }

    final public void setDescription(final String Description)
    {
        this.Description = Description;
    }

    final public ArrayList<Item> getItemList()
    {
        return ItemList;
    }

    final public void setItemList(final ArrayList<Item> ItemList)
    {
        this.ItemList = ItemList;
    }

    final public String getLanguage()
    {
        return Language;
    }

    final public void setLanguage(final String Language)
    {
        this.Language = Language;
    }

    final public String getCopyright()
    {
        return Copyright;
    }

    final public void setCopyright(final String Copyright)
    {
        this.Copyright = Copyright;
    }

    final public String getManagingEditor()
    {
        return ManagingEditor;
    }

    final public void setManagingEditor(final String ManagingEditor)
    {
        this.ManagingEditor = ManagingEditor;
    }

    final public String getWebMaster()
    {
        return WebMaster;
    }

    final public void setWebMaster(final String WebMaster)
    {
        this.WebMaster = WebMaster;
    }

    final public String getPubDate()
    {
        return PubDate;
    }

    final public void setPubDate(final String PubDate)
    {
        this.PubDate = PubDate;
    }

    final public String getLastBuildDate()
    {
        return LastBuildDate;
    }

    final public void setLastBuildDate(final String LastBuildDate)
    {
        this.LastBuildDate = LastBuildDate;
    }

    final public String getCategory()
    {
        return Category;
    }

    final public void setCategory(final String Category)
    {
        this.Category = Category;
    }

    final public String getCloud()
    {
        return Cloud;
    }

    final public void setCloud(final String Cloud)
    {
        this.Cloud = Cloud;
    }

    final public String getTTL()
    {
        return TTL;
    }

    final public void setTTL(final String TTL)
    {
        this.TTL = TTL;
    }

    final public String getImage()
    {
        return Image;
    }

    final public void setImage(final String Image)
    {
        this.Image = Image;
    }

    final public String getSkipHours()
    {
        return SkipHours;
    }

    final public void setSkipHours(final String SkipHours)
    {
        this.SkipHours = SkipHours;
    }

    final public String getSkipDays()
    {
        return SkipDays;
    }

    final public void setSkipDays(final String SkipDays)
    {
        this.SkipDays = SkipDays;
    }



    @Override
    @NonNull
    public String toString()
    {
        return "Channel:" +
                "\n\tTitle: " + Title +
                "\n\tLink: " + Link +
                "\n\tDescription: " + Description +
                "\n\tLanguage: " + Language +
                "\n\tCopyright: " + Copyright +
                "\n\tManagingEditor: " + ManagingEditor +
                "\n\tWebMaster: " + WebMaster +
                "\n\tPubDate: " + PubDate +
                "\n\tLastBuildDate: " + LastBuildDate +
                "\n\tCategory: " + Category +
                "\n\tCloud: " + Cloud +
                "\n\tTTL: " + TTL +
                "\n\tImage: " + Image +
                "\n\tSkipHours: " + SkipHours +
                "\n\tSkipDays: " + SkipDays +
                "\n\tNumber of Items: " + ItemList.size() +
           "\nEnd of Channel";
    }


    final public String hashString()
    {
        return Link.toString();
    }

}
