package com.example.sergi.cycloguardian.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.PhotoEntity;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Models.Photo;
import com.example.sergi.cycloguardian.Intro.IntroActivity;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Utils.Constants;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;


import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;


/**
 * Actividad principal de la aplicación
 * @author sergi
 */
public class MainActivity extends AppCompatActivity {

    Boolean wifiBol = false, bluetoothBol = false, gpsBol = false, cameraBol = false;
    ImageView imWifi, imBLE, imGPS, imCam;
    TextView textWifi, textBLE, textGPS, textCam, textDisp;
    Button btnStart;
    Drawer result;
    AppDataBase myDb;
    MyApplication myApplication;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    UserEntity myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imWifi = (ImageView) findViewById(R.id.imageViewWifi);
        imBLE = (ImageView) findViewById(R.id.imageViewBluetooth);
        imGPS = (ImageView) findViewById(R.id.imageViewGPS);
        imCam = (ImageView) findViewById(R.id.imageViewCamera);
        textWifi = (TextView) findViewById(R.id.textViewWifi);
        textBLE = (TextView) findViewById(R.id.textViewBluetooht);
        textGPS = (TextView) findViewById(R.id.textViewGPS);
        textCam = (TextView) findViewById(R.id.textViewCamera);

        //Get the DataBase
        myDb = AppDataBase.getAppDataBase(this);

        //Get the aplication object
        myApplication = ((MyApplication)this.getApplication());

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_home);

        //Get the actual user
        List<UserEntity> userEntityList = myDb.userDao().getAll();
        myUser = userEntityList.get(0);  //We take my user from the list, because we only have one user

        //Set the sesion user
        myApplication.mySession.setUserID(myUser.getIdUser());

        //Set the items of the drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.help).withIcon(GoogleMaterial.Icon.gmd_help);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.info).withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.logout).withIcon(GoogleMaterial.Icon.gmd_person_outline);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName(R.string.license).withIcon(GoogleMaterial.Icon.gmd_bookmark);
        //Account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withEmail(myUser.getEmail()).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withFullscreen(false)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(false)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item3,
                        item4,
                        item6,
                        new DividerDrawerItem(),
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                //drawerItem.withSetSelected(true);
                            }

                            if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                                startActivity(intent);
                                result.setSelection(1);

                            }

                            if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                                startActivity(intent);
                                result.setSelection(1);

                            }

                            if (drawerItem.getIdentifier() == 5) {
                                //TODO remove registry from DataBase
                                removeRegisterToDataBase(myDb, myUser);

                                //Remove all data of the user
                                removeSesionsUser(myDb);

                                //Change to login Activity
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

                            if (drawerItem.getIdentifier() == 6) {
                                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent);
                                result.setSelection(1);

                            }
                        }

                        return false;
                    }
                })
                .build();

        //set the back arrow in the toolbar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);


        //Creación del EventBus
        EventBus myEventBus = EventBus.getDefault();

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the use
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

//        Log.i("UserID", Integer.toString(myUser.getIdUser()));

        //Comprobamos si el wifi esta activado
        WifiManager wifiServ = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiServ.isWifiEnabled()){
            imWifi.setColorFilter(Color.GREEN);
            wifiBol = true;

            //Vamos a comprobar si la cámara está dentro del rango para concectarla directamente
            List<ScanResult> listWifi = wifiServ.getScanResults();
            for (ScanResult i: listWifi) {
                if (i.SSID.equals(Constants.SSID_CAMERA)) {
                    for(WifiConfiguration wifiConfiguration : wifiServ.getConfiguredNetworks()) {
                        if(wifiConfiguration.SSID != null && wifiConfiguration.SSID.equals("\"" + Constants.SSID_CAMERA + "\"")) {
                            wifiServ.disconnect();
                            wifiServ.enableNetwork(wifiConfiguration.networkId, true);
                            wifiServ.reconnect();
                            imCam.setColorFilter(Color.GREEN);
                            cameraBol = true;
                        }
                    }
                }
            }
        } else {
            imWifi.setColorFilter(Color.RED);
        }

        //Comprobamos si el bluetooh esta activado
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            bluetoothBol = true;
            imBLE.setColorFilter(Color.GREEN);
        } else {
            imBLE.setColorFilter(Color.RED);
        }

        //Comprobamos si la localización está activada
        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            }
            imGPS.setColorFilter(Color.GREEN);
            gpsBol = true;
        } else {
            imGPS.setColorFilter(Color.RED);
        }

        //La camara y el dispositivo no estan enlazados
        if (cameraBol == false) {
            imCam.setColorFilter(Color.RED);
        }

    }

    /**
     * Method to remove the sesions of the user
     * @param myDb the dataBAse
     */
    private void removeSesionsUser(AppDataBase myDb) {
        myDb.sessionDao().deleteAllSessions();
        myDb.incidenceDao().deleteAllIncidences();
        myDb.photoDao().deleteAllPhotos();

        //Remove all fotos
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CycloGuardian");


    }


    /**
     * Button to configure the Wifi
     * @param view
     */
    public void configureWifi(View view) {
        WifiManager wifiServ = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiServ.isWifiEnabled()){
            Toast.makeText(this, getText(R.string.wifi_enabled), Toast.LENGTH_LONG).show();
        } else {
            textWifi.setError(null);
            imWifi.setColorFilter(Color.YELLOW);
            wifiServ.setWifiEnabled(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(wifiServ.isWifiEnabled()){
                imWifi.setColorFilter(Color.GREEN);
                wifiBol = true;
            } else {
                Toast.makeText(this, getText(R.string.start_wifi_fail), Toast.LENGTH_LONG).show();
                imWifi.setColorFilter(Color.RED);
            }
        }
    }

    /**
     * Button to configure the GPS
     * @param view
     */
    public void configureGPS(View view) {
        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, getText(R.string.gps_enabled), Toast.LENGTH_LONG).show();
        } else {
            textGPS.setError(null);
            imGPS.setColorFilter(Color.YELLOW);
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);

            //Solicitamos los permisos al usuario
            if (!checkPermissions()) {
                startLocationPermissionRequest();
            }

            //Comprobamos si se ha activado el GPS
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(this, getText(R.string.gps_enabled_success), Toast.LENGTH_LONG).show();
                imGPS.setColorFilter(Color.GREEN);
                gpsBol = true;

            } else {
                Toast.makeText(this, getText(R.string.gps_enable_fail), Toast.LENGTH_LONG).show();
                imGPS.setColorFilter(Color.RED);
            }
        }


    }

    /**
     * Button to configure the Bluetooth
     * @param view
     */
    public void configureBluetooth(View view) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, getText(R.string.bluetooth_enabled), Toast.LENGTH_LONG).show();
        } else {
            textBLE.setError(null);
            imBLE.setColorFilter(Color.YELLOW);
            bluetoothAdapter.enable();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bluetoothAdapter.isEnabled()) {
                imBLE.setColorFilter(Color.GREEN);
                bluetoothBol = true;
                Toast.makeText(this, getText(R.string.bluetooth_enabled_success), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getText(R.string.bluetooh_enabled_fail), Toast.LENGTH_LONG).show();
                imBLE.setColorFilter(Color.RED);
            }
        }

    }

    /**
     * Button to configure the camera
     * @param view
     */
    public void linkCamera(View view) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Boolean cameraNetworkEnabled = false;
        if (wifi.getWifiState() == WIFI_STATE_ENABLED) {
            imCam.setColorFilter(Color.YELLOW);
            WifiConfiguration wc = new WifiConfiguration();

            //Vamos a comprobar si la cámara está dentro del rango para concectarla directamente
            List<ScanResult> listWifi = wifi.getScanResults();
            for (ScanResult i: listWifi) {
                if (i.SSID.equals(Constants.SSID_CAMERA)) {
                    cameraNetworkEnabled = true;
                }
            }

            if(cameraNetworkEnabled) {  //La cámara está encendida
                List<WifiConfiguration> list = wifi.getConfiguredNetworks();
                for( WifiConfiguration i : list ) {
                    if (i.SSID != null && i.SSID.equals("\"" + Constants.SSID_CAMERA + "\"")) {
                        wifi.disconnect();
                        wifi.enableNetwork(i.networkId, true);
                        wifi.reconnect();
                        cameraBol = true;
                        imCam.setColorFilter(Color.GREEN);
                        Toast notificacion = Toast.makeText(this, getText(R.string.concet_wifi) + i.SSID, Toast.LENGTH_LONG);
                        notificacion.show();

                        break;
                    }
                }

                //Si la red no está configurada
                if(!cameraBol) {
                    wc.SSID = "\"" + Constants.SSID_CAMERA + "\"";
                    wc.preSharedKey = "\""+ Constants.PWD_CAMERA +"\"";
                    wc.hiddenSSID = true;
                    wc.status = WifiConfiguration.Status.ENABLED;
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    int res = wifi.addNetwork(wc);

                    for( WifiConfiguration i : list ) {
                        if(i.SSID != null && i.SSID.equals("\"" + Constants.SSID_CAMERA + "\"")) {
                            wifi.disconnect();
                            wifi.enableNetwork(i.networkId, true);
                            wifi.reconnect();
                            textCam.setError(null);
                            cameraBol = true;
                            imCam.setColorFilter(Color.GREEN);
                            Toast notificacion = Toast.makeText(this, getText(R.string.concet_wifi) + wc.SSID, Toast.LENGTH_LONG);
                            notificacion.show();

                            break;
                        }
                    }

                    if (cameraBol == false) {
                        Toast.makeText(this, getText(R.string.conect_wifi_fail) + wc.SSID + getText(R.string.try_later), Toast.LENGTH_LONG).show();
                        imCam.setColorFilter(Color.RED);
                    }

                }



            } else {
                Toast notificacion = Toast.makeText(this, getText(R.string.wifi_camera_disconnect), Toast.LENGTH_LONG);
                notificacion.show();
            }
            // This is must be quoted according to the documentation
            // http://developer.android.com/reference/android/net/wifi/WifiConfiguration.html#SSID


        } else {
            //Mensaje para conectar el wifi
            Toast notificacion = Toast.makeText(this, getText(R.string.wifi_disabled), Toast.LENGTH_LONG);
            notificacion.show();
        }

    }


    //Para la comprobación de permisos del servicio de GPS

    /**
     * Check the GPS permissions
     * @return
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Set the GPS permissions
     */
    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
            Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    /**
     * Method to start the service and the monitoring
     * @param view
     */
    public void startService(View view) {
        //Iniciamos otra actividad
        if (cameraBol == true && bluetoothBol == true && gpsBol == true && wifiBol == true) {
            //Remove the photos of other sessions
            removePhotosLastSession();
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if(cameraBol == false) {
                textCam.setError(getText(R.string.configure_camera));
            }

            if (bluetoothBol == false) {
                textBLE.setError(getText(R.string.configure_BLE));
            }

            if (gpsBol == false) {
                textGPS.setError(getText(R.string.configure_GPS));
            }

            if (wifiBol == false) {
                textWifi.setError(getText(R.string.configure_wifi));
            }
        }


    }

    /**
     * Method to remove the user logged from the dataBase
     * @param appDataBase the database
     * @param userEntity the user to delete
     */
    private void removeRegisterToDataBase(AppDataBase appDataBase, UserEntity userEntity) {
        appDataBase.userDao().deleteUser(userEntity);

    }


    /**
     * Method to remove the photos in the storage
     */
    private void removePhotosLastSession() {
        File photoFile;
        AppDataBase myDb = AppDataBase.getAppDataBase(this.getApplicationContext());
        List<PhotoEntity> photoEntityList = myDb.photoDao().getAll();
        for (int i = 0; i < photoEntityList.size(); i++) {
            if (photoEntityList.get(i).getSyncronized() == true) {
                photoFile = Photo.getPhotoFile(photoEntityList.get(i).getNamePhoto());
                if (photoFile.exists())
                    photoFile.delete();

                myDb.photoDao().deletePhoto(photoEntityList.get(i));
            }
        }

    }


}
