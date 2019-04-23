package com.agamidev.indigo.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by agamidev on 6/23/17.
 */

public class WayPoint {
    private int id;
    private String name;
    private LatLng latLng;
    private ArrayList<WayPoint> arrayList;

    public WayPoint(int newID,String newName ,LatLng newLatLng , ArrayList newArrayList){
        id = newID;
        name = newName;
        latLng = newLatLng;
        arrayList = newArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }
}
