package com.agamidev.indigo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.agamidev.indigo.Models.WayPoint;
import com.agamidev.indigo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by agamidev on 6/25/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<WayPoint> mArrayList;
    private RecyclerView mRecyclerView;
    private Context context;
    LatLng latLng;
    GoogleMap mMap;
    Marker marker;
    public ListAdapter (Context context, ArrayList<WayPoint> wayPointList, RecyclerView RV, ListView lv, GoogleMap m, Marker mm){
        this.context = context;
        mArrayList = wayPointList;
        mRecyclerView = RV;
        mMap = m;
        marker = mm;
    }



    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.places_item_list, parent, false);
        ListAdapter.MyViewHolder holder = new ListAdapter.MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pointPosition = mRecyclerView.indexOfChild(v);
                String pointName = mArrayList.get(pointPosition).getName();
                latLng = mArrayList.get(pointPosition).getLatLng();
//                Toast.makeText(context, latLng.toString(), Toast.LENGTH_SHORT).show();
//                mMap.addPolyline(new PolylineOptions().add());
                // create marker
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(pointName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

                // adding marker
//                marker = mMap.addMarker(markerOptions);




            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, int position) {
        String name = mArrayList.get(position).getName();
        holder.textView.setText(name);
//        holder.icon.setImageResource(img);

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        //        ImageView icon;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
//            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
