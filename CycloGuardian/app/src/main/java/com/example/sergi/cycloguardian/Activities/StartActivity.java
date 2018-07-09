package com.example.sergi.cycloguardian.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.SessionEntity;
import com.example.sergi.cycloguardian.Events.DisconnectBLEEvent;
import com.example.sergi.cycloguardian.Fragments.FragmentGallery;
import com.example.sergi.cycloguardian.Fragments.FragmentGaleryList;
import com.example.sergi.cycloguardian.Fragments.FragmentGraph;
import com.example.sergi.cycloguardian.Fragments.FragmentMap;
import com.example.sergi.cycloguardian.Job.SyncJobSesion;
import com.example.sergi.cycloguardian.Models.Session;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Services.MainService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    // A reference to the service used to get location updates.
    private MainService mService = null;
    Chronometer chronometerSession;
    MyApplication myApplication;

    public StartActivity getStartActivity() {
        return startActivity;
    }

    public void setStartActivity(StartActivity startActivity) {
        this.startActivity = startActivity;
    }

    StartActivity startActivity;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //Iniciamos los fragments
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentGraph(), getString(R.string.title_fragment_graph));
        adapter.addFragment(new FragmentMap(), getString(R.string.title_fragment_map));
        adapter.addFragment(new FragmentGallery(), getString(R.string.title_fragment_image));
        adapter.addFragment(new FragmentGaleryList(), getString(R.string.title_fragment_gallery));
        viewPager.setAdapter(adapter);
        setStartActivity(this);
        myApplication = ((MyApplication)this.getApplication());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        startService(new Intent(this, MainService.class));
        //Start the service
      /*  bindService(new Intent(this, MainService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);*/

        //Set the time of session start
        myApplication.mySession.setSessionStart(new Date());

        //Start the chronometrer
        chronometerSession = (Chronometer) findViewById(R.id.chronometerSession);
        chronometerSession.setBase(SystemClock.elapsedRealtime());
        chronometerSession.start();

    }


    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void stopSession(View view) {
        long elapsedMillis = 0;

        //Todo desconectar Bluetooth
        EventBus.getDefault().post(new DisconnectBLEEvent());

        //TODO detener el servicio
        stopService(new Intent(this, MainService.class));

        //TODO detener timestamp y setearlo en la Session
        myApplication.mySession.setSessionEnd(new Date());
        elapsedMillis = SystemClock.elapsedRealtime() - chronometerSession.getBase();
        chronometerSession.stop();
        myApplication.mySession.setTimeElapsedSession(elapsedMillis);

        //Obtain the instance of the DataBase and update the session
        AppDataBase myDB = AppDataBase.getAppDataBase(getStartActivity());
        updateSession(myDB, myApplication.mySession);



        //TODO programar un trabajo subir Session al servidor (jobs)
        SyncJobSesion.scheduleJob();

        //Change activity
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updateSession(AppDataBase appDataBase, Session session) {
        SessionEntity sessionEntity;
        sessionEntity = appDataBase.sessionDao().getSessionByUUID(session.getSessionUUID().toString());

        if (sessionEntity == null) {
            sessionEntity = new SessionEntity();
            sessionEntity.setUserId(session.getUserID());  //TODO poner usuario real
            sessionEntity.setUuid(session.getSessionUUID().toString());
            sessionEntity.setTimeElapssed(session.getTimeElapsedSession());
            sessionEntity.setSessionStart(session.getSessionStart().toString());
            sessionEntity.setSessionEnd(session.getSessionEnd().toString());

            //Save to the DB
            appDataBase.sessionDao().insertSession(sessionEntity);

        } else {
            //Set the new data
            sessionEntity.setSessionEnd(session.getSessionEnd().toString());
            sessionEntity.setSessionStart(session.getSessionStart().toString());
            sessionEntity.setTimeElapssed(session.getTimeElapsedSession());
            //Update the database
            appDataBase.sessionDao().updateSession(sessionEntity);
        }


    }

}
