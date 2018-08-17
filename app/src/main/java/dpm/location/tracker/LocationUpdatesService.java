package dpm.location.tracker;

import static dpm.location.tracker.JobServiceDemoActivity.Companion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import dpm.location.tracker.room.LocationDb;
import dpm.location.tracker.room.LocationRepository;
import dpm.location.tracker.room.StoredLoctation;
import java.util.ArrayList;


/**
 * location update service continues to running and getting location information
 */
public class LocationUpdatesService extends JobService implements LocationUpdatesComponent.ILocationProvider {

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    public static final int LOCATION_MESSAGE = 9999;

    private LocationUpdatesComponent locationUpdatesComponent;

    private Messenger mActivityMessenger;

    public LocationUpdatesService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "created...............");

        locationUpdatesComponent = new LocationUpdatesComponent(this);

        locationUpdatesComponent.onCreate(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy....");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLocationUpdate(Location location) {

        sendMessage(LOCATION_MESSAGE, location);
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        super.onRebind(intent);
    }

    private static final String CHANNEL_ID = "location_updates";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location Updated";
            String description = "Location Tracking testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Log.i(TAG, "onStartCommand Service started");
        if (intent != null) {
            mActivityMessenger = intent.getParcelableExtra(Companion.getMESSENGER_INTENT_KEY());
        }
        //hey request for location updates
        locationUpdatesComponent.onStart();

      /*  HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());*/

        //This thread is need to continue the service running
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    Log.i(TAG, "thread... is running...");
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob....");
//        Utils.scheduleJob(getApplicationContext(), LocationUpdatesService.class);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob....");

        locationUpdatesComponent.onStop();

        return false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        return true; // Ensures onRebind() is called when a client re-binds.
    }

    private void saveLocation(final StoredLoctation loc) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                LocationDb db = ((LocationRepository) getApplicationContext()).getDB();

                ArrayList<StoredLoctation> list = new ArrayList<StoredLoctation>();
                list.add(loc);
                db.locationDao().insertAll(list);
            }
        });

    }

    private int mCountBg = 0;
    private int mCountFg = 0;

    /**
     * send message by using messenger
     */
    private void sendMessage(int messageID, final Location location) {

        StoredLoctation loc = new StoredLoctation();
        loc.setLat(location.getLatitude());
        loc.setLon(location.getLongitude());
        loc.setTimestamp(location.getTime());
        loc.setBackground(mActivityMessenger == null);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle("Location Updated")
                .setOnlyAlertOnce(true)
                .setWhen(loc.getTimestamp())
                .setContentText(String.format("%d --  %s", mActivityMessenger == null ? mCountBg : mCountFg, loc.toString()))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(mActivityMessenger == null ? mCountBg : mCountFg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(5555, mBuilder.build());

        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.
        if (mActivityMessenger == null) {
            mCountBg++;
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }

        mCountFg++;
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = location;
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }
}