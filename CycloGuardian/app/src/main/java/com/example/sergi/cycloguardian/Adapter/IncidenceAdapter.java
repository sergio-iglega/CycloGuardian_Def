package com.example.sergi.cycloguardian.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sergi.cycloguardian.Models.Incidence;

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


    /**
     * Obtiene el número de imágenes
     * @return numeroImagenes
     */
    @Override
    public int getCount() {
        return incidenceList.size();
    }

    /**
     * Obtiene la imagen de la posicion i
     * @param i posicion
     * @return Objeto
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Obtiene un imageView de una determinada posicon
     * @param position
     * @param convertView
     * @param parent
     * @return imageView
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        // create a ImageView programmatically
        ImageView imageView = new ImageView(context);
        //imageView.setImageResource(incidenceList.get(position).getImage().getNamePhoto()); // set image in ImageView
        Glide.with(context).load(incidenceList.get(position).getImage().getUrl()).into(imageView);
        //Picasso.with(context).load(Photo.getPhotoFile(incidenceList.get(position).getImage().getNamePhoto())).into(imageView);
        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200)); // set ImageView param
        return imageView;
    }
}
