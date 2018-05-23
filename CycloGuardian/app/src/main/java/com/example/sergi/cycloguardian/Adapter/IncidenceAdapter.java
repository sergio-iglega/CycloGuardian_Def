package com.example.sergi.cycloguardian.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sergi.cycloguardian.Files.Photo;
import com.example.sergi.cycloguardian.Models.Incidence;
import com.example.sergi.cycloguardian.R;

import java.util.List;

/**
 * Created by sergi on 14/04/2018.
 */

public class IncidenceAdapter extends BaseAdapter{

    public List<Incidence> incidenceList;
    private Context context;

    //Constructor de la clase
    public IncidenceAdapter(List<Incidence> incidenceList, Context context) {
        this.context = context;
        this.incidenceList = incidenceList;
    }

    //Constructor sin argumentos
    public IncidenceAdapter() {

    }


    @Override
    public int getCount() {
        return incidenceList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {

        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);
        //imageView.setImageResource(incidenceList.get(position).getImage().getNamePhoto()); // set image in ImageView
        Glide.with(context).load(incidenceList.get(position).getImage().getUrl()).into(imageView);
        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200)); // set ImageView param
        return imageView;
    }
}
