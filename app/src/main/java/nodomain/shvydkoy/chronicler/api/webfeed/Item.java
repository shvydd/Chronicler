package nodomain.shvydkoy.chronicler.api.webfeed;

import android.support.annotation.NonNull;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import nodomain.shvydkoy.chronicler.api.utils.StringUtil;
import nodomain.shvydkoy.chronicler.api.utils.URLUtil;



//http://www.rssboard.org/rss-specification#ltttlgtSubelementOfLtchannelgt
public class Item
{
    final private static String EXCEPTION_DESCRIPTION_IF_ITEM_INVALID = "The Item does not contain valid title or link.";

    private String Title;
    private URL Link;
    private String Description;

    private String Author;
    private String Category;
    private String Enclosure;
    private String Guid;
    private String PubDate;
    private String Source;

    private String Content;



    Item(final String title, final String link, final String description, final String author, final String category,
         final String enclosure, final String guid, final String pubDate, final String source, final String content) throws IllegalArgumentException
    {
        if (StringUtil.isBlank(title))
        {
            Log.d("Item constructor", "Item title is blank: title={" + title + "}");
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
        }


        URL URLLink;
        try
        {
            URLLink = new URL(link);
            if (!URLUtil.isHttpOrHttpsURL(URLLink))
            {
                Log.d("Item constructor", "Item link is not http(s): link={" + link + "}");
                throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
            }
        }
        catch (MalformedURLException e)
        {
            Log.d("Item constructor", "Item link is not valid URL: link={" + link + "}");
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
        }

        this.Title = title;
        this.Link = URLLink;
        this.Description = description;
        this.Author = author;
        this.Category = category;
        this.Enclosure = enclosure;
        this.Guid = guid;
        this.PubDate = pubDate;
        this.Source = source;
        this.Content = content;
    }


    protected Item(final Item parsedItem)
    {
        this.Title = parsedItem.Title;
        this.Link = parsedItem.Link;
        this.Description = parsedItem.Description;
        this.Author = parsedItem.Author;
        this.Category = parsedItem.Category;
        this.Enclosure = parsedItem.Enclosure;
        this.Guid = parsedItem.Guid;
        this.PubDate = parsedItem.PubDate;
        this.Source = parsedItem.Source;
        this.Content = parsedItem.Content;
    }


    final public String getTitle()
    {
        return Title;
    }

    final public void setTitle(final String Title)
    {
        this.Title = Title;
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
                throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
            }
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
        }

        this.Link = URLLink;
    }

    final public String getDescription()
    {
        return Description;
    }

    final public void setDescription(final String Description)
    {
        this.Description = Description;
    }

    final public String getAuthor()
    {
        return Author;
    }

    final public void setAuthor(final String Author)
    {
        this.Author = Author;
    }

    final public String getCategory()
    {
        return Category;
    }

    final public void setCategory(final String Category)
    {
        this.Category = Category;
    }

    final public String getEnclosure()
    {
        return Enclosure;
    }

    final public void setEnclosure(final String Enclosure)
    {
        this.Enclosure = Enclosure;
    }

    final public String getGuid()
    {
        return Guid;
    }

    final public void setGuid(final String Guid)
    {
        this.Guid = Guid;
    }

    final public String getPubDate()
    {
        return PubDate;
    }

    final public void setPubDate(final String PubDate)
    {
        this.PubDate = PubDate;
    }

    final public String getSource()
    {
        return Source;
    }

    final public void setSource(final String Source)
    {
        this.Source = Source;
    }

    final public String getContent()
    {
        return Content;
    }

    final public void setContent(String content)
    {
        Content = content;
    }

    @Override
    @NonNull
    public String toString()
    {
        return "Item:" +
                "\n\tTitle: " + Title +
                "\n\tLink: " + Link +
                "\n\tDescription: " + Description +
                "\n\tAuthor: " + Author +
                "\n\tCategory: " + Category +
                "\n\tEnclosure: " + Enclosure +
                "\n\tGuid: " + Guid +
                "\n\tPubDate: " + PubDate +
                "\n\tSource: " + Source +
                "\n\tContent: " + Content +
            "\nEnd of the Item.";
    }


    final public boolean equals(final Item parsedItem)
    {
        if (this == parsedItem)
        {
            return true;
        }

        if (null != this.Title)
        {
            if (!this.Title.equals(parsedItem.Title))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Title)
                return false;
        }

        if (null != this.Link)
        {
            if (!this.Link.equals(parsedItem.Link))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Link)
                return false;
        }

        if (null != this.Description)
        {
            if (!this.Description.equals(parsedItem.Description))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Description)
                return false;
        }

        if (null != this.Author)
        {
            if (!this.Author.equals(parsedItem.Author))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Author)
                return false;
        }

        if (null != this.Category)
        {
            if (!this.Category.equals(parsedItem.Category))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Category)
                return false;
        }

        if (null != this.Enclosure)
        {
            if (!this.Enclosure.equals(parsedItem.Enclosure))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Enclosure)
                return false;
        }

        if (null != this.Guid)
        {
            if (!this.Guid.equals(parsedItem.Guid))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Guid)
                return false;
        }

        if (null != this.PubDate)
        {
            if (!this.PubDate.equals(parsedItem.PubDate))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.PubDate)
                return false;
        }

        if (null != this.Source)
        {
            if (!this.Source.equals(parsedItem.Source))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Source)
                return false;
        }


        if (null != this.Content)
        {
            if (!this.Content.equals(parsedItem.Content))
            {
                return false;
            }
        }
        else
        {
            if (null != parsedItem.Content)
                return false;
        }

            return true;
    }


    final public String hashString()
    {
        return Title + Link + PubDate;
    }

}
