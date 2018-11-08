package nodomain.shvydkoy.chronicler.channelsScreen.test;

import android.util.Log;

import nodomain.shvydkoy.chronicler.api.subcribtions.SubsManager;
import nodomain.shvydkoy.chronicler.api.subcribtions.UserNotifyingException;
import nodomain.shvydkoy.chronicler.channelsScreen.ChannelListFragment;



public final class ChannelScreenFillingTest
{
    static public void fillTest(final SubsManager subsManager, final ChannelListFragment channelListFragment)
    {
        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_1);
            Log.d("addChannel","constantChannel_1 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_2);
            Log.d("addChannel","constantChannel_2 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_3);
            Log.d("addChannel","constantChannel_3 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_4);
            Log.d("addChannel","constantChannel_4 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_4);
            Log.d("addChannel","constantChannel_4 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }
        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_5);
            Log.d("addChannel","constantChannel_5 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_6);
            Log.d("addChannel","constantChannel_6 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }


        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_7);
            Log.d("addChannel","constantChannel_7 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_8);
            Log.d("addChannel","constantChannel_8 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_9);
            Log.d("addChannel","constantChannel_9 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelConstFeedStrings.constantChannel_10);
            Log.d("addChannel","constantChannel_10 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        Log.d("fillTest", "all channels added");

        subsManager.confirmChannel(3);

        // Log.d("fillTest", "all channels added, 3rd confirmed");

        channelListFragment.setChannels(subsManager.getSubscriptions());

        Log.d("fillTest", "Channels have been set");
    }


}
