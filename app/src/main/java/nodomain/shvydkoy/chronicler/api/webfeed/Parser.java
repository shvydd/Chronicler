//TODO Refactor this mash!
/*
 1) Replace declarations like "String TitleFieldName = "Title"
                              String LinkFieldName = "Link"
                                         ...                 "
 with HashMap<String, String>.

 2) Reuse the code of Channel header parsing in Item Parsing.

 3) Try to employ tree in markup, or at least employ tree-borrowed algorithm of matching markup tags with document tag.

 4) Try to add to the Tag class add a lambda, converting Tag String field filled by the Parser with one formatted in general form.
    For example: time-date strings in RSS2.0, RSS"1.0" and ATOM are different. And they should be brought to a common
    form, recognizable with Channel and Item constructors. Oh yes, change date-time Channel/Item fields type to appropriate one.

 5) Bring all the comments to common style (replace russian comments in the Parser).

 6) Creare AttrbuteTag extended Tag


 *
 */
package nodomain.shvydkoy.chronicler.api.webfeed;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import nodomain.shvydkoy.chronicler.api.webfeed.markup.BankOfMarkups;
import nodomain.shvydkoy.chronicler.api.webfeed.markup.Markup;
import nodomain.shvydkoy.chronicler.api.webfeed.markup.Tag;

import static nodomain.shvydkoy.chronicler.api.utils.URLUtil.isValidHttpOrHttpsURL;



public final class Parser
{



    final private static Markup[] KnownMarkups = BankOfMarkups.getMarkups();
    final private static boolean XML_PULL_PARSER_STATE = false;
    final private static String NAMESPACE = null;
    final private static String EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP = "Invalid newsfeed file markup.";
    final private static String EXCEPTION_DESCRIPTION_IF_UNKNOWN_MARKUP = "Newsfeed file has unknown markup.";
    final private static String EXCEPTION_DESCRIPTION_IF_ITEM_INVALID = "The Item do not contain valid title or link.";


    final static private String TitleFieldName = "Title";
    final static private String LinkFieldName = "Link";
    final static private String DescriptionFieldName = "Description";
    final static private String LanguageFieldName = "Language";
    final static private String CopyrightFieldName = "Copyright";
    final static private String ManagingEditorFieldName = "ManagingEditor";
    final static private String WebMasterFieldName = "WebMaster";
    final static private String PubDateFieldName = "PubDate";
    final static private String LastBuildDateFieldName = "LastBuildDate";
    final static private String CategoryFieldName = "Category";
    final static private String CloudFieldName = "Cloud";
    final static private String TTLFieldName = "TTL";
    final static private String ImageFieldName = "Image";
    final static private String SkipHoursFieldName = "SkipHours";
    final static private String SkipDaysFieldName = "SkipDays";

    final static private String ItemTitleFieldName = "ItemTitle";
    final static private String ItemLinkFieldName = "ItemLink";
    final static private String ItemDescriptionFieldName = "ItemDescription";
    final static private String ItemAuthorFieldName = "ItemAuthor";
    final static private String ItemCategoryFieldName = "ItemCategory";
    final static private String ItemEnclosureFieldName = "ItemEnclosure";
    final static private String ItemGuidFieldName = "ItemGuid";
    final static private String ItemPubDateFieldName = "ItemPubDate";
    final static private String ItemSourceFieldName = "ItemSource";
    final static private String ItemContentFieldName = "ItemContent";


    private String channelTitle = null;
    private String channelLink = null;
    private String channelDescription = null;
    private String channelLanguage = null;
    private String channelCopyright = null;
    private String channelManagingEditor = null;
    private String channelWebMaster = null;
    private String channelPubDate = null;
    private String channelLastBuildDate = null;
    private String channelCategory = null;
    private String channelCloud = null;
    private String channelTTL = null;
    private String channelImage = null;
    private String channelSkipHours = null;
    private String channelSkipDays = null;

    private String itemTitle;
    private String itemLink;
    private String itemDescription;
    private String itemAuthor;
    private String itemCategory;
    private String itemEnclosure;
    private String itemGuid;
    private String itemPubDate;
    private String itemSource;
    private String itemContent;


    private String INPUTENCODING = null;

    private XmlPullParser parser;
    private Markup markup = null;
    private Channel channel = null;
    private ArrayList<Item> itemList = new ArrayList<>();
    private String knownChannelURL;

    private String documentTagName = null;
    private int documentTagDepth = 0;
    private int documentEventType = 0;


    public Parser() throws XmlPullParserException
    {
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, XML_PULL_PARSER_STATE);
    }


    public final Channel parse(InputStream channelFileStream, String knownChannelURL) throws XmlPullParserException, IOException, ParsingFailedException
    {
        parser.setInput(channelFileStream, INPUTENCODING); // If inputEncoding is null, parser determine it on his own
        this.knownChannelURL = knownChannelURL;
        Log.d("parse(InputStream)", "Parser has proceeded to fillChannel()");
        return fillChannel();
    }


    private Channel fillChannel() throws XmlPullParserException, IOException, ParsingFailedException
    {
        Log.d("fillChannel()", "Started");

        determineMarkup();
        //Parser stays at root START_TAG

        moveParserToChannelTag();
        //Parser stays at channel START_TAG


        Tag markupChannelTag = markup.getChannel();


        documentTagName = parser.getName();
        documentTagDepth = parser.getDepth();
        documentEventType = parser.getEventType();


        //Matching in document channel attributes with markup fields established as attributes
        Tag markupChannelTagAttributeTag;
        String markupChannelTagAttributeTagFieldName;
        String markupChannelTagAttributeTagKey;
        String documentChannelTagAttributeTagValue;

        for (int i = 0; i < markupChannelTag.getAttributes().size(); i++)
        {
            markupChannelTagAttributeTag = markupChannelTag.getAttributes().get(i);
            markupChannelTagAttributeTagFieldName = markupChannelTagAttributeTag.getFieldName();
            markupChannelTagAttributeTagKey = markupChannelTagAttributeTag.getName();
            documentChannelTagAttributeTagValue = parser.getAttributeValue(NAMESPACE, markupChannelTagAttributeTagKey);

            //Если аттрибут описывает обособленное поле класса Channel
            if (null != markupChannelTagAttributeTagFieldName)
            {
                fillTempChannelField(markupChannelTagAttributeTagFieldName, documentChannelTagAttributeTagValue, markupChannelTagAttributeTag.isMultiple());
            }
            /* Если аттрибут не описывает обособленное поле класса Channel, а является частью значения,
             * которое должно быть присвоено полю Channel класса Channel ( полю, которого нет ) - не делать ничего.
             */
        }

        parser.nextTag();

        //Искать совпадения        определенных в разметке дочерних каналу тэгов     с текущим тэгом в файле
        throughDocEventsLoop:
        while (documentEventType != XmlPullParser.END_DOCUMENT && documentTagDepth >= markupChannelTag.getDepth())
        {
            documentTagName = parser.getName();
            documentTagDepth = parser.getDepth();
            documentEventType = parser.getEventType();

            Log.d("fillChannel()", "Current document tag: name " + documentTagName + ", depth " + documentTagDepth + ", type " + getEventTypeName(documentEventType));


            //Finish parsing after first channel END_TAG has been observed.
            if (documentEventType == XmlPullParser.END_TAG && markupChannelTag.getName().equals(documentTagName) && documentTagDepth == markupChannelTag.getDepth())
            {
                Log.d("fillChannel()", "Channel END_TAG has been observed");
                return assemblyChannel();
            }
            else if (documentEventType != XmlPullParser.START_TAG)
            {
                parser.nextTag();
                continue;
            }


            /*Заполнить Item, если текущий тэг в документе соответствует Item
             */

            if (documentTagDepth == markup.getItem().getDepth() && markup.getItem().getName().equals(documentTagName))
            {
                Log.d("fillChannel()", "Current document tag is Item tag, according to determined markup");

                try
                {
                    fillItem();
                }
                catch (ParsingNotifyingException e)
                {
                    Log.d("fillChannel()", "Invalid markup inside an Item");
                }
                catch (IOException e)
                {
                    Log.d("fillChannel()", "IOException (Has InputStream thrown?)");
                    throw e;
                }

                parser.nextTag();
                continue throughDocEventsLoop;
            }


            /*
             * Заполнить заголовчные поля канала.
             */
            Tag markupNestedInChannelTag;
            String markupNestedInChannelTagFieldName;
            boolean markupNestedInChannelTagIsMuliple;


            //StringBuilder documentNestedInChannelTagText;


            for (int i = 0; i < markupChannelTag.getNested().size(); i++)
            {
                markupNestedInChannelTag = markupChannelTag.getNested().get(i);
                markupNestedInChannelTagFieldName = markupNestedInChannelTag.getFieldName();
                markupNestedInChannelTagIsMuliple = markupNestedInChannelTag.isMultiple();


                /* Заполнить поля класса Channnel, для которых тэги в разметке не содержат дочерних тэгов.
                 * Если дочерний каналу тэг разметки имеет аттрибуты, добавить их значения в поле класса Channel, соотвествующее тэгу.
                 */
                if (markupNestedInChannelTag.getNested().size() == 0 && documentTagDepth == markupNestedInChannelTag.getDepth() && markupNestedInChannelTag.getName().equals(documentTagName))
                {
                    Log.d("fillChannel()", "Matching document tag with markup tags nested in Channel tag. Through nested tags which HAVE NOT nested tags");
                    Log.d("fillChannel()", "The nested in Channel tag has attributes defined in markup (amount): " + markupNestedInChannelTag.getAttributes().size());

                    //Если дочерний каналу тэг разметки имеет аттрибуты, добавить их значения в поле класса Channel, соотвествующее тэгу.
                    StringBuilder documentNestedInChannelTagAttributeTagValue = new StringBuilder();

                    for (int j = 0; j < markupNestedInChannelTag.getAttributes().size(); j++)
                    {

                        Tag    markupNestedInChannelTagAttributeTag =    markupNestedInChannelTag.getAttributes().get(j);
                        String markupNestedInChannelTagAttributeTagKey = markupNestedInChannelTagAttributeTag.getName();

                        Log.d("fillChannel", "markupNestedInChannelTagAttributeTagKey " + markupNestedInChannelTagAttributeTagKey );

                        String documentNestedInChannelTagAttributeTagTempValue = parser.getAttributeValue(NAMESPACE, markupNestedInChannelTagAttributeTagKey);

                        //TODO Define in Tag class field "If Tag has attributes, its a separator between them" and "Add attribute key to the value of Item filed corresponding to nested in Item tag

                        Log.d("fillChannel", "documentNestedInChannelTagAttributeTagTempValue " + documentNestedInChannelTagAttributeTagTempValue );

                        if (documentNestedInChannelTagAttributeTagValue.length() != 0)
                        {
                            documentNestedInChannelTagAttributeTagValue.append(" ").append(documentNestedInChannelTagAttributeTagTempValue);
                        }
                        else
                        {
                            documentNestedInChannelTagAttributeTagValue.append(documentNestedInChannelTagAttributeTagTempValue);
                        }

                    }

                    Log.d("fillChannel", "documentNestedInChannelTagAttributeTagValue " + documentNestedInChannelTagAttributeTagValue );

                    //Прочитать текст тэга, вложенного в Channel
                    String documentNestedInChannelTagText;
                    try
                    {
                        //nextText() остановится на теге, закрывающем вложенный в Channel тэг, или выдаст XmlPullParserException. Если XmlPullParserException, бросить ParsingFailedException
                        //Т.е. у таких вложенных в Channel тэгов, в которых нет своих вложенных тэгов, вложенных тэгов быть не может. CAPTN
                        documentNestedInChannelTagText = parser.nextText();

                        Log.d("fillChannel", "documentNestedInChannelTagText " + documentNestedInChannelTagText );

                        //Если тэг содержал текст и аттрибуты - предпочесть текст, аттрибуты отбросить
                        if (documentNestedInChannelTagText != null && documentNestedInChannelTagText.length() != 0)
                        {
                            fillTempChannelField(markupNestedInChannelTagFieldName, documentNestedInChannelTagText, markupNestedInChannelTagIsMuliple);
                        }
                        else if (documentNestedInChannelTagAttributeTagValue.length() != 0)
                        {
                            fillTempChannelField(markupNestedInChannelTagFieldName, documentNestedInChannelTagAttributeTagValue.toString(), markupNestedInChannelTagIsMuliple);
                        }


                        /*Если имя закрывающего тэга != имени открывающего - бросить исключение ParsingFailedException
                         * так как парсится Item.
                         */
                        if ( !markupNestedInChannelTag.getName().equals(parser.getName()) )
                        {
                            Log.d("fillChannel()", "Nested in Channel START_TAG and its END_TAG have different names");
                            throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                        }
                    }
                    catch (XmlPullParserException e)
                    {
                        Log.d("fillChannel()", "Nested in Channel START_TAG has no END_TAG");
                        throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                    }


                    //Перевожу парсер на следующий START_TAG
                    parser.nextTag();
                    continue throughDocEventsLoop;
                }


                /* Заполнить поля класса Channnel, для которых тэги в разметке содержат дочерние тэги.
                 * Если тэг имеет вложенные тэги, он не нимеет текста?
                 */
                if (markupNestedInChannelTag.getNested().size() > 0 && documentTagDepth == markupNestedInChannelTag.getDepth() && markupNestedInChannelTag.getName().equals(documentTagName))
                {
                    Log.d("fillChannel()", "Matching document tag with markup tags nested in Channel tag. Through nested tags which HAVE nested tags");
                    Log.d("fillChannel()", "The nested in Channel tag has nested (amount): " + markupNestedInChannelTag.getNested().size());


                    StringBuilder documentNestedInChannelTagText = new StringBuilder();
                    boolean documentNestedInChannel_END_TAG = false;


                    while (documentEventType != XmlPullParser.END_DOCUMENT && documentTagDepth >= markupNestedInChannelTag.getDepth())
                    {
                        documentTagName = parser.getName();
                        documentTagDepth = parser.getDepth();
                        documentEventType = parser.getEventType();

                        if (documentTagDepth == markupNestedInChannelTag.getDepth() && markupNestedInChannelTag.getName().equals(documentTagName) && documentEventType == XmlPullParser.END_TAG)
                        {
                            Log.d("fillChannel()", "END_TAG of nested in Channel tag");
                            documentNestedInChannel_END_TAG = true;
                            break;
                        }

                        throughNestedinTestedLoop: for (int j = 0; j < markupNestedInChannelTag.getNested().size(); j++)
                        {
                            Tag markupDoubleNestedInChannelTag = markupNestedInChannelTag.getNested().get(j);
                            String markupDoubleNestedInChannelTagName = markupDoubleNestedInChannelTag.getName();

                            documentTagName = parser.getName();
                            documentTagDepth = parser.getDepth();
                            documentEventType = parser.getEventType();


                            if (markupDoubleNestedInChannelTagName.equals(documentTagName) && documentTagDepth == markupDoubleNestedInChannelTag.getDepth())
                            {
                                String documentDoubleNestedInChannelTagTempText;
                                try
                                {
                                    documentDoubleNestedInChannelTagTempText = parser.nextText();
                                }
                                catch (XmlPullParserException e)
                                {
                                    Log.d("fillChannel()", "Double Nested in Channel START_TAG has no END_TAG");
                                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                                }

                                if (documentNestedInChannelTagText.length() == 0)
                                {
                                    if (documentDoubleNestedInChannelTagTempText != null)
                                    {
                                        documentNestedInChannelTagText.append(markupDoubleNestedInChannelTagName).append(": ").append(documentDoubleNestedInChannelTagTempText);
                                    }
                                }
                                else
                                {
                                    if (documentDoubleNestedInChannelTagTempText != null)
                                    {
                                        documentNestedInChannelTagText.append("\n").append(markupDoubleNestedInChannelTagName).append(": ").append(documentDoubleNestedInChannelTagTempText);
                                    }
                                }

                                break  throughNestedinTestedLoop;
                            }
                        }


                        parser.nextTag();
                        Log.d("fillChannel()",  documentNestedInChannelTagText.toString());
                    }

                    if (!documentNestedInChannel_END_TAG)
                    {
                        //Тэг, содержащий поле заголовка новости, не закрыт
                        throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                    }

                    fillTempChannelField(markupNestedInChannelTagFieldName, documentNestedInChannelTagText.toString(), markupNestedInChannelTag.isMultiple());
                    //if (documentNestedInItemTagText != null)

                }

                //Log.d("fillChannel()", "Document tag does not match nested in Channel tag " + markupNestedInChannelTag.getName());

            }

            Log.d("fillChannel()", "Document tag does not match ANY nested in Channel tag");

            parser.next();

        }

        return channel;
    }


    //Determines markup using root element, finishes with parser at root START_TAG
    private void determineMarkup() throws XmlPullParserException, IOException
    {
        Log.d("determineMarkup()", "Started");

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT)
        {
            if (parser.getEventType() == XmlPullParser.START_TAG)
            {
                String documentTagName = parser.getName();
                int documentTagDepth = parser.getDepth();

                Log.d("determineMarkup()", "Current document tag: name " + documentTagName + ", depth " + documentTagDepth);

                for (Markup KnownMarkup : KnownMarkups)
                {
                    Log.d("determineMarkup()", " Matching document markup with the markup: root tag name " + KnownMarkup.getRoot().getName()
                            + ", root tag depth " + KnownMarkup.getRoot().getDepth());
                    if (KnownMarkup.getRoot().getName().equals(documentTagName) && KnownMarkup.getRoot().getDepth() == documentTagDepth)
                    {
                        markup = KnownMarkup;

                        Log.d("determineMarkup()", "Document Markup has been determined: " + markup.getMarkupName());

                        return;
                    }
                }
            }
            parser.next();
        }

        throw new XmlPullParserException(EXCEPTION_DESCRIPTION_IF_UNKNOWN_MARKUP);
    }


    //Finishes with parser at channel START_TAG
    private void moveParserToChannelTag() throws XmlPullParserException, IOException
    {
        Log.d("moveParserToCh^Tag()", "Started");
        //Reading untill channel START_TAG
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }

            documentTagName = parser.getName();
            documentTagDepth = parser.getDepth();

            Log.d("moveParserToCh^Tag()", "Current document tag: name " + documentTagName + ", depth " + documentTagDepth);

            if (markup.getChannel().getName().equals(documentTagName) && markup.getChannel().getDepth() == documentTagDepth)
            {
                Log.d("moveParserToCh^Tag()", "Channel START_TAG has been found in document");

                return;
            }

            parser.nextTag();
        }

        throw new XmlPullParserException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
    }


    /* Fills temp channel field, which corresponds to observed by parser FieldName, with Value. In appenRegime new callback with the same
     * FieldName adds Value to the temp channel field. If markup allows multiple tags, corresponding to the channel field, new callback
     * adds Value to the temp channel field. Otherwise exception.
     */
    private void fillTempChannelField(String tagFieldName, String tagValue, boolean appendRegime) throws ParsingFailedException
    {
        switch (tagFieldName)
        {
            case TitleFieldName:
                if (null != channelTitle && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelTitle)
                {
                    channelTitle = channelTitle + tagValue;
                }
                else
                {
                    channelTitle = tagValue;
                }
                break;
            case LinkFieldName:
                if (null != channelLink && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelLink)
                {
                    channelLink = channelLink + tagValue;
                }
                else
                {
                    channelLink = tagValue;
                }
                break;
            case DescriptionFieldName:
                if (null != channelDescription && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelDescription)
                {
                    channelDescription = channelDescription + tagValue;
                }
                else
                {
                    channelDescription = tagValue;
                }
                break;
            case LanguageFieldName:
                if (null != channelLanguage && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelLanguage)
                {
                    channelLanguage = channelLanguage + tagValue;
                }
                else
                {
                    channelLanguage = tagValue;
                }
                break;
            case CopyrightFieldName:
                if (null != channelCopyright && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelCopyright)
                {
                    channelCopyright = channelCopyright + tagValue;
                }
                else
                {
                    channelCopyright = tagValue;
                }
                break;
            case ManagingEditorFieldName:
                if (null != channelManagingEditor && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelManagingEditor)
                {
                    channelManagingEditor = channelManagingEditor + tagValue;
                }
                else
                {
                    channelManagingEditor = tagValue;
                }
                break;
            case WebMasterFieldName:
                if (null != channelWebMaster && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelWebMaster)
                {
                    channelWebMaster = channelWebMaster + tagValue;
                }
                else
                {
                    channelWebMaster = tagValue;
                }
                break;
            case PubDateFieldName:
                if (null != channelPubDate && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelPubDate)
                {
                    channelPubDate = channelPubDate + tagValue;
                }
                else
                {
                    channelPubDate = tagValue;
                }
                break;
            case LastBuildDateFieldName:
                if (null != channelLastBuildDate && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelLastBuildDate)
                {
                    channelLastBuildDate = channelLastBuildDate + tagValue;
                }
                else
                {
                    channelLastBuildDate = tagValue;
                }
                break;
            case CategoryFieldName:
                if (null != channelCategory && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelCategory)
                {
                    channelCategory = channelCategory + tagValue;
                }
                else
                {
                    channelCategory = tagValue;
                }
                break;
            case CloudFieldName:
                if (null != channelCloud && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelCloud)
                {
                    channelCloud = channelCloud + tagValue;
                }
                else
                {
                    channelCloud = tagValue;
                }
                break;
            case TTLFieldName:
                if (null != channelTTL && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelTTL)
                {
                    channelTTL = channelTTL + tagValue;
                }
                else
                {
                    channelTTL = tagValue;
                }
                break;
            case ImageFieldName:
                if (null != channelImage && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelImage)
                {
                    channelImage = channelImage + tagValue;
                }
                else
                {
                    channelImage = tagValue;
                }
                break;
            case SkipHoursFieldName:
                if (null != channelSkipHours && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelSkipHours)
                {
                    channelSkipHours = channelSkipHours + tagValue;
                }
                else
                {
                    channelSkipHours = tagValue;
                }
                break;
            case SkipDaysFieldName:
                if (null != channelSkipDays && !appendRegime)
                {
                    throw new ParsingFailedException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != channelSkipDays)
                {
                    channelSkipDays = channelSkipDays + tagValue;
                }
                else
                {
                    channelSkipDays = tagValue;
                }
                break;
        }
    }


    private Channel assemblyChannel() throws IllegalArgumentException
    {

        try
        {
            channel = new Channel(channelTitle, channelLink, channelDescription, channelLanguage,
                    channelCopyright, channelManagingEditor, channelWebMaster, channelPubDate,
                    channelLastBuildDate, channelCategory, channelCloud, channelTTL, channelImage,
                    channelSkipHours, channelSkipDays, itemList);
        }
        catch (IllegalArgumentException e)
        {
            Log.d("assemlyChannel()", "Parsed Channel Link is not valid");

            if (isValidHttpOrHttpsURL(knownChannelURL))
            {
                channel = new Channel(channelTitle, knownChannelURL, channelDescription, channelLanguage,
                        channelCopyright, channelManagingEditor, channelWebMaster, channelPubDate,
                        channelLastBuildDate, channelCategory, channelCloud, channelTTL, channelImage,
                        channelSkipHours, channelSkipDays, itemList);

                Log.d("assemlyChannel()", "Channel has been assemled with known channel link, because parsed one is not valid");

            }
        }


        return channel;
    }


    private void fillItem() throws XmlPullParserException, IOException, ParsingNotifyingException
    {
        Log.d("fillItem()", "Started");

        Tag markupItemTag = markup.getItem();

        documentTagName = parser.getName();
        documentTagDepth = parser.getDepth();
        documentEventType = parser.getEventType();


        Log.d("fillItem()", "Current document tag: name " + documentTagName + ", depth " + documentTagDepth + ", type " + getEventTypeName(documentEventType));


        //Matching in document Item attributes with markup fields established as attributes
        Tag markupItemTagAttributeTag;
        String markupItemTagAttributeTagFieldName;
        String markupItemTagAttributeTagKey;
        String documentItemTagAttributeTagValue;

        for (int i = 0; i < markupItemTag.getAttributes().size(); i++)
        {
            Log.d("fillItem()", "Matching document Item tag attributes with markup fields established as attributes");

            markupItemTagAttributeTag = markupItemTag.getAttributes().get(i);
            markupItemTagAttributeTagFieldName = markupItemTagAttributeTag.getFieldName();
            markupItemTagAttributeTagKey = markupItemTagAttributeTag.getName();
            documentItemTagAttributeTagValue = parser.getAttributeValue(NAMESPACE, markupItemTagAttributeTagKey);



            //Если аттрибут описывает обособленное поле класса Item
            if (null != markupItemTagAttributeTagFieldName)
            {
                fillTempItemField(markupItemTagAttributeTagFieldName, documentItemTagAttributeTagValue, markupItemTagAttributeTag.isMultiple());
            }
            /* Если аттрибут не описывает обособленное поле класса Item, а является частью значения,
             * которое должно быть присвоено полю Item класса Item ( полю, которого нет ) - не делать ничего.
             */
        }

        parser.nextTag();

        //Искать совпадения        определенных в разметке дочерних Item тэгов     с текущим тэгом в файле
        throughDocEventsLoop:
        while (documentEventType != XmlPullParser.END_DOCUMENT && documentTagDepth >= markupItemTag.getDepth())
        {
            documentTagName = parser.getName();
            documentTagDepth = parser.getDepth();
            documentEventType = parser.getEventType();

            Log.d("fillItem()", "Current document tag: name " + documentTagName + ", depth " + documentTagDepth + ", type " + getEventTypeName(documentEventType));


            //Finish Item parsing after first Item END_TAG has been observed.
            if (documentEventType == XmlPullParser.END_TAG && markupItemTag.getName().equals(documentTagName) && documentTagDepth == markupItemTag.getDepth())
            {
                Log.d("fillItem()", "Item closing tag has been found");
                assemblyItemAndAddToListIfValid();
                return;
            }
            else if (documentEventType != XmlPullParser.START_TAG)
            {
                parser.nextTag();
                continue;
            }


            /*
             * Заполнить поля Item.
             */
            Tag markupNestedInItemTag;
            String markupNestedInItemTagFieldName;
            boolean markupNestedInItemTagIsMuliple;


            Log.d("fillItem()", "Matching document tag with markup tags nested in Item tag has started");
            for (int i = 0; i < markupItemTag.getNested().size(); i++)
            {
                markupNestedInItemTag = markupItemTag.getNested().get(i);
                markupNestedInItemTagFieldName = markupNestedInItemTag.getFieldName();
                markupNestedInItemTagIsMuliple = markupNestedInItemTag.isMultiple();



                /* Заполнить поля класса Item, для которых тэги в разметке не содержат дочерних тэгов.
                 * Если дочерний новости тэг разметки имеет аттрибуты, добавить их значения в поле класса Item, соотвествующее тэгу.
                 */
                if (markupNestedInItemTag.getNested().size() == 0 && documentTagDepth == markupNestedInItemTag.getDepth() && markupNestedInItemTag.getName().equals(documentTagName))
                {
                    Log.d("fillItem()", "Matching document tag with markup tags nested in Item tag. Through nested tags which HAVE NOT nested tags");
                    Log.d("fillItem()", "The nested in Item tag has attributes defined in markup (amount): " + markupNestedInItemTag.getAttributes().size());

                    //Если дочерний каналу тэг разметки имеет аттрибуты, добавить их значения в поле класса Item, соотвествующее тэгу.
                    StringBuilder documentNestedInItemTagAttributeTagValue = new StringBuilder();

                    for (int j = 0; j < markupNestedInItemTag.getAttributes().size(); j++)
                    {

                        Tag    markupNestedInItemTagAttributeTag =    markupNestedInItemTag.getAttributes().get(j);
                        String markupNestedInItemTagAttributeTagKey = markupNestedInItemTagAttributeTag.getName();


                        String documentNestedInItemTagAttributeTagTempValue = parser.getAttributeValue(NAMESPACE, markupNestedInItemTagAttributeTagKey);

                        //TODO Define in Tag class field "If Tag has attributes, its a separator between them" and "Add attribute key to the value of Item filed corresponding to nested in Item tag


                        if (documentNestedInItemTagAttributeTagValue.length() != 0)
                        {
                            documentNestedInItemTagAttributeTagValue.append(" ").append(documentNestedInItemTagAttributeTagTempValue);
                        }
                        else
                        {
                            documentNestedInItemTagAttributeTagValue.append(documentNestedInItemTagAttributeTagTempValue);
                        }
                    }


                    //Прочитать текст тэга, вложенного в Item
                    String documentNestedInItemTagText;
                    try
                    {
                        //nextText() остановится на теэе, закрывающем вложенный в Item тэг, или выдаст XmlPullParserException. Если XmlPullParserException, бросить ParsingNotifyingException
                        //Т.е.
                        documentNestedInItemTagText = parser.nextText();

                        //Если тэг содержал текст и аттрибуты - предпочесть текст, аттрибуты отбросить
                        if (documentNestedInItemTagText != null && documentNestedInItemTagText.length() != 0)
                        {
                            fillTempItemField(markupNestedInItemTagFieldName, documentNestedInItemTagText, markupNestedInItemTagIsMuliple);
                        }
                        else if (documentNestedInItemTagAttributeTagValue.length() != 0)
                        {
                            fillTempItemField(markupNestedInItemTagFieldName, documentNestedInItemTagAttributeTagValue.toString(), markupNestedInItemTagIsMuliple);
                        }


                        /*Если имя закрывающего тэга != имени открывающего - бросить исключение (или прервать парсинг Item). Бросить ParsingNotifyingException
                         * так как парсится Item.
                         */
                        if ( !markupNestedInItemTag.getName().equals(parser.getName()) )
                        {
                            Log.d("fillItem()", "Nested in Item START_TAG and its END_TAG have different names");
                            throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                        }
                    }
                    catch (XmlPullParserException e)
                    {
                        Log.d("fillItem()", "Nested in Item START_TAG has no END_TAG");
                        throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                    }


                    //Перевожу парсер на следующий START_TAG
                    parser.nextTag();
                    continue throughDocEventsLoop;
                }


                /* Заполнить поля класса Item, для которых тэги в разметке содержат дочерние тэги.
                 * Если тэг в Item имеет вложенные тэги, он не меет текста. Аттрибуты тэга, вложенного во вложенный в Item тэг, не читаются.
                 */
                if (markupNestedInItemTag.getNested().size() > 0 && documentTagDepth == markupNestedInItemTag.getDepth() && markupNestedInItemTag.getName().equals(documentTagName))
                {
                    Log.d("fillItem()", "Matching document tag with markup tags nested in Item tag. Through nested tags which HAVE nested tags");
                    Log.d("fillItem()", "The nested in Item tag has nested (amount): " + markupNestedInItemTag.getNested().size());


                    StringBuilder documentNestedInItemTagText = new StringBuilder();
                    boolean documentNestedInItem_END_TAG = false;

                    while (documentEventType != XmlPullParser.END_DOCUMENT && documentTagDepth >= markupNestedInItemTag.getDepth())
                    {
                        documentTagName = parser.getName();
                        documentTagDepth = parser.getDepth();
                        documentEventType = parser.getEventType();

                        if (documentTagDepth == markupNestedInItemTag.getDepth() && markupNestedInItemTag.getName().equals(documentTagName) && documentEventType == XmlPullParser.END_TAG)
                        {
                            Log.d("fillItem()", "END_TAG of nested in Item tag");
                            documentNestedInItem_END_TAG = true;
                            break;
                        }

                        throughNestedinTestedLoop: for (int j = 0; j < markupNestedInItemTag.getNested().size(); j++)
                        {
                            Tag markupDoubleNestedInItemTag = markupNestedInItemTag.getNested().get(j);
                            String markupDoubleNestedInItemTagName = markupDoubleNestedInItemTag.getName();

                            documentTagName = parser.getName();
                            documentTagDepth = parser.getDepth();
                            documentEventType = parser.getEventType();


                            if (markupDoubleNestedInItemTagName.equals(documentTagName) && documentTagDepth == markupDoubleNestedInItemTag.getDepth())
                            {
                                String documentDoubleNestedInItemTagTempText;
                                try
                                {
                                    documentDoubleNestedInItemTagTempText = parser.nextText();
                                }
                                catch (XmlPullParserException e)
                                {
                                    Log.d("fillItem()", "Double Nested in Item START_TAG has no END_TAG");
                                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                                }

                                if (documentNestedInItemTagText.length() == 0)
                                {
                                    if (documentDoubleNestedInItemTagTempText != null)
                                    {
                                        documentNestedInItemTagText.append(markupDoubleNestedInItemTagName).append(": ").append(documentDoubleNestedInItemTagTempText);
                                    }
                                }
                                else
                                {
                                    if (documentDoubleNestedInItemTagTempText != null)
                                    {
                                        documentNestedInItemTagText.append("\n").append(markupDoubleNestedInItemTagName).append(": ").append(documentDoubleNestedInItemTagTempText);
                                    }
                                }

                                break  throughNestedinTestedLoop;
                            }
                        }


                        parser.nextTag();
                        Log.d("fillItem()",  documentNestedInItemTagText.toString());
                    }

                    if (!documentNestedInItem_END_TAG)
                    {
                        //Тэг, содержащий поле заголовка новости, не закрыт
                        throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                    }

                    fillTempItemField(markupNestedInItemTagFieldName, documentNestedInItemTagText.toString(), markupNestedInItemTag.isMultiple());
                    //if (documentNestedInItemTagText != null)

                }

                //Log.d("fillItem()", "Document tag does not match nested in Item tag " + markupNestedInItemTag.getName());

            }

            Log.d("fillItem()", "Document tag does not match ANY nested in Item tag");

            parser.next();
        }
    }


    private void fillTempItemField(String tagFieldName, String tagValue, boolean appendRegime) throws ParsingNotifyingException
    {

        switch (tagFieldName)
        {
            case ItemTitleFieldName:
                if (null != itemTitle && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemTitle)
                {
                    itemTitle = itemTitle + tagValue;
                }
                else
                {
                    itemTitle = tagValue;
                }
                break;
            case ItemLinkFieldName:
                if (null != itemLink && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemLink)
                {
                    itemLink = itemLink + tagValue;
                }
                else
                {
                    itemLink = tagValue;
                }
                break;
            case ItemDescriptionFieldName:
                if (null != itemDescription && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemDescription)
                {
                    itemDescription = itemDescription + tagValue;
                }
                else
                {
                    itemDescription = tagValue;
                }
                break;
            case ItemAuthorFieldName:
                if (null != itemAuthor && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemAuthor)
                {
                    itemAuthor = itemAuthor + tagValue;
                }
                else
                {
                    itemAuthor = tagValue;
                }
                break;
            case ItemCategoryFieldName:
                if (null != itemCategory && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemCategory)
                {
                    itemCategory = itemCategory + tagValue;
                }
                else
                {
                    itemCategory = tagValue;
                }
                break;
            case ItemEnclosureFieldName:
                if (null != itemEnclosure && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemEnclosure)
                {
                    itemEnclosure = itemEnclosure + tagValue;
                }
                else
                {
                    itemEnclosure = tagValue;
                }
                break;
            case ItemGuidFieldName:
                if (null != itemGuid && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemGuid)
                {
                    itemGuid = itemGuid + tagValue;
                }
                else
                {
                    itemGuid = tagValue;
                }
                break;
            case ItemPubDateFieldName:
                if (null != itemPubDate && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemPubDate)
                {
                    itemPubDate = itemPubDate + tagValue;
                }
                else
                {
                    itemPubDate = tagValue;
                }
                break;
            case ItemSourceFieldName:
                if (null != itemSource && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemSource)
                {
                    itemSource = itemSource + tagValue;
                }
                else
                {
                    itemSource = tagValue;
                }
                break;
            case ItemContentFieldName:
                if (null != itemContent && !appendRegime)
                {
                    throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_INVALID_MARKUP);
                }
                if (null != itemContent)
                {
                    itemContent = itemContent + tagValue;
                }
                else
                {
                    itemContent = tagValue;
                }
                break;
        }
    }





    private void assemblyItemAndAddToListIfValid() throws ParsingNotifyingException
    {

        Item item;
        try
        {
            item = new Item(itemTitle, itemLink, itemDescription, itemAuthor, itemCategory,
                    itemEnclosure, itemGuid, itemPubDate, itemSource, itemContent);
        }
        catch (IllegalArgumentException e)
        {
            Log.d("assemblyItem...()", EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
            throw new ParsingNotifyingException(EXCEPTION_DESCRIPTION_IF_ITEM_INVALID);
        }
        finally
        {
            setTempItemFieldsNull();
        }


        for (int i=0; i<itemList.size(); i++)
        {
            if (itemList.get(i).equals(item))
                return;
        }

        itemList.add(0, item);
        Log.d("assemblyItem...()", "The item has been added to the Channel");
        Log.d("assemblyItem...()", item.toString());
    }


    private void setTempItemFieldsNull()
    {
        itemTitle = null;
        itemLink = null;
        itemDescription = null;
        itemAuthor = null;
        itemCategory = null;
        itemEnclosure = null;
        itemGuid = null;
        itemPubDate = null;
        itemSource = null;
        itemContent = null;
    }


    private static String getEventTypeName(int evenType)
    {
        switch (evenType)
        {
            case XmlPullParser.CDSECT:
                return "CDSECT";
            case XmlPullParser.COMMENT:
                return "COMMENT";
            case XmlPullParser.DOCDECL:
                return "DOCDECL";
            case XmlPullParser.END_DOCUMENT:
                return "END_DOCUMENT";
            case XmlPullParser.END_TAG:
                return "END_TAG";
            case XmlPullParser.ENTITY_REF:
                return "ENTITY_REF";
            case XmlPullParser.IGNORABLE_WHITESPACE:
                return "IGNORABLE_WHITESPACE";
            case XmlPullParser.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";
            case XmlPullParser.START_DOCUMENT:
                return "START_DOCUMENT";
            case XmlPullParser.START_TAG:
                return "START_TAG";
            case XmlPullParser.TEXT:
                return "TEXT";
            default:
                return "UNKNOWN EVENT TYPE";
        }
    }






}



