package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Society> clubList;

    public ListViewAdapter(Context context, List<Society> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.i("check","pass7");
        View row = convertView;
        SocietyHolder holder = null;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        //Log.i("check","pass6");
        if (row == null) {
            row = inflater.inflate(R.layout.activity_club2, parent, false);
        }
        //Log.i("check","pass7");

        holder = new SocietyHolder();
        holder.society = clubList.get(position);
        holder.club_name = (TextView) row.findViewById(R.id.club_name);


        row.setTag(holder);
        holder.club_name.setText(holder.society.getClub_name());


        if (position % 2 == 0) {
            row.setBackgroundColor(Color.rgb(213, 229, 241));
        } else {
            row.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        return row;
    }

    public static class SocietyHolder {
        Society society;
        TextView club_name;
    }

    @Override
    public int getCount() {
        return clubList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
