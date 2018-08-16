package dpm.location.tracker.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.support.annotation.LayoutRes
import dpm.location.tracker.R

class LocationAdapter(context: Context?, resource: Int) : ArrayAdapter<StoredLoctation>(context, resource) {
    private var mContext: Context?=null
    private var locationList = ArrayList<StoredLoctation>()

    fun MovieAdapter(context: Context, @LayoutRes list: ArrayList<StoredLoctation>) {
        mContext = context
        locationList = list
    }

    override fun getView(position: Int,  convertView: View?, parent: ViewGroup): View? {
        var listItem = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false)

        val currentLocation = locationList.get(position)

        //val textbox = listItem?.findViewById(R.id.text1) as TextView\
//            text1.setText("")


        return listItem
    }

}
