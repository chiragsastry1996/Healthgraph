package com.acharya.healthgraph.GameList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.acharya.healthgraph.Details.BlankFragment;
import com.acharya.healthgraph.Details.Details;
import com.acharya.healthgraph.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder>{
    private static final String TAG = PersonAdapter.class.getSimpleName();

    private Context mContext;
    List<Person> personList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,txtStatusMsg;
        public ImageView feedImage1;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            txtStatusMsg = (TextView) view.findViewById(R.id.txtStatusMsg);
            feedImage1 = (ImageView) view.findViewById(R.id.feedImage1);
            button = (Button)view.findViewById(R.id.button2);
        }
    }

    public PersonAdapter(Context mContext, int i, List<Person> albumList) {
        this.mContext = mContext;
        this.personList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.name.setText(person.getName());
        holder.txtStatusMsg.setText(person.getCaption() + " online");

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Details.class);
                i.putExtra("appid",MainActivity.games[holder.getAdapterPosition()]);
                i.putExtra("position",holder.getAdapterPosition());
                mContext.startActivity(i);
            }
        });

        // loading album cover using Glide library
        Glide.with(mContext).load(person.getImage()).into(holder.feedImage1);
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }



}
