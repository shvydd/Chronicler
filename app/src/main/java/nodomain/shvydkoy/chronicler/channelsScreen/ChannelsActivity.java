package nodomain.shvydkoy.chronicler.channelsScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import nodomain.shvydkoy.chronicler.R;
import nodomain.shvydkoy.chronicler.api.subcribtions.SubsManager;
import nodomain.shvydkoy.chronicler.channelsScreen.test.ChannelScreenFillingTest;



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


        ChannelScreenFillingTest.fillTest(subsManager, channelListFragment);
    }


}
