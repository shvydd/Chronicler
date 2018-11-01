package nodomain.shvydkoy.chronicler.api.webfeed.markup;

public final class BankOfMarkups
{
    private final static Markup[] KnownMarkups = {new Markup_RSS_2_0(), new Markup_Atom_1_0()};


    public final static Markup[] getMarkups()
    {
        return KnownMarkups;
    }

}
