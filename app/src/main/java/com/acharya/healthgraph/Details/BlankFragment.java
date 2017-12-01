package com.acharya.healthgraph.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acharya.healthgraph.GameList.MainActivity;
import com.acharya.healthgraph.Login.Login;
import com.acharya.healthgraph.R;


public class BlankFragment extends Fragment {
    private  View view;
    TextView details,title;
    ImageButton backbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_blank, container, false);

        details = (TextView)view.findViewById(R.id.info);

        backbutton = (ImageButton)view.findViewById(R.id.back);
        String info = getArguments().getString("information");
        details.setText(Html.fromHtml(info));
        String title_info = getArguments().getString("title");
        title = (TextView)view.findViewById(R.id.title);
        title.setText(title_info);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFragmentManager().popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }



}
