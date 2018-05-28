package com.example.sergi.cycloguardian.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.UserEntity;
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

import java.util.List;

import de.greenrobot.event.EventBus;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;


public class MainActivity extends AppCompatActivity {

    Boolean wifiBol = false, bluetoothBol = false, gpsBol = false, cameraBol = false, dispositivoBool = false;
    ImageView imWifi, imBLE, imGPS, imCam, imDisp;
    TextView textWifi, textBLE, textGPS, textCam, textDisp;
    Button btnStart;
    Drawer result;
    AppDataBase myDb;
    private SharedPreferences prefs;
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
        imDisp = (ImageView) findViewById(R.id.imageViewDispositivo);
        textWifi = (TextView) findViewById(R.id.textViewWifi);
        textBLE = (TextView) findViewById(R.id.textViewBluetooht);
        textGPS = (TextView) findViewById(R.id.textViewGPS);
        textCam = (TextView) findViewById(R.id.textViewCamera);
        textDisp = (TextView) findViewById(R.id.textViewDevice);

        //Get the shared preferences
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //Get the DataBase
        myDb = AppDataBase.getAppDataBase(this);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MAIN ACTIVITY");

        //Get the actual user
        myUser = myDb.userDao().getUserById(getUserIdSharedPreferences());

        //Set the items of the drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.settings).withIcon(GoogleMaterial.Icon.gmd_settings);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.help).withIcon(GoogleMaterial.Icon.gmd_help);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.info).withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName(R.string.logout).withIcon(GoogleMaterial.Icon.gmd_person_outline);
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
                        item2,
                        item3,
                        item4,
                        new DividerDrawerItem(),
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {

                            }

                            if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(MainActivity.this, SettignsActivity.class);
                                startActivity(intent);
                            }

                            if (drawerItem.getIdentifier() == 3) {

                            }

                            if (drawerItem.getIdentifier() == 4) {

                            }

                            if (drawerItem.getIdentifier() == 5) {
                                //TODO remove registry from DataBase
                                removeRegisterToDataBase(myDb, myUser);

                                //Remove the sharedPreferences
                                removeSharedPreferences();

                                //Change to login Activity
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

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

        Log.i("UserID", Integer.toString(myUser.getIdUser()));

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
        if (cameraBol == false && dispositivoBool == false) {
            imCam.setColorFilter(Color.RED);
            imDisp.setColorFilter(Color.RED);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

        if (cameraBol == true) {
            menu.removeItem(R.id.linkCamera);
        }

        if (wifiBol == true) {
            menu.removeItem(R.id.conWifi);
        }

        if (bluetoothBol == true) {
            menu.removeItem(R.id.conBLE);
        }

        if(gpsBol == true) {
            menu.removeItem(R.id.conGPS);
        }

        if(dispositivoBool == true) {
            menu.removeItem(R.id.linHardware);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.conWifi: {
                    configureWifi();
                break;
            }
            case R.id.conGPS: {
                    configureGPS();
                break;
            }
            case R.id.conBLE: {
                    configureBluetooth();
                break;
            }

            case R.id.linkCamera: {
                    linkCamera();
                break;
            }

            case R.id.linHardware: {
                    linkDevice();
                break;
            }
        }
        return true;
    }

    private void configureWifi() {
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

    private void configureGPS() {
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

    private void configureBluetooth() {
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

    private void linkCamera() {
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

    private void linkDevice() {
        textDisp.setError(null);
        dispositivoBool = true;
    }

    //Para la comprobación de permisos del servicio de GPS
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
            Constants.REQUEST_PERMISSIONS_REQUEST_CODE);
    }



    public void startService(View view) {
        //Iniciamos otra actividad
        if (cameraBol == true && dispositivoBool == true && bluetoothBol == true && gpsBol == true && wifiBol == true) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if(cameraBol == false) {
                textCam.setError(getText(R.string.configure_camera));
            }

            if (dispositivoBool == false) {
                textDisp.setError(getText(R.string.configure_device));
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

    private void removeSharedPreferences() {
        prefs.edit().clear().apply();
    }

    private void removeRegisterToDataBase(AppDataBase appDataBase, UserEntity userEntity) {
        appDataBase.userDao().deleteUser(userEntity);

    }

    private int getUserIdSharedPreferences() {
        return prefs.getInt("idUser", -1);
    }
}
