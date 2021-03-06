package dpm.location.tracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import dpm.location.tracker.room.LocationAdapter
import dpm.location.tracker.room.LocationDb
import dpm.location.tracker.room.LocationRepository
import dpm.location.tracker.room.StoredLoctation
import kotlinx.android.synthetic.main.content_job_service_demo.delete_data
import kotlinx.android.synthetic.main.content_job_service_demo.show_results
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DateFormat
import java.util.ArrayList
import java.util.Date

class JobServiceDemoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var locationMsg: TextView? = null

    // as google doc says
    // Handler for incoming messages from the service.
    private var listView: ListView? = null
    private val mAdapter: LocationAdapter? = null
    private var mHandler: IncomingMessageHandler? = null

    private val db: LocationDb
        get() = (application as LocationRepository).db

    internal inner class IncomingMessageHandler : Handler() {

        override fun handleMessage(msg: Message) {
            Log.i(TAG, "handleMessage..." + msg.toString())

            super.handleMessage(msg)

            when (msg.what) {
                LocationUpdatesService.LOCATION_MESSAGE -> {
                    val obj = msg.obj as Location
                    val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
                    locationMsg?.text = "LAT :  " + obj.latitude + "\nLNG : " + obj.longitude + "\n\n" + obj.toString() + " \n\n\nLast updated- " + currentDateTimeString
                }
            }
        }
    }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_job_service_demo)
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            //room

            //  List<StoredLoctation> products = LocationRepository.get().getDB().locationDao().getAll();

            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            val toggle = ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer.addDrawerListener(toggle)
            toggle.syncState()

            val navigationView = findViewById<NavigationView>(R.id.nav_view)
            navigationView.setNavigationItemSelectedListener(this)

            drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

            locationMsg = findViewById(R.id.location)

            mHandler = IncomingMessageHandler()
            requestPermissions()
            bindListeners()
        }

        override fun onStart() {
            super.onStart()
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler = null
        }

        /**
         * Callback received when a permissions request has been completed.
         */
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                grantResults: IntArray) {
            Log.i(TAG, "onRequestPermissionResult")
            if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
                if (grantResults.size <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                    finish()
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // can be schedule in this way also
                    //  Utils.scheduleJob(this, LocationUpdatesService.class);
                    //doing this way to communicate via messenger
                    // Start service and provide it a way to communicate with this class.
                    val startServiceIntent = Intent(this, LocationUpdatesService::class.java)
                    val messengerIncoming = Messenger(mHandler)
                    startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming)
                    startService(startServiceIntent)
                } else {
                    // Permission denied.
                    finish()
                }
            }
        }
    fun bindListeners(){
        delete_data.setOnClickListener { deleteDb() }
        show_results.setOnClickListener { showResults() }
    }

    private fun showResults() {
        startActivity(Intent(this, ListViewActivity::class.java))
//        doAsync {
//            val products = db.locationDao().all
//
//            uiThread {
//
//            }
//        }



    }

    private fun deleteDb() {
        doAsync {
            //val products = db.locationDao().nukeTable()
            uiThread {

            }

        }
        Toast.makeText(applicationContext,"Database deleted",Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.job_service_demo, menu)
            return true
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // Handle navigation view item clicks here.
            val id = item.itemId

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            val id = item.itemId


            return if (id == R.id.action_settings) {
                true
            } else super.onOptionsItemSelected(item)
        }

        private fun requestPermissions() {
            val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)

            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            if (shouldProvideRationale) {
                Log.i(TAG, "Displaying permission rationale to provide additional context.")
                // Request permission
                ActivityCompat.requestPermissions(this@JobServiceDemoActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE)
            } else {
                Log.i(TAG, "Requesting permission")
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(this@JobServiceDemoActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS_REQUEST_CODE)
            }
        }

        companion object {

            private val TAG = JobServiceDemoActivity::class.java.simpleName

            /**
             * Code used in requesting runtime permissions.
             */
            private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

            val MESSENGER_INTENT_KEY = "msg-intent-key"

            private val DATABASE_NAME = "LocaionDB"
        }
    }
