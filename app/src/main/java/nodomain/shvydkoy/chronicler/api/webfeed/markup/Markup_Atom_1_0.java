package nodomain.shvydkoy.chronicler.api.webfeed.markup;

public final class Markup_Atom_1_0 extends Markup
{
    public Markup_Atom_1_0()
    {

        //https://validator.w3.org/feed/docs/atom.html#category
        MarkupName = "Atom 1.0";

        Root = new Tag("feed", false);

            Root.nest(Channel = Root);

                Channel.nest(Title = new Tag("title"));
                    //Tag channelLinkHolder = new Tag("link");
                    //Link =  new Tag("href", channelLinkHolder);
                //Channel.nest(channelLinkHolder);

                    Link = new Tag("link");
                    Link.addAttribute("href");
                Channel.nest(Link);


                Channel.nest(Description = new Tag("subtitle"));
                Channel.nest(Language = new Tag("language"));
                Channel.nest(Copyright = new Tag("rights"));
                Channel.nest(ManagingEditor = new Tag("author", true));
                    ManagingEditor.nest(new Tag("name", true));
                    ManagingEditor.nest(new Tag("email", true));
                    ManagingEditor.nest(new Tag("uri", true));
                Channel.nest(WebMaster = new Tag("contributor"));
                    WebMaster.nest(new Tag("name", true));
                    WebMaster.nest(new Tag("email", true));
                    WebMaster.nest(new Tag("uri", true));
                Channel.nest(PubDate = new Tag("updated"));
                Channel.nest(LastBuildDate = new Tag("updated"));
                    Category = new Tag("category", true);
                    Category.addAttribute("term");
                 Channel.nest(Category);
                //Channel.nest(Cloud = new Tag("cloud"));

                //Channel.nest(TTL = new Tag("ttl"));
                Channel.nest(Image = new Tag("logo"));
                //Channel.nest(SkipHours = new Tag("skipHours"));
                //Channel.nest(SkipDays = new Tag("skipDays"));
                Channel.nest(Item = new Tag("entry",true));
                        Item.nest(ItemTitle = new Tag("title"));
                        Item.nest(ItemLink = new Tag("link"));
                            ItemLink.addAttribute("href");
                        Item.nest(ItemDescription = new Tag("summary"));
                        Item.nest(ItemAuthor = new Tag("author"));
                            ItemAuthor.nest(new Tag("name", true));
                            ItemAuthor.nest(new Tag("email", true));
                            ItemAuthor.nest(new Tag("uri", true));
                        Item.nest(ItemCategory = new Tag("category", true));
                            ItemCategory.addAttribute("term");
                        //Item.nest(ItemEnclosure = new Tag("enclosure"));
                        Item.nest(ItemGuid = new Tag("id"));
                        Item.nest(ItemPubDate = new Tag("updated"));
                        Item.nest(ItemSource = new Tag("source"));
                            ItemSource.nest(new Tag("id"));
                            ItemSource.nest(new Tag("title"));
                            ItemSource.nest(new Tag("updated"));
                        Item.nest(ItemContent = new Tag("content"));

        setFieldNames();
    }



}
