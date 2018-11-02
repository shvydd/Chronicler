package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.webfeed.Item;



final class SubsItem extends Item
{
    private Calendar RecievedDate;
    private boolean  Starred;

    public SubsItem(final Item parsedItem)
    {
        super(parsedItem);

        RecievedDate = Calendar.getInstance();
        Starred = false;
    }

    final void MakeStarred()
    {
        Starred = true;
    }

    final void MakeNotStarred()
    {
        Starred = false;
    }

    final Calendar getRecievedDate()
    {
        return RecievedDate;
    }

    final boolean isStarred()
    {
        return Starred;
    }



}
