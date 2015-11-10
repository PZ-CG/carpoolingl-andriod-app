
    package com.osu.pzcg.carpool.Adapters;

    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.osu.pzcg.carpool.Info.ListCard;
    import com.osu.pzcg.carpool.R;
    import com.osu.pzcg.carpool.async.JoinAsync;

    import java.util.List;
    import java.util.concurrent.ExecutionException;

    /**
     * Created by GoThumbers on 11/6/15.
     */
    public class SpecialListAdapters extends BaseAdapter {

        private List<ListCard> mCards;
        private Context mContext;
        private String current_user;


        public SpecialListAdapters(Context mContext,List<ListCard> mCards,String current_user)
        {
            this.mContext=mContext;
            this.mCards=mCards;
            this.current_user = current_user;
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
            mView= LayoutInflater.from(mContext).inflate(R.layout.special_card, null);
            mHolder.cardTitle=(TextView)mView.findViewById(R.id.publisher);
            mHolder.cardTitle.setText(mCards.get(index).getName());
            mHolder.cardTime=(TextView)mView.findViewById(R.id.time_special);
            mHolder.cardTime.setText(mCards.get(index).getTime());
            mHolder.depart=(TextView)mView.findViewById(R.id.event_name);
            mHolder.depart.setText(mCards.get(index).getDeparture());
            mHolder.destin=(TextView)mView.findViewById(R.id.destination_special);
            mHolder.destin.setText(mCards.get(index).getDestination());
            mHolder.cardSeats=(TextView)mView.findViewById(R.id.seat_left_special);
            mHolder.cardSeats.setText(mCards.get(index).getSeats());




            Button b = (Button) mView.findViewById(R.id.join_special);
            b.setTag(index);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(mContext, "THIS IS MY TOAST" + mCards.get((Integer)(v.getTag())).getDeparture(),
                    //      Toast.LENGTH_SHORT).show();
                    String name = mCards.get((Integer)(v.getTag())).getName();
                    String time = mCards.get((Integer)(v.getTag())).getTime();
                    String departure = mCards.get((Integer)(v.getTag())).getDeparture();
                    String destination = mCards.get((Integer)(v.getTag())).getDestination();
                    int seats = Integer.parseInt(mCards.get((Integer)(v.getTag())).getSeats());
                    try {
                        if (name != null && time != null && departure != null && destination != null && seats != 0 && current_user != null) {
                            String result = new JoinAsync(mContext).execute(name, time , departure, destination, (seats-1)+"",current_user).get();
                            Log.i("guo", result);
                            if (result!=null){
                                Toast.makeText(mContext, "Join successful!", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show();
                        }

                    }catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }


                }
            });
            return mView;


        }

        private static class ViewHolder
        {
            TextView cardTitle;
            TextView cardTime;
            TextView depart;
            TextView destin;
            TextView cardSeats;
        }



    }


