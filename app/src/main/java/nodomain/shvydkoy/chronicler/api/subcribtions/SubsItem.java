package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.webfeed.Item;



final class SubsItem extends Item
{
    private Calendar RecievedDate;
    private boolean  Starred;
    private boolean Hidden;

    SubsItem(final Item parsedItem)
    {
        super(parsedItem);

        RecievedDate = Calendar.getInstance();
        Starred = false;
        Hidden = false;
    }

    final void makeStarred()
    {
        Starred = true;
    }

    final void makeNotStarred()
    {
        Starred = false;
    }

    final boolean isStarred()
    {
        return Starred;
    }

    final Calendar getRecievedDate()
    {
        return RecievedDate;
    }

    final void makeHidden() { Hidden = true; }

    final void makeNotHidden() { Hidden = false; }

    final boolean isHidden() { return Hidden; }


}
