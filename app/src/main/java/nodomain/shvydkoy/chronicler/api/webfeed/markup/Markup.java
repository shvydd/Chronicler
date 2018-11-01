package nodomain.shvydkoy.chronicler.api.webfeed.markup;

//Should Successors be singletons ?
public class Markup
{
    protected String MarkupName;
    protected Tag Root;
    protected Tag Channel;
    protected Tag Title;
    protected Tag Link;
    protected Tag Description;
    protected Tag Language;
    protected Tag Copyright;
    protected Tag ManagingEditor;
    protected Tag WebMaster;
    protected Tag PubDate;
    protected Tag LastBuildDate;
    protected Tag Category;
    protected Tag Cloud;
    protected Tag TTL;
    protected Tag Image;
    protected Tag SkipHours;
    protected Tag SkipDays;
    protected Tag Item;
    protected Tag ItemTitle;
    protected Tag ItemLink;
    protected Tag ItemDescription;
    protected Tag ItemAuthor;
    protected Tag ItemCategory;
    protected Tag ItemEnclosure;
    protected Tag ItemGuid;
    protected Tag ItemPubDate;
    protected Tag ItemSource;
    protected Tag ItemContent;

    protected Markup() {}

    final public String getMarkupName()
    {
        return MarkupName;
    }

    final public Tag getRoot()
    {
        return Root;
    }

    final public Tag getChannel()
    {
        return Channel;
    }

    final public Tag getTitle()
    {
        return Title;
    }

    final public Tag getLink()
    {
        return Link;
    }

    final public Tag getDescription()
    {
        return Description;
    }

    final public Tag getLanguage()
    {
        return Language;
    }

    final public Tag getCopyright()
    {
        return Copyright;
    }

    final public Tag getManagingEditor()
    {
        return ManagingEditor;
    }

    final public Tag getWebMaster()
    {
        return WebMaster;
    }

    final public Tag getPubDate()
    {
        return PubDate;
    }

    final public Tag getLastBuildDate()
    {
        return LastBuildDate;
    }

    final public Tag getCategory()
    {
        return Category;
    }

    final public Tag getCloud()
    {
        return Cloud;
    }

    final public Tag getTTL()
    {
        return TTL;
    }

    final public Tag getImage()
    {
        return Image;
    }

    final public Tag getSkipHours()
    {
        return SkipHours;
    }

    final public Tag getSkipDays()
    {
        return SkipDays;
    }

    final public Tag getItem()
    {
        return Item;
    }

    final public Tag getItemTitle()
    {
        return ItemTitle;
    }

    final public Tag getItemLink()
    {
        return ItemLink;
    }

    final public Tag getItemDescription()
    {
        return ItemDescription;
    }

    final public Tag getItemAuthor()
    {
        return ItemAuthor;
    }

    final public Tag getItemCategory()
    {
        return ItemCategory;
    }

    final public Tag getItemEnclosure()
    {
        return ItemEnclosure;
    }

    final public Tag getItemGuid()
    {
        return ItemGuid;
    }

    final public Tag getItemPubDate()
    {
        return ItemPubDate;
    }

    final public Tag getItemSource()
    {
        return ItemSource;
    }

    public Tag getItemContent()
    {
        return ItemContent;
    }

    final protected void setFieldNames()
    {
        if (null != Root) Root.setFieldName("Root");
        if (null != Channel) Channel.setFieldName("Channel");
        if (null != Title) Title.setFieldName("Title");
        if (null != Link) Link.setFieldName("Link");
        if (null != Description) Description.setFieldName("Description");
        if (null != Language) Language.setFieldName("Language");
        if (null != Copyright) Copyright.setFieldName("Copyright");
        if (null != ManagingEditor) ManagingEditor.setFieldName("ManagingEditor");
        if (null != WebMaster) WebMaster.setFieldName("WebMaster");
        if (null != PubDate) PubDate.setFieldName("PubDate");
        if (null != LastBuildDate) LastBuildDate.setFieldName("LastBuildDate");
        if (null != Category) Category.setFieldName("Category");
        if (null != Cloud) Cloud.setFieldName("Cloud");
        if (null != TTL) TTL.setFieldName("TTL");
        if (null != Image) Image.setFieldName("Image");
        if (null != SkipHours) SkipHours.setFieldName("SkipHours");
        if (null != SkipDays) SkipDays.setFieldName("SkipDays");
        if (null != Item) Item.setFieldName("Item");
        if (null != ItemTitle) ItemTitle.setFieldName("ItemTitle");
        if (null != ItemLink) ItemLink.setFieldName("ItemLink");
        if (null != ItemDescription) ItemDescription.setFieldName("ItemDescription");
        if (null != ItemAuthor) ItemAuthor.setFieldName("ItemAuthor");
        if (null != ItemCategory) ItemCategory.setFieldName("ItemCategory");
        if (null != ItemEnclosure) ItemEnclosure.setFieldName("ItemEnclosure");
        if (null != ItemGuid) ItemGuid.setFieldName("ItemGuid");
        if (null != ItemPubDate) ItemPubDate.setFieldName("ItemPubDate");
        if (null != ItemSource) ItemSource.setFieldName("ItemSource");
        if (null != ItemContent) ItemContent.setFieldName("ItemContent");
    }

    /*final String getFieldNameUsingTagName(String tagName)
    {
        if (null != Root)
        {
            if (tagName.equals(Root.getName()))
            return Root.getFieldName();
        }
        else if (null != Channel)
        {
            if (tagName.equals(Channel.getName()))
                return Channel.getFieldName();
        }
        else if (null != Title)
        {
            if (tagName.equals(Title.getName()))
                return Title.getFieldName();
        }
        else if (null != Link)
        {
            if (tagName.equals(Link.getName()))
                return Link.getFieldName();
        }
        else if (null != Description)
        {
            if (tagName.equals(Description.getName()))
                return Description.getFieldName();
        }
        else if (null != Language)
        {
            if (tagName.equals(Language.getName()))
                return Language.getFieldName();
        }
        else if (null != Copyright)
        {
            if (tagName.equals(Copyright.getName()))
                return Copyright.getFieldName();
        }
        else if (null != ManagingEditor)
        {
            if (tagName.equals(ManagingEditor.getName()))
                return ManagingEditor.getFieldName();
        }
        else if (null != WebMaster)
        {
            if (tagName.equals(WebMaster.getName()))
                return WebMaster.getFieldName();
        }
        else if (null != PubDate)
        {
            if (tagName.equals(PubDate.getName()))
                return PubDate.getFieldName();
        }
        else if (null != LastBuildDate)
        {
            if (tagName.equals(LastBuildDate.getName()))
                return LastBuildDate.getFieldName();
        }
        else if (null != Category)
        {
            if (tagName.equals(Category.getName()))
                return Category.getFieldName();
        }
        else if (null != Cloud)
        {
            if (tagName.equals(Cloud.getName()))
                return Cloud.getFieldName();
        }
        else if (null != TTL)
        {
            if (tagName.equals(TTL.getName()))
                return TTL.getFieldName();
        }
        else if (null != Image)
        {
            if (tagName.equals(Image.getName()))
                return Image.getFieldName();
        }
        else if (null != SkipHours)
        {
            if (tagName.equals(SkipHours.getName()))
                return SkipHours.getFieldName();
        }
        else if (null != SkipDays)
        {
            if (tagName.equals(SkipDays.getName()))
                return SkipDays.getFieldName();
        }
        else if (null != Item)
        {
            if (tagName.equals(Item.getName()))
                return Item.getFieldName();
        }
        else if (null != ItemTitle)
        {
            if (tagName.equals(ItemTitle.getName()))
                return ItemTitle.getFieldName();
        }
        else if (null != ItemLink)
        {
            if (tagName.equals(ItemLink.getName()))
                return ItemLink.getFieldName();
        }
        else if (null != ItemDescription)
        {
            if (tagName.equals(ItemDescription.getName()))
                return ItemDescription.getFieldName();
        }
        else if (null != ItemAuthor)
        {
            if (tagName.equals(ItemAuthor.getName()))
                return ItemAuthor.getFieldName();
        }
        else if (null != ItemCategory)
        {
            if (tagName.equals(ItemCategory.getName()))
                return ItemCategory.getFieldName();
        }
        else if (null != ItemEnclosure)
        {
            if (tagName.equals(ItemEnclosure.getName()))
                return ItemEnclosure.getFieldName();
        }
        else if (null != ItemGuid)
        {
            if (tagName.equals(ItemGuid.getName()))
                return ItemGuid.getFieldName();
        }
        else if (null != ItemPubDate)
        {
            if (tagName.equals(ItemPubDate.getName()))
                return ItemPubDate.getFieldName();
        }
        else if (null != ItemSource)
        {
            if (tagName.equals(ItemSource.getName()))
                return ItemSource.getFieldName();
        }
        else if (null != ItemContent)
        {
            if (tagName.equals(ItemContent.getName()))
                return ItemContent.getFieldName();
        }

        return null;
    }*/
}
