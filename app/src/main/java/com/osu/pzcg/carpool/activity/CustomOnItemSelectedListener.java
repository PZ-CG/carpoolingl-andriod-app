package com.osu.pzcg.carpool.activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * Created by peihongzhong on 11/4/15.
 */
public class CustomOnItemSelectedListener implements OnItemSelectedListener{
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}

