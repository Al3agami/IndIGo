package com.agamidev.indigo.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.agamidev.indigo.Activitites.MainActivity;
import com.agamidev.indigo.Models.WayPoint;
import com.agamidev.indigo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by agamidev on 6/23/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<WayPoint> mArrayList;
    private RecyclerView mRecyclerView;
//    private Context context;
//    DrawerLayout drawerLayout;
//    private ListView mListView;
    private ArrayList<WayPoint> polyPoints;
    private WayPoint startWayPoint;
    private WayPoint desiredWayPoint;
    private LatLng latLng;
    private LatLng currentLatLng;
    private LatLng startLatLng;
    private Marker myMarker;
    private MainActivity contextActivity;
    private Polyline polyline;
    public MyAdapter(MainActivity context, ArrayList<WayPoint> wayPointList, RecyclerView RV,LatLng currentLatLng){
        inflater = LayoutInflater.from(context);
        mArrayList = wayPointList;
        mRecyclerView = RV;
        this.contextActivity = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.places_item_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                min = 0.0;
                polyPoints = new ArrayList<WayPoint>();
                int pointPosition = mRecyclerView.indexOfChild(v);
                String pointName = mArrayList.get(pointPosition).getName();
                latLng = mArrayList.get(pointPosition).getLatLng();
//                Toast.makeText(context, latLng.toString(), Toast.LENGTH_SHORT).show();
//                mMap.addPolyline(new PolylineOptions().add());
                // create marker
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(pointName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                // adding marker
//                marker = mMap.addMarker(markerOptions);
                if(contextActivity instanceof MainActivity) {
                    if(myMarker != null) {
                        myMarker.remove();
                    }


                    double min = contextActivity.distance(mArrayList.get(0).getLatLng(),contextActivity.latLng);
                    startLatLng = mArrayList.get(0).getLatLng();
                    for(int i=1 ;i<mArrayList.size();i++){
                        double d = contextActivity.distance(mArrayList.get(i).getLatLng(),contextActivity.latLng);
                        if(d<min){
                            min = d;
                            Log.i("LatValue","Changed To"+mArrayList.get(i).getName());
                            startLatLng = mArrayList.get(i).getLatLng();
                            startWayPoint = mArrayList.get(i);
                        }
                    }
                    PolylineOptions polylineOptions = new PolylineOptions();
                    if(polyline != null){
                        polyline.remove();
                    }
                    polylineOptions.add(startLatLng);
                    int ind = mArrayList.get(pointPosition).getId();
                    Log.i("pointway",String.valueOf(ind));
                    desiredWayPoint = (WayPoint) startWayPoint.getArrayList().get(mArrayList.get(pointPosition).getId());

                    if(desiredWayPoint.getLatLng() == mArrayList.get(pointPosition).getLatLng()){
                        polylineOptions.add(desiredWayPoint.getLatLng());
                    }else{
                        while(desiredWayPoint.getLatLng() != mArrayList.get(pointPosition).getLatLng()){
                            polylineOptions.add(desiredWayPoint.getLatLng());
                            desiredWayPoint = (WayPoint) desiredWayPoint.getArrayList().get(mArrayList.get(pointPosition).getId());
                        }
                        polylineOptions.add(desiredWayPoint.getLatLng());
                    }
                    polyline = contextActivity.getMap().addPolyline(polylineOptions.width(20).color(Color.BLUE));

                    GoogleMap m = contextActivity.getMap();
                    myMarker = m.addMarker(markerOptions);
                    contextActivity.showList(contextActivity.img_showList);
                }else {
                    Toast.makeText(contextActivity, "you cannot invoke!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = mArrayList.get(position).getName();
        holder.textView.setText(name);
//        holder.icon.setImageResource(img);

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public Polyline getPolyline(){
        return polyline;
    }
    public Marker getMarker(){
        return myMarker;
    }

    public WayPoint getDest(){
        if(desiredWayPoint != null){
            return desiredWayPoint;
        }else{
            return null;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        //        ImageView icon;
        TextView textView;
        private MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
//            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
