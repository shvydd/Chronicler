package nodomain.shvydkoy.chronicler.api.subcribtions;

import java.util.Calendar;

import nodomain.shvydkoy.chronicler.api.webfeed.Item;



final class SubsItem extends Item
{
    private Calendar RecievedDate;
    private boolean  Starred;
    private boolean  Hidden;
    private boolean  Read;
    private boolean  ReObservedAtLastUpdate;
    private boolean  CheckedAndNotReObservedAtLastUpdate;

    SubsItem(final Item parsedItem)
    {
        super(parsedItem);

        RecievedDate = Calendar.getInstance();
        Starred = false;
        Hidden = false;
        Read = false;
        ReObservedAtLastUpdate = false;
        CheckedAndNotReObservedAtLastUpdate = false;
    }

    final Calendar getRecievedDate()
    {
        return RecievedDate;
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



    final void makeHidden()
    {
        Hidden = true;
        Starred = false;
    }

    final boolean isHidden() { return Hidden; }



    final void makeRead() { Read = true; }

    final void makeUnRead() { Read = false; }

    final boolean isRead() { return Read; }



    final void makeReObservedAtLastUpdate() { ReObservedAtLastUpdate = true; }

    final void makeNotReObservedAtLastUpdate() { ReObservedAtLastUpdate = false; }

    final boolean isReObservedAtLastUpdate() { return ReObservedAtLastUpdate; }



    final void makeCheckedAtLastUpdate() { CheckedAndNotReObservedAtLastUpdate = true; }

    final void makeNotCheckedAtLastUpdate() { CheckedAndNotReObservedAtLastUpdate = false; }

    final boolean isCheckedAtLastUpdate() { return CheckedAndNotReObservedAtLastUpdate; }


}
