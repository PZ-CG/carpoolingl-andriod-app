package com.osu.pzcg.carpool.Adapters;

/**
 * Created by GoThumbers on 11/9/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.activity.ObjectDrawerItem;

/**
 * Created by peihongzhong on 10/31/15.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

    Context mContext;
    int layoutResourceId;
    ObjectDrawerItem data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        ObjectDrawerItem folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }


}
