package com.example.sergi.cycloguardian.Services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.evernote.android.job.JobCreator;
import com.example.sergi.cycloguardian.Activities.StartActivity;
import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.IncidenceEntity;
import com.example.sergi.cycloguardian.Database.PhotoEntity;
import com.example.sergi.cycloguardian.Database.SessionEntity;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Events.BluetoothMessage;
import com.example.sergi.cycloguardian.Events.ConnectBLEEvent;
import com.example.sergi.cycloguardian.Events.DisconnectBLEEvent;
import com.example.sergi.cycloguardian.Events.SensorEvent;
import com.example.sergi.cycloguardian.Events.ThersholdEvent;
import com.example.sergi.cycloguardian.Files.Photo;
import com.example.sergi.cycloguardian.Job.SyncJobIncidence;
import com.example.sergi.cycloguardian.Job.SyncJobSesion;
import com.example.sergi.cycloguardian.Messages.IncomingCameraMessage;
import com.example.sergi.cycloguardian.Messages.OutcomingCameraMessagePhoto;
import com.example.sergi.cycloguardian.Messages.OutcomingCameraMessageRequest;
import com.example.sergi.cycloguardian.Models.Incidence;
import com.example.sergi.cycloguardian.Models.Session;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.Utils.Constants;
import com.example.sergi.cycloguardian.Utils.Parser;
import com.example.sergi.cycloguardian.Utils.Util;
import com.example.sergi.cycloguardian.Activities.MainActivity;
import com.example.sergi.cycloguardian.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polidea.rxandroidble2.NotificationSetupMode;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by sergi on 25/03/2018.
 */

public class MainService extends Service {


    private static final String PACKAGE_NAME =
            "com.example.sergi.cycloguardian.Services";

    private static final String TAG = MainService.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = (IBinder) new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    public Location mLocation = null;

    //NetworkTask
    NetworkTask networkTask;

    //Object myAplication
    MyApplication myApplication;

    //Object Incidence
    Incidence incidence;

    Boolean hacerfoto = true;


    public static String HM_RX_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private RxBleDevice bleDevice;
    private Disposable connectionDisposable;
    private RxBleConnection.RxBleConnectionState currentState;


    public MainService() {
    }

    @Override
    public void onCreate() {

        EventBus.getDefault().register(this); //Registro al bus de evnetos

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        myApplication = ((MyApplication)this.getApplication());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        myApplication.mySession.setSessionUUID(UUID.randomUUID());

        createLocationRequest();
        getLastLocation();
        //randomDistanceGenerator();
        //hacerFoto();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void connect(ConnectBLEEvent connectBLEEvent){

        // We create a bluetooth ble device from MAC Address
        String macAddress = connectBLEEvent.getMacAddress();
        bleDevice = MyApplication.getRxBleClient(this).getBleDevice(macAddress);

        // We listen for changes in the state of the device
        bleDevice.observeConnectionStateChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxBleConnectionState -> currentState = rxBleConnectionState,
                        throwable -> Log.d("MainService",throwable.getMessage()));

        connectionDisposable = bleDevice.establishConnection(false)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::dispose)// establish the connection
                .flatMap(new Function<RxBleConnection, ObservableSource<Observable<byte[]>>>() {
                    @Override
                    public ObservableSource<Observable<byte[]>> apply(RxBleConnection rxBleConnection) throws Exception {
                        return rxBleConnection.setupNotification(UUID.fromString(HM_RX_TX), NotificationSetupMode.COMPAT);
                    }
                })
                .subscribe(bytes -> {
                    bytes.subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) throws Exception {
                            String msg = new String(bytes, "UTF-8");
                                    //Log.d("MainService", msg);
                            EventBus.getDefault().post(new BluetoothMessage(msg));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d("MainService", throwable.getMessage());
                        }
                    });
                }, throwable -> {
                    Log.d("MainService", throwable.getMessage());
                });

    }

    private void dispose(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void disconnect(DisconnectBLEEvent disconnectBLEEvent){
        Log.i("BLE", "disconnecting");
        connectionDisposable.dispose();
        //connectionDisposable = null;
        dispose();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBluetoothMessage(BluetoothMessage bluetoothMessage){

        if (bluetoothMessage.getSensor1() != 0 && bluetoothMessage.getSensor2() != 0) {
            //Add to the queue
            myApplication.mySession.getSensorDatesQueue().add(bluetoothMessage.getSensor1());
            myApplication.mySession.getSensorDatesQueue2().add(bluetoothMessage.getSensor2());

            //Notify event
            SensorEvent sensorEvent = new SensorEvent();
            sensorEvent.setSensor1(bluetoothMessage.getSensor1());
            sensorEvent.setSensor2(bluetoothMessage.getSensor2());
            EventBus.getDefault().post(sensorEvent);

            //Check queue
            checkQueue(bluetoothMessage.getSensor1(), bluetoothMessage.getSensor2());
        }

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        Log.i(TAG, "Calling to connect...");
        connect(new ConnectBLEEvent("7C:01:0A:5F:40:45"));

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Util.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            /*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             */
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service stopped");
        mServiceHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Util.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), MainService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Util.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Util.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Util.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, MainService.class);

        CharSequence text = Util.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, getString(R.string.launch_activity),
                        activityPendingIntent)
                .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle(Util.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);

        mLocation = location;

        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    public void randomDistanceGenerator() {
        final float minX = 1.0f;  //To calculate the random distance
        final float maxX = 4.0f;
        final Random rand = new Random();
        Log.i("GNERATOR", "Generating ramdon numbers");

       /* SensorEvent sensorEvent = new SensorEvent();
        EventBus.getDefault().post(sensorEvent);*/
        //Thread to generate the random numbers
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    float finalXS1;
                    float finalXS2;
                    finalXS1 = rand.nextFloat() * (maxX - minX) + minX;
                    finalXS2 = rand.nextFloat() * (maxX - minX) + minX;
                    //Add to the queue
                    myApplication.mySession.getSensorDatesQueue().add(finalXS1);
                    //Notificación de un nuevo evento de datos
                    SensorEvent sensorEvent = new SensorEvent();
                    sensorEvent.setSensor1(finalXS1);
                    sensorEvent.setSensor2(finalXS2);
                    EventBus.getDefault().post(sensorEvent);
                    //check the date
                    checkQueue(finalXS1,finalXS2);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();


    }

    private void checkQueue(float dateSensor1, float dateSensor2) {
        long delayFoto = 2000;
        if(dateSensor1 <= myApplication.mySession.getLimitOvertaking() || dateSensor2 <= myApplication.mySession.getLimitOvertaking()) {
            incidence = new Incidence();
            incidence.setDistanceSensor(dateSensor1+dateSensor2/2);
            Timer timer = new Timer();

            Log.i("TIMER", String.valueOf(hacerfoto));
            if (hacerfoto) {
                hacerfoto = false;
                hacerFoto();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        hacerfoto = true;
                    }
                }, delayFoto);

            }
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void hacerFoto() {
        OutcomingCameraMessagePhoto msg = new OutcomingCameraMessagePhoto(Constants.MSG_ID_TAKE_PHOTO); //Constructor del mensaje
        networkTask = new NetworkTask();
        networkTask.execute(msg);
    }

    public class NetworkTask extends AsyncTask<OutcomingCameraMessagePhoto, byte[], Photo> {
        ThersholdEvent thersholdEvent; //Para la creación del evento
        Socket nsocket; //Network Socket
        InputStream nis; //Network Input Stream
        OutputStream nos; //Network Output Stream

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "onPreExecute");
        }

        @Override
        protected Photo doInBackground(OutcomingCameraMessagePhoto... params) { //This runs on a different thread
            String result;
            String ruta = null;
            String fileName = null;
            String url = null;
            Parser p = new Parser();
            Photo photo = null;
            try {

                OutcomingCameraMessagePhoto msgPhoto = params[0];
                OutcomingCameraMessageRequest msg = new OutcomingCameraMessageRequest(Constants.MSG_ID_REQUEST);

                //TODO INICIAR LA CONEXIÓN
                Log.i("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress(Constants.IP_CAMARA, Constants.PUERTO_CAMARA);
                nsocket = new Socket();
                try {
                    nsocket.connect(sockaddr, Constants.TIMEOUT_SOCKET); //10 segundos de timeout
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nsocket.isConnected()) {
                    nis = nsocket.getInputStream();  //Creamos los streams
                    nos = nsocket.getOutputStream();
                    Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                    Log.i("AsyncTask", "doInBackground: Waiting for inital data...");
                    //TODO OBTENER EL TOKEN
                    //Enviamos el mensaje inicial para obtener el token
                    String cmd = msg.componerMensajePhoto();  //Creación de la cadena inicial
                    nos.write(cmd.getBytes());
                    Log.i("AsyncTask", "doInBackground: Waiting for reply message..");
                    byte[] buffer = new byte[Constants.TAMANO_BUFFER];
                    int read = nis.read(buffer, 0, Constants.TAMANO_BUFFER); //This is blocking
                    byte[] tempdata = new byte[read];
                    System.arraycopy(buffer, 0, tempdata, 0, read);
                    publishProgress(tempdata);
                    //Parsear la cadena recibida
                    IncomingCameraMessage inMsgReply = p.parsearMensaje(tempdata);
                    if(inMsgReply == null) {
                        //La acción de tomar foto ha fallado
                        photo = new Photo(null, null);
                        photo = null;
                    }

                    //TODO SETEAR EL TOKEN EN EL MSG
                    //OutcomingCameraMessage msgPhoto = new OutcomingCameraMessage(769, inMsgReply.paramToken);
                    msgPhoto.setToken(inMsgReply.paramToken); //Modificamos el token del mensaje
                    String cmdPhoto = msgPhoto.componerMensajePhoto();

                    //TODO ENVIAR MSG
                    nos.write(cmdPhoto.getBytes());

                    //TODO ESPERAR RESPUESTA --> CON TIMEOUT
                    byte[] inputData = new byte[1024];
                    int readCount = readInputStreamWithTimeout(nis, inputData, Constants.TIMEOUT_MENSAJE);  //TimeOut de 6 segundos
                    byte[] tempdataPhoto = new byte[readCount];
                    System.arraycopy(inputData, 0, tempdataPhoto, 0, readCount);
                    publishProgress(tempdataPhoto);

                    //Comprobar los mensajes recibidos y determinar si ha habido una respuesta correcta
                    //Primeramente parseamos las cadenas
                    int i;
                    String cadena = new String(tempdataPhoto);
                    String delimiter = "[{]";
                    String[] parts = cadena.split(delimiter);
                    for (i = 0; i < parts.length; i++){  //Recorremos el array de partes
                        //Añadimos el caracter que anteriormente hemos eliminado
                        parts[i] = "{" + parts[i];

                    }

                    //Comprobation of the returned msg
                    if(parts[0].equals("{")) {
                        parts = Arrays.copyOfRange(parts, 1, parts.length);
                    }
                    IncomingCameraMessage[] msgsRespond = new IncomingCameraMessage[i];

                    for (i = 0; i < parts.length; i++){  //Recorremos el array de partes y cremos los distintos mensajes
                        msgsRespond[i] = p.parsearMensaje(parts[i].getBytes());
                        //msgsRespond[i].parserRespondMessage();
                        if(msgsRespond[i].type != null) {
                            if (msgsRespond[i].type.contains("photo_taken")) {
                                if (msgsRespond[i].param != null){
                                    ruta = msgsRespond[i].param;
                                    fileName = p.extractFileName(ruta);
                                    url = p.generateFileURL(fileName);
                                    photo = new Photo(url, fileName);
                                }
                            }
                        }
                    }



                    //TODO CERRAR SOCKETS Y STREAMS
                    nis.close();
                    nos.close();
                    nsocket.close();

                }

                //TODO RETURN OK | ERROR

            } catch (IOException e) {
                e.printStackTrace();
            }

            return photo;  //Devolvemos la foto con la url y el nombre al método PostExecute
        }

        public int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
                throws IOException {
            int bufferOffset = 0;
            long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
            while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
                int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
                // can alternatively use bufferedReader, guarded by isReady():
                int readResult = is.read(b, bufferOffset, readLength);
                if (readResult == -1) break;
                bufferOffset += readResult;
            }
            return bufferOffset;
        }


        @Override
        protected void onProgressUpdate(byte[]... values) {
            String str = null;
            if (values.length > 0) {
                Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                str = new String(values[0]);
            }
        }

        @Override
        protected void onPostExecute(final Photo photo) {
            int indexIncidence = 0;
            if (photo != null) {
                if (photo.getUrl() != null) {
                    Log.i("AsyncTask", "onPostExecute: Completed.");


                    //TODO obtain location
                    getLastLocation();
                    if (mLocation != null) {
                        Log.i("LOC", String.valueOf(mLocation.getLatitude()) + " " + String.valueOf(mLocation.getLongitude()));
                        incidence.setPosicion(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
                    } else {
                        incidence.setPosicion(new LatLng(0.0, 0.0));
                    }

                    Glide.with(getBaseContext())
                            .load(photo.getUrl())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(300,300) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    saveToInternalStorage(resource, photo.getNamePhoto());

                                }
                            });

                    //Obtain the path of the photo from the filesystem
                    String rutaPhoto = Photo.obtainPath(photo.getNamePhoto());
                    if (rutaPhoto != null)
                        photo.setRutaInterna(rutaPhoto);

                    //Obtain the uuid of the photo
                    photo.setUuidPhoto(UUID.randomUUID());


                    //Set the incidence UUID and the session UUID
                    incidence.setMyUUID(UUID.randomUUID());
                    incidence.setSessionUUID(myApplication.mySession.getSessionUUID());

                    //Set in the photo the incidence UUID
                    photo.setIncidenceUUID(incidence.getMyUUID());

                    //Add the photo to the indidence and the date
                    incidence.setImage(photo);
                    incidence.setTimeIncidence(new Date());



                    //TODO añadir incicencia a la session
                    myApplication.mySession.getIncidenceArryList().add(incidence);

                    //Obtain the index of the
                    for (int i = 0; i < myApplication.mySession.getIncidenceArryList().size(); i++) {
                        if (photo.getNamePhoto().equals(myApplication.mySession.getIncidenceArryList().get(i).getImage().getNamePhoto())) {
                            indexIncidence = i;
                        }
                    }


                    //Create the event and post to the eventbus
                    thersholdEvent = new ThersholdEvent();
                    thersholdEvent.setPosIncidence(indexIncidence);
                    EventBus.getDefault().post(thersholdEvent);

                    //Save to database
                    AppDataBase myDB = AppDataBase.getAppDataBase(getBaseContext());
                    saveSessionToDataBase(myDB, myApplication.mySession);
                    saveIncidenceToDataBase(myDB, incidence);

                    //JOb
                    SyncJobIncidence.scheduleJob();

                } else {
                    Log.i("AsyncTask", "onPostExecute: Something ocurred.");
                    //textStatus.setText("No se ha podido realizar la foto");
                }
            } else {
                Log.i("AsyncTask", "onPostExecute: Something ocurred.");
                //textStatus.setText("No se ha podido realizar la foto");
            }
        }


        private void saveToInternalStorage(Bitmap bitmap, String name){
            //imageView.setImageBitmap(bitmap);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/CycloGuardian");


            if(!myDir.exists())
                myDir.mkdirs();

            File file = new File (myDir, name);
            if (file.exists ()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void saveSessionToDataBase(AppDataBase appDataBase, Session mySession) {
            SessionEntity oldSession = null;
            oldSession = appDataBase.sessionDao().getSessionByUUID(mySession.getSessionUUID().toString());
            if (oldSession == null) {  //There isnt the session
                Log.i("DB", "Create Session in DB");
                SessionEntity sessionEntity = new SessionEntity();
                sessionEntity.setUuid(mySession.getSessionUUID().toString());
                sessionEntity.setUserId(mySession.getUserID());  //TODO poner usuario real

                //Save to database
                appDataBase.sessionDao().insertSession(sessionEntity);
            } else {
                Log.i("DB", "The session already exists");
            }
        }

        private void saveIncidenceToDataBase(AppDataBase appDataBase, Incidence myIncidence) {


            IncidenceEntity incidenceEntity = new IncidenceEntity();
            incidenceEntity.setIdSession(myIncidence.getSessionUUID().toString());
            incidenceEntity.setLatitude(myIncidence.getPosicion().latitude);
            incidenceEntity.setLongitude(myIncidence.getPosicion().longitude);
            incidenceEntity.setUuid(myIncidence.getMyUUID().toString());
            incidenceEntity.setTimeIncidence(myIncidence.getTimeIncidence().toString());
            incidenceEntity.setDistanceSensor(myIncidence.getDistanceSensor());

            //Save to database
            appDataBase.incidenceDao().insertIncidence(incidenceEntity);

            //Save the photo to database
            savePhotoToDataBase(appDataBase, myIncidence.getImage());


        }

        private void savePhotoToDataBase(AppDataBase appDataBase, Photo myPhoto) {

            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setNamePhoto(myPhoto.getNamePhoto());
            photoEntity.setRutaPhoto(myPhoto.getRutaInterna());
            photoEntity.setUuidIncidence(myPhoto.getIncidenceUUID().toString());
            photoEntity.setUuidPhoto(myPhoto.getUuidPhoto().toString());

            //Save the photo to databases
            appDataBase.photoDao().insertPhoto(photoEntity);

        }


    }

}