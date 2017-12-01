package com.acharya.healthgraph.Details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acharya.healthgraph.GameList.MainActivity;
import com.acharya.healthgraph.R;
import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Details extends AppCompatActivity {

    String appid, name, users, required_age,
            about_the_game, detailed_description,
            short_description, minimum, recommended, rank;
    ExpandableTextView
            tabout_the_game, tdetailed_description,
            tshort_description, tminimum, trecommended;

    TextView tusers, trequired_age, trank;

    ImageButton ibabout, ibdetailed_description, ibminimum_requirement, ibrecomended_requirement;

    private static FragmentManager fragmentManager;

    LinearLayout linearLayout;
    int position;
    ImageView imageView;
    android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        fragmentManager = getSupportFragmentManager();

        getSupportActionBar().hide();
        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            appid = bundle.getString("appid");
            position = bundle.getInt("position");
            System.out.println("position" + position);
        }


        imageView = (ImageView) findViewById(R.id.headerimg);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        Glide.with(this).load(MainActivity.gameDetails.get(position).get(2)).into(imageView);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        tusers = (TextView) findViewById(R.id.users);
        trequired_age = (TextView) findViewById(R.id.required_age);
        trank = (TextView) findViewById(R.id.rank);
        tabout_the_game = (ExpandableTextView) findViewById(R.id.about_the_game);
        tdetailed_description = (ExpandableTextView) findViewById(R.id.detailed_description);
        tshort_description = (ExpandableTextView) findViewById(R.id.short_description);
        tminimum = (ExpandableTextView) findViewById(R.id.minimum);
        trecommended = (ExpandableTextView) findViewById(R.id.recommended);


        ibabout = (ImageButton) findViewById(R.id.aboutexpand);
        ibdetailed_description = (ImageButton) findViewById(R.id.detailexpand);
        ibminimum_requirement = (ImageButton) findViewById(R.id.minimumexpand);
        ibrecomended_requirement = (ImageButton) findViewById(R.id.recommendedexpand);


        name = MainActivity.gameDetails.get(position).get(0);
        users = MainActivity.gameDetails.get(position).get(1);
        required_age = MainActivity.gameDetails.get(position).get(3);
        about_the_game = MainActivity.gameDetails.get(position).get(4);
        detailed_description = MainActivity.gameDetails.get(position).get(5);
        short_description = MainActivity.gameDetails.get(position).get(6);
        System.out.println("desc" + short_description);
        minimum = MainActivity.gameDetails.get(position).get(7);
        recommended = MainActivity.gameDetails.get(position).get(8);
        rank = MainActivity.gameDetails.get(position).get(9);


        toolbar.setTitle(name);
        toolbar.setTitleTextColor(Color.WHITE);
        tusers.setText(users);
        trequired_age.setText(required_age);
        trank.setText(rank);
        tabout_the_game.setText(Html.fromHtml(about_the_game));
        tdetailed_description.setText(Html.fromHtml(detailed_description));
        tshort_description.setText(Html.fromHtml(short_description));
        tminimum.setText(Html.fromHtml(minimum));
        trecommended.setText(Html.fromHtml(recommended));


        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(0, 0, 10, 10);
            Glide.with(this).load(MainActivity.game_images.get(position).get(i)).into(imageView);
            linearLayout.addView(imageView);
            imageView.getLayoutParams().height = 500;
            imageView.requestLayout();
            System.out.println("possss" + MainActivity.game_images.get(position).get(i));


        }

        ibabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("information", about_the_game);
                args.putString("title", "About the Game");
                Fragment detail = new BlankFragment();
                detail.setArguments(args);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.frameContainer, detail).addToBackStack(null).commit();

            }
        });

        ibdetailed_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("information", detailed_description);
                args.putString("title", "Detailed Description");
                Fragment detail = new BlankFragment();
                detail.setArguments(args);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.frameContainer, detail).addToBackStack(null).commit();


            }
        });

        ibminimum_requirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("information", minimum);
                args.putString("title", "Minimum Requirement");
                Fragment detail = new BlankFragment();
                detail.setArguments(args);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.frameContainer, detail).addToBackStack(null).commit();


            }
        });

        ibrecomended_requirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("information", recommended);
                args.putString("title", "Recommended Requirement");
                Fragment detail = new BlankFragment();
                detail.setArguments(args);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.frameContainer, detail).addToBackStack(null).commit();


            }
        });

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
