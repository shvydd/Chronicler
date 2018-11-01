package nodomain.shvydkoy.chronicler.api.webfeed.markup;

public final class Markup_RSS_2_0 extends Markup
{
    public Markup_RSS_2_0()
    {
        //http://www.rssboard.org/rss-specification#ltcategorygtSubelementOfLtitemgt
        MarkupName = "RSS 2.0";

        Root = new Tag("rss",  false);
        Root.addAttribute("version", "2.0");

            Root.nest(Channel = new Tag("channel"));

                Channel.nest(Title = new Tag("title"));
                Channel.nest(Link = new Tag("link"));

                Channel.nest(Description = new Tag("description"));
                Channel.nest(Language = new Tag("language"));
                Channel.nest(Copyright = new Tag("copyright"));
                Channel.nest(ManagingEditor = new Tag("managingEditor"));
                Channel.nest(WebMaster = new Tag("webMaster"));
                Channel.nest(PubDate = new Tag("pubDate"));
                Channel.nest(LastBuildDate = new Tag("lastBuildDate"));
                Channel.nest(Category = new Tag("category", true));
                Channel.nest(Cloud = new Tag("cloud"));
                    Cloud.addAttribute("domain");
                    Cloud.addAttribute("port");
                    Cloud.addAttribute("path");
                    Cloud.addAttribute("registerProcedure");
                    Cloud.addAttribute("protocol");
                Channel.nest(TTL = new Tag("ttl"));
                Channel.nest(Image = new Tag("image"));
                    Image.nest(new Tag("url"));
                    Image.nest(new Tag("link"));
                Channel.nest(SkipHours = new Tag("skipHours"));
                Channel.nest(SkipDays = new Tag("skipDays"));
                Channel.nest(Item = new Tag("item",true));
                        Item.nest(ItemTitle = new Tag("title"));
                        Item.nest(ItemLink = new Tag("link"));
                        Item.nest(ItemDescription = new Tag("description"));
                        Item.nest(ItemAuthor = new Tag("author"));
                        Item.nest(ItemCategory = new Tag("category", true));
                        Item.nest(ItemEnclosure = new Tag("enclosure"));
                            ItemEnclosure.addAttribute("url");
                            ItemEnclosure.addAttribute("length");
                            ItemEnclosure.addAttribute("type");
                        Item.nest(ItemGuid = new Tag("guid"));
                        Item.nest(ItemPubDate = new Tag("pubDate"));
                        Item.nest(ItemSource = new Tag("source"));
                            ItemSource.addAttribute("url");

        setFieldNames();
    }


}
