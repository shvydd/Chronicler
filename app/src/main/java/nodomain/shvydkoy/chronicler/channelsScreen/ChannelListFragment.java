package nodomain.shvydkoy.chronicler.channelsScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nodomain.shvydkoy.chronicler.R;
import nodomain.shvydkoy.chronicler.api.subcribtions.SubsChannel;



public final class ChannelListFragment extends Fragment
{

    final private ArrayList<SubsChannel> AllSubscriptions = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChannelRecyclerViewAdapter channelRecyclerViewAdapter = new ChannelRecyclerViewAdapter(AllSubscriptions);


    public ChannelListFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.channel_list);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(channelRecyclerViewAdapter);
    }



    public void setChannels(ArrayList<SubsChannel> AllSubscriptions)
    {
        this.AllSubscriptions.clear();
        this.AllSubscriptions.addAll(AllSubscriptions);
        Log.d("setChannels", "added");

        channelRecyclerViewAdapter.notifyDataSetChanged();

    }




}
