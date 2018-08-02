package com.example.sergi.cycloguardian.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergi.cycloguardian.Adapter.IncidenceAdapter;
import com.example.sergi.cycloguardian.Events.ThersholdEvent;
import com.example.sergi.cycloguardian.Models.Photo;
import com.example.sergi.cycloguardian.Models.Incidence;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author sergi
 */

public class FragmentGaleryList extends Fragment {
    View mView;
    //RecyclerView recyclerView;
    List<Incidence> incidenceList;
    MyApplication myApplication;
    Gallery simpleGallery;
    IncidenceAdapter incidenceAdapter;
    ImageView selectedImageView;
    TextView textViewName, textViewDate;

    public FragmentGaleryList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this); //Registro al bus de evnetos
        myApplication = ((MyApplication)getActivity().getApplication());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_galery_list, container, false);
        incidenceList = myApplication.mySession.getIncidenceArryList();

        //TextView
        textViewDate = (TextView) mView.findViewById(R.id.textViewDate);
        textViewName = (TextView) mView.findViewById(R.id.textViewName);

        //Pattern from the date
        String pattern = "d MMM yyyy  HH:mm:ss";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        //Gallery
        simpleGallery = (Gallery) mView.findViewById(R.id.simpleGallery); // get the reference of Gallery
        selectedImageView = (ImageView) mView.findViewById(R.id.selectedImageView);
        incidenceAdapter = new IncidenceAdapter(incidenceList, this.getContext());
        simpleGallery.setAnimationDuration(3000); // set 3000 milliseconds for animation duration between items of Gallery
        simpleGallery.setSpacing(5); // set space between the items of Gallery
        simpleGallery.setUnselectedAlpha(0.80f); // set 0.25 value for the alpha of unselected items of Gallery
        simpleGallery.setAdapter(incidenceAdapter); // set the adapter
        // perform setOnItemClickListener event on the Gallery
        simpleGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set the selected image in the ImageView
                //Glide.with(getContext()).load(incidenceList.get(position).getImage().getUrl()).into(selectedImageView);
                Picasso.with(getContext()).load(Photo.getPhotoFile(incidenceList.get(position).getImage().getNamePhoto())).into(selectedImageView);
                textViewName.setText(incidenceList.get(position).getImage().getNamePhoto());
                textViewDate.setText(simpleDateFormat.format(incidenceList.get(position).getTimeIncidence()));
                //

            }
        });


        return mView;

    }

    /**
     * Subscripción al evento para la recepción de nuevas imágenes
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEvent(final ThersholdEvent event) {
        //Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_SHORT).show();
      incidenceList = myApplication.mySession.getIncidenceArryList();
      incidenceAdapter = new IncidenceAdapter(incidenceList, this.getContext());
      simpleGallery.setAdapter(incidenceAdapter);
      //IncidenceAdapter incidenceAdapter = new IncidenceAdapter(incidenceList, R.layout.photo_list_row, getActivity());
      //recyclerView.setAdapter(incidenceAdapter);
    }



}
