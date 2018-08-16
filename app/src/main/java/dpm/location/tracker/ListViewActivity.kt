package dpm.location.tracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import dpm.location.tracker.room.LocationDb
import dpm.location.tracker.room.LocationRepository
import dpm.location.tracker.room.StoredLoctation
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.ArrayList

class ListViewActivity : AppCompatActivity() {

    var itemsAdapter : ArrayAdapter<StoredLoctation>? =null

    private val db: LocationDb by lazy {
      (application as LocationRepository).db
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        val list = ArrayList<StoredLoctation>()
        val location = StoredLoctation()

        val listView = findViewById(R.id.location_listview) as ListView
        itemsAdapter = ArrayAdapter<StoredLoctation>(this, R.layout.single_item)
        listView.setAdapter(itemsAdapter)


        doAsync {
            val products = db.locationDao().all
            uiThread {
                itemsAdapter?.addAll(products)
            }
        }
    }
}
