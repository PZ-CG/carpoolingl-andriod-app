package com.osu.pzcg.carpool.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.osu.pzcg.carpool.R;


/**
 * Created by peihongzhong on 10/31/15.
 */
public class CreateFragment extends Fragment {
    public Button offerButton;
    public Button wantButton;

    public CreateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);
        offerButton = (Button) view.findViewById(R.id.offer);

        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OfferCarActivity.class);
                startActivity(intent);
            }
        });
        wantButton = (Button) view.findViewById(R.id.want);
        wantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WantCarActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
