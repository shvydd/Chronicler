package nodomain.shvydkoy.chronicler.channelsScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nodomain.shvydkoy.chronicler.R;
import nodomain.shvydkoy.chronicler.api.subcribtions.SubsChannel;



public final class ChannelRecyclerViewAdapter extends RecyclerView.Adapter<ChannelRecyclerViewAdapter.ViewHolder>
{
    private final ArrayList<SubsChannel> AllSubscriptions;



    public ChannelRecyclerViewAdapter(ArrayList<SubsChannel> AllSubscriptions)
    {
        this.AllSubscriptions = AllSubscriptions;
    }


    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder (final ViewHolder holder, int position)
    {
        holder.channel = AllSubscriptions.get(position);
        holder.title.setText(AllSubscriptions.get(position).getTitle());
    }


    @Override
    public int getItemCount()
    {
        return AllSubscriptions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View parentView;
        public final TextView title;
        public SubsChannel channel;

        public ViewHolder(View view)
        {
            super(view);

            parentView = view;
            title = (TextView) view.findViewById(R.id.channel_item_title);
        }


        @Override
        public String toString()
        {
            //TODO
            return super.toString() + " '" + title.getText() + "'";
        }

    }





}
