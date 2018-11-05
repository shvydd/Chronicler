package nodomain.shvydkoy.chronicler.channelsScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nodomain.shvydkoy.chronicler.R;
import nodomain.shvydkoy.chronicler.api.subcribtions.SubsManager;
import nodomain.shvydkoy.chronicler.api.subcribtions.UserNotifyingException;



public class ChannelsActivity extends AppCompatActivity
{

    private final static String TAG_CHANNEL_LIST_FRAGMENT = "TAG_CHANNEL_LIST_FRAGMENT";

    ChannelListFragment channelListFragment;
    SubsManager subsManager;


    //Meier, R., 2012. Professional Android 4 application development. John Wiley & Sons. p.91
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        subsManager = new SubsManager();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            channelListFragment = new ChannelListFragment();
            fragmentTransaction.add(R.id.channels_activity_frame, channelListFragment, TAG_CHANNEL_LIST_FRAGMENT);

            fragmentTransaction.commitNow();
        }
        else
        {
            channelListFragment = (ChannelListFragment) fragmentManager.findFragmentByTag(TAG_CHANNEL_LIST_FRAGMENT);
        }


        fillTest();
    }



    private void fillTest()
    {
        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_1);
            Log.d("addChannel","constantChannel_1 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_2);
            Log.d("addChannel","constantChannel_2 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());
        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_3);
            Log.d("addChannel","constantChannel_3 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_4);
            Log.d("addChannel","constantChannel_4 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_4);
            Log.d("addChannel","constantChannel_4 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }
        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_5);
            Log.d("addChannel","constantChannel_5 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_6);
            Log.d("addChannel","constantChannel_6 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }


        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_7);
            Log.d("addChannel","constantChannel_7 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_8);
            Log.d("addChannel","constantChannel_8 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_9);
            Log.d("addChannel","constantChannel_9 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }

        try
        {
            subsManager.addChannel(ChannelScreenTest.constantChannel_10);
            Log.d("addChannel","constantChannel_10 added");
        }
        catch (UserNotifyingException e)
        {
            Log.d("addChannel", e.getMessage());

        }



        channelListFragment.setChannels(subsManager.getAllSubscriptions());
    }


}
