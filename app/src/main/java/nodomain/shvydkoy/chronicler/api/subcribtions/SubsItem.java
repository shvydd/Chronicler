package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.webfeed.Item;



public class SubsItem extends Item
{
    private Calendar RecievedDate;
    private boolean  Starred;

    public SubsItem(Item parsedItem)
    {
        super(parsedItem);

        RecievedDate = Calendar.getInstance();
        Starred = false;
    }

    final public void MakeStarred()
    {
        Starred = true;
    }

    final public void MakeNotStarred()
    {
        Starred = false;
    }

    final public Calendar getRecievedDate()
    {
        return RecievedDate;
    }

    final public boolean isStarred()
    {
        return Starred;
    }



}
