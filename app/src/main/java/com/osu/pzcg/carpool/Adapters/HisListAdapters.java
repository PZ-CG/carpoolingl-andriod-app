package com.osu.pzcg.carpool.Adapters;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.TextView;
    import com.osu.pzcg.carpool.Info.ListCard;
    import com.osu.pzcg.carpool.R;

    import java.util.List;

    /**
     * Created by GoThumbers on 11/6/15.
     */
    public class HisListAdapters extends BaseAdapter {

        private List<ListCard> mCards;
        private Context mContext;


        public HisListAdapters(Context mContext,List<ListCard> mCards)
        {
            this.mContext=mContext;
            this.mCards=mCards;
        }
        @Override
        public int getCount()
        {
            return mCards.size();
        }

        @Override
        public Object getItem(int index)
        {
            return mCards.get(index);
        }

        @Override
        public long getItemId(int index)
        {
            return index;
        }

        @Override
        public View getView(int index, View mView, ViewGroup mParent)
        {
            ViewHolder mHolder=new ViewHolder();
            mView= LayoutInflater.from(mContext).inflate(R.layout.history_card, null);
            mHolder.cardTitle=(TextView)mView.findViewById(R.id.his_name);
            mHolder.cardTitle.setText(mCards.get(index).getName());
            mHolder.cardTime=(TextView)mView.findViewById(R.id.his_time);
            mHolder.cardTime.setText(mCards.get(index).getTime());
            mHolder.depart=(TextView)mView.findViewById(R.id.his_departure);
            mHolder.depart.setText(mCards.get(index).getDeparture());
            mHolder.destin=(TextView)mView.findViewById(R.id.his_destination);
            mHolder.destin.setText(mCards.get(index).getDestination());
            mHolder.peopleSum=(TextView)mView.findViewById(R.id.his_seat);
            mHolder.peopleSum.setText(mCards.get(index).getSeats());

            return mView;


        }

        private static class ViewHolder
        {
            TextView cardTitle;
            TextView cardTime;
            TextView depart;
            TextView destin;
            TextView peopleSum;
        }
    }


