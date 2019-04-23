package com.agamidev.indigo.Activitites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.agamidev.indigo.Adapters.MyAdapter;
import com.agamidev.indigo.R;
import com.agamidev.indigo.SdkExample;
import com.agamidev.indigo.Models.WayPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;


@SdkExample(description = R.string.example_googlemaps_overlay_description)
public class MainActivity extends FragmentActivity {


    private static final String TAG = "IndoorAtlasExample";

    private static final float HUE_IABLUE = 200.0f;

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private IAFloorPlan mIAFloorPlan;
    private IARegion mOverlayFloorPlan = null;
    private GroundOverlay mGroundOverlay = null;
    private IALocationManager mIALocationManager;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private Target mLoadTarget;
    private boolean mCameraPositionNeedsUpdating = true; // update on first location
    private Polyline polyline;

//    Button btn_drawLine;
    ListView mListView;
    boolean visibility;
    boolean aboutUsVisibility;
    boolean scheduleVisibility;
    ArrayList<WayPoint> listviewArray;
    ArrayAdapter listviewAdapter;
    public ImageView img_showList;
    ScrollView scrollView;
    ImageView img_aboutUs;
    ImageView img_showSchedule;
    MyAdapter myAdapter;
    RecyclerView mRecyclerView;
    LinearLayout layout_AboutUs;
    ArrayList<String> schedulesArray;
    ArrayAdapter<String> schedulesAdapter;
    ListView lv_Schedules;
    ArrayList<Integer> imagesArray;

    WayPoint A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q;
    ArrayList<WayPoint> arrayA;
    ArrayList<WayPoint> arrayB;
    ArrayList<WayPoint> arrayC;
    ArrayList<WayPoint> arrayD;
    ArrayList<WayPoint> arrayE;
    ArrayList<WayPoint> arrayF;
    ArrayList<WayPoint> arrayG;
    ArrayList<WayPoint> arrayH;
    ArrayList<WayPoint> arrayI;
    ArrayList<WayPoint> arrayJ;
    ArrayList<WayPoint> arrayK;
    ArrayList<WayPoint> arrayL;
    ArrayList<WayPoint> arrayM;
    ArrayList<WayPoint> arrayN;
    ArrayList<WayPoint> arrayO;
    ArrayList<WayPoint> arrayP;
    ArrayList<WayPoint> arrayQ;

    public LatLng latLng;
    Marker adapterMarker;
    Polyline adapterPolyline;
    WayPoint dest;
    IALocation loc;
    //111111111111111111111111111111111111111111111111111111
    /**
     * Listener that handles location change events.
     */
    private IALocationListener mListener = new IALocationListenerSupport() {

        /**
         * Location changed, move marker and camera position.
         */
        @Override
        public void onLocationChanged(IALocation location) {
            if (mMap == null) {
                // location received before map is initialized, ignoring update here
                return;
            }
            loc = location;
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            IALatLng iaLatlng = new IALatLng(location.getLatitude(), location.getLongitude());
            if (mMarker == null) {
                // first location, add marker
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));
            } else {
                // move existing markers position to received location
                mMarker.setPosition(latLng);
            }
            // our camera position needs updating if location has significantly changed
            if (mCameraPositionNeedsUpdating) {
                CameraPosition camPos = CameraPosition
                        .builder(
                        ).target(latLng)
                        .bearing(location.getBearing())
                        .zoom(20.0f)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
//              mCameraPositionNeedsUpdating = false;
            }
            adapterMarker = myAdapter.getMarker();
            adapterPolyline = myAdapter.getPolyline();
            dest = myAdapter.getDest();
            if(dest!=null) {
                if (distance(latLng, dest.getLatLng()) <= 1.3608396259807245) {
                    adapterPolyline.remove();
                    adapterMarker.remove();
                }
            }
        }
    };

    //22222222222222222222222222222222222222222222222222222222222
    /**
     * Listener that changes overlay if needed
     */
    private IARegion.Listener mRegionListener = new IARegion.Listener() {

        @Override
        public void onEnterRegion(IARegion region) {
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                final String newId = region.getId();
                // Are we entering a new floor plan or coming back the floor plan we just left?
                if (mGroundOverlay == null || !region.equals(mOverlayFloorPlan)) {
                    mCameraPositionNeedsUpdating = true; // entering new fp, need to move camera
                    if (mGroundOverlay != null) {
                        mGroundOverlay.remove();
                        mGroundOverlay = null;
                    }
                    mOverlayFloorPlan = region; // overlay will be this (unless error in loading)
                    fetchFloorPlan(newId);
                } else {
                    mGroundOverlay.setTransparency(0.0f);
                }
            }
//            showInfo("Enter " + (region.getType() == IARegion.TYPE_VENUE
//                    ? "VENUE "
//                    : "FLOOR_PLAN ") + region.getId());
        }

        @Override
        public void onExitRegion(IARegion region) {
            if (mGroundOverlay != null) {
                // Indicate we left this floor plan but leave it there for reference
                // If we enter another floor plan, this one will be removed and another one loaded
                mGroundOverlay.setTransparency(0.5f);
            }
//            showInfo("Enter " + (region.getType() == IARegion.TYPE_VENUE
//                    ? "VENUE "
//                    : "FLOOR_PLAN ") + region.getId());
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        btn_drawLine = (Button) findViewById(R.id.btn_showList);
        mListView = (ListView) findViewById(R.id.lv_places);
        img_showList = (ImageView) findViewById(R.id.img_showList);
        img_aboutUs = (ImageView) findViewById(R.id.img_aboutUs);
        img_showSchedule = (ImageView) findViewById(R.id.img_showSchedule);
        mRecyclerView = (RecyclerView) findViewById(R.id.navRecycler);
        layout_AboutUs = (LinearLayout) findViewById(R.id.layout_AboutUs);
        scrollView = (ScrollView) findViewById(R.id.myscroller);
        lv_Schedules = (ListView)findViewById(R.id.lv_showSchedules);

        schedulesArray = new ArrayList<String>();
        schedulesArray.add("Hall 1");
        schedulesArray.add("Hall 2");
        schedulesArray.add("Laboratory 1");
        schedulesArray.add("Laboratory 2");
        schedulesArray.add("Laboratory MultiMedia");
        schedulesAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.places_item_list,schedulesArray);
        lv_Schedules.setAdapter(schedulesAdapter);

        imagesArray = new ArrayList<Integer>();
        imagesArray.add(R.drawable.table1);
        imagesArray.add(R.drawable.table2);
        imagesArray.add(R.drawable.table3);
        imagesArray.add(R.drawable.table4);
        imagesArray.add(R.drawable.table4);

//        marker = new Marker();

        initWayPoints();

        addDrawerItems();

        // prevent the screen going to sleep while app is on foreground
        findViewById(android.R.id.content).setKeepScreenOn(true);
        mIALocationManager = IALocationManager.create(this);


        final String floorPlanId = getString(R.string.indooratlas_floor_plan_id);
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation FLOOR_PLAN_ID = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(FLOOR_PLAN_ID);
        }
        // instantiate IALocationManager and IAResourceManager
        mIALocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);

        visibility = false;
        aboutUsVisibility = false;
        scheduleVisibility = false;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,listviewArray.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        lv_Schedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,ScheduleTable.class);
                i.putExtra("image",imagesArray);
                i.putExtra("index",position);
                MainActivity.this.startActivity(i);
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remember to clean up after ourselves
        mIALocationManager.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    double d = distance(new LatLng(31.04236633, 31.35203087), new LatLng(31.04237261, 31.35201861));
                    Toast.makeText(MainActivity.this, String.valueOf(d), Toast.LENGTH_SHORT).show();
                    System.out.println(d);



                }
            });
        }

        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    //555555555555555555555555555555555555555555555555555555555

    /**
     * Sets bitmap of floor plan as ground overlay on Google Maps
     */
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {

        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
        }

        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng center = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions fpOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .position(center, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());

            mGroundOverlay = mMap.addGroundOverlay(fpOverlay);
        }
    }

    //444444444444444444444444444444444444444444444444444444444

    /**
     * Download floor plan using Picasso library.
     */
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {
        mIAFloorPlan = floorPlan;
        final String url = floorPlan.getUrl();

        if (mLoadTarget == null) {
            mLoadTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth() + "x"
                            + bitmap.getHeight());
                    setupGroundOverlay(floorPlan, bitmap);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // N/A
                }

                @Override
                public void onBitmapFailed(Drawable placeHolderDraweble) {
                    showInfo("Failed to load bitmap");
                    mOverlayFloorPlan = null;
                }
            };
        }

        RequestCreator request = Picasso.with(this).load(url);

        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }

        request.into(mLoadTarget);
    }

    //33333333333333333333333333333333333333333333333333333333333

    /**
     * Fetches floor plan data from IndoorAtlas server.
     */
    private void fetchFloorPlan(String id) {

        // if there is already running task, cancel it
        cancelPendingNetworkCalls();

        final IATask<IAFloorPlan> task = mResourceManager.fetchFloorPlanWithId(id);

        task.setCallback(new IAResultCallback<IAFloorPlan>() {

            @Override
            public void onResult(IAResult<IAFloorPlan> result) {

                if (result.isSuccess() && result.getResult() != null) {
                    // retrieve bitmap for this floor plan metadata
                    fetchFloorPlanBitmap(result.getResult());
                } else {
                    // ignore errors if this task was already canceled
                    if (!task.isCancelled()) {
                        // do something with error
                        showInfo("Loading floor plan failed: " + result.getError());
                        mOverlayFloorPlan = null;
                    }
                }
            }
        }, Looper.getMainLooper()); // deliver callbacks using main looper

        // keep reference to task so that it can be canceled if needed
        mFetchFloorPlanTask = task;

    }

    /**
     * Helper method to cancel current task if any.
     */
    private void cancelPendingNetworkCalls() {
        if (mFetchFloorPlanTask != null && !mFetchFloorPlanTask.isCancelled()) {
            mFetchFloorPlanTask.cancel();
        }
    }

    private void showInfo(String text) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), text,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("CLOSE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }


    public double distance(LatLng p1, LatLng p2) {
        double lat1= p1.latitude;
        double lat2= p2.latitude;
        double lon1=p1.longitude;
        double lon2=p2.longitude;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }


    private void initWayPoints() {
        arrayA = new ArrayList<WayPoint>();
        arrayB = new ArrayList<WayPoint>();
        arrayC = new ArrayList<WayPoint>();
        arrayD = new ArrayList<WayPoint>();
        arrayE = new ArrayList<WayPoint>();
        arrayF = new ArrayList<WayPoint>();
        arrayG = new ArrayList<WayPoint>();
        arrayH = new ArrayList<WayPoint>();
        arrayI = new ArrayList<WayPoint>();
        arrayJ = new ArrayList<WayPoint>();
        arrayK = new ArrayList<WayPoint>();
        arrayL = new ArrayList<WayPoint>();
        arrayM = new ArrayList<WayPoint>();
        arrayN = new ArrayList<WayPoint>();
        arrayO = new ArrayList<WayPoint>();
        arrayP = new ArrayList<WayPoint>();
        arrayQ = new ArrayList<WayPoint>();




        A = new WayPoint(0,"Gate 1",new LatLng(31.04224496, 31.35200614),arrayA);
        B = new WayPoint(1,"Elevator",new LatLng(31.04224691, 31.35197083),arrayB);
        C = new WayPoint(2,"Steps 1",new LatLng(31.04225164, 31.35192211),arrayC);
        D = new WayPoint(3,"Teaching Staff Affairs",new LatLng(31.04235153, 31.35192923),arrayD);
        E = new WayPoint(4,"Teachers Room",new LatLng(31.04235082, 31.35195105),arrayE);
        F = new WayPoint(5,"Youth Care",new LatLng(31.04235042, 31.35198140),arrayF);
        G = new WayPoint(6,"Secretary\r\nTreasury",new LatLng(31.04234658, 31.35204558),arrayG);
        H = new WayPoint(7,"Hall 1",new LatLng(31.04225165, 31.35190041),arrayH);
        I = new WayPoint(8,"Hall 2\r\nSteps 2",new LatLng(31.04225532, 31.35176312),arrayI);
        J = new WayPoint(9,"Lab A",new LatLng(31.04223038, 31.35175233),arrayJ);
        K = new WayPoint(10,"Toilet 1",new LatLng(31.04221032, 31.35175244),arrayK);
        L = new WayPoint(11,"Lab B \r\n Ambulance",new LatLng(31.04218130, 31.35174921),arrayL);
        M = new WayPoint(12,"MultiMedia Lab\r\nGate 2",new LatLng(31.04213318, 31.35174343),arrayM);
        N = new WayPoint(13,"Student Affairs",new LatLng(31.04216764, 31.35196523),arrayN);
        O = new WayPoint(14,"Graduate Affairs",new LatLng(31.04214074, 31.35196025),arrayO);
        P = new WayPoint(15,"Guards Leader\r\nMasjed",new LatLng(31.04214282, 31.35192728),arrayP);
        Q = new WayPoint(16,"Toilet 2\r\nAdministartive affairs",new LatLng(31.04214372, 31.35189580),arrayQ);

        //fill arrayA
        fillArrays(arrayA,A);
        for(int i =0 ; i<16;i++){
            fillArrays(arrayA,B);
        }


        //fill arrayB
        fillArrays(arrayB,A);
        fillArrays(arrayB,B);
        for(int i=0 ; i< 11; i++){
            fillArrays(arrayB,C);
        }
        fillArrays(arrayB,N);
        fillArrays(arrayB,N);
        fillArrays(arrayB,N);
        fillArrays(arrayB,N);


        //fill arrayC
        fillArrays(arrayC,B);
        fillArrays(arrayC,B);
        fillArrays(arrayC,C);
        fillArrays(arrayC,D);
        fillArrays(arrayC,D);
        fillArrays(arrayC,D);
        fillArrays(arrayC,D);
        fillArrays(arrayC,H);
        fillArrays(arrayC,H);
        fillArrays(arrayC,H);
        fillArrays(arrayC,H);
        fillArrays(arrayC,H);
        fillArrays(arrayC,H);
        fillArrays(arrayC,B);
        fillArrays(arrayC,B);
        fillArrays(arrayC,B);
        fillArrays(arrayC,B);



        //fill arrayD
        fillArrays(arrayD,C);
        fillArrays(arrayD,C);
        fillArrays(arrayD,C);
        fillArrays(arrayD,D);
        fillArrays(arrayD,E);
        fillArrays(arrayD,E);
        fillArrays(arrayD,E);
        for(int i=0 ; i< 10; i++){
            fillArrays(arrayD,C);
        }


        //fill arrayE
        fillArrays(arrayE,D);
        fillArrays(arrayE,D);
        fillArrays(arrayE,D);
        fillArrays(arrayE,D);
        fillArrays(arrayE,E);
        fillArrays(arrayE,F);
        fillArrays(arrayE,F);
        for(int i=0 ; i< 10; i++){
            fillArrays(arrayE,D);
        }

        //fill arrayF
        fillArrays(arrayF,E);
        fillArrays(arrayF,E);
        fillArrays(arrayF,E);
        fillArrays(arrayF,E);
        fillArrays(arrayF,E);
        fillArrays(arrayF,F);
        fillArrays(arrayF,G);
        for(int i=0 ; i< 10; i++){
            fillArrays(arrayF,E);
        }

        //fill arrayG
        fillArrays(arrayG,F);
        fillArrays(arrayG,F);
        fillArrays(arrayG,F);
        fillArrays(arrayG,F);
        fillArrays(arrayG,F);
        fillArrays(arrayG,F);
        fillArrays(arrayG,G);
        for(int i=0 ; i< 10; i++){
            fillArrays(arrayG,F);
        }

        //fill arrayH
        for(int i=0 ; i< 7; i++){
            fillArrays(arrayH,C);
        }
        fillArrays(arrayH,H);
        fillArrays(arrayH,I);
        fillArrays(arrayH,I);
        fillArrays(arrayH,I);
        fillArrays(arrayH,I);
        fillArrays(arrayH,I);
        fillArrays(arrayH,C);
        fillArrays(arrayH,C);
        fillArrays(arrayH,C);
        fillArrays(arrayH,C);

        //fill arrayI
        for(int i=0 ; i< 8; i++){
            fillArrays(arrayI,H);
        }
        fillArrays(arrayI,I);
        fillArrays(arrayI,J);
        fillArrays(arrayI,J);
        fillArrays(arrayI,J);
        fillArrays(arrayI,J);
        fillArrays(arrayI,H);
        fillArrays(arrayI,H);
        fillArrays(arrayI,H);
        fillArrays(arrayI,H);


        //fill arrayJ
        for(int i=0 ; i< 9; i++){
            fillArrays(arrayJ,I);
        }
        fillArrays(arrayJ,J);
        fillArrays(arrayJ,K);
        fillArrays(arrayJ,K);
        fillArrays(arrayJ,K);
        fillArrays(arrayJ,I);
        fillArrays(arrayJ,I);
        fillArrays(arrayJ,I);
        fillArrays(arrayJ,I);


        //fill arrayK
        for(int i=0 ; i< 10; i++){
            fillArrays(arrayK,J);
        }
        fillArrays(arrayK,K);
        fillArrays(arrayK,L);
        fillArrays(arrayK,L);
        fillArrays(arrayK,J);
        fillArrays(arrayK,J);
        fillArrays(arrayK,J);
        fillArrays(arrayK,J);


        //fill arrayL
        for(int i=0 ; i< 11; i++){
            fillArrays(arrayL,K);
        }
        fillArrays(arrayL,L);
        fillArrays(arrayL,M);
        fillArrays(arrayL,K);
        fillArrays(arrayL,K);
        fillArrays(arrayL,K);
        fillArrays(arrayL,K);


        //fill arrayM
        for(int i=0 ; i< 12; i++){
            fillArrays(arrayM,L);
        }
        fillArrays(arrayM,M);
        fillArrays(arrayM,L);
        fillArrays(arrayM,L);
        fillArrays(arrayM,L);
        fillArrays(arrayM,L);



        //fill arrayN
        for(int i=0 ; i< 13; i++){
            fillArrays(arrayN,B);
        }
        fillArrays(arrayN,N);
        fillArrays(arrayN,O);
        fillArrays(arrayN,O);
        fillArrays(arrayN,O);


        //fill arrayO
        for(int i=0 ; i< 14; i++){
            fillArrays(arrayO,N);
        }
        fillArrays(arrayO,O);
        fillArrays(arrayO,P);
        fillArrays(arrayO,P);

        //fill arrayP
        for(int i=0 ; i< 15; i++){
            fillArrays(arrayP,O);
        }
        fillArrays(arrayP,P);
        fillArrays(arrayP,Q);


        //fill arrayQ
        for(int i=0 ; i< 16; i++){
            fillArrays(arrayQ,P);
        }
        fillArrays(arrayQ,Q);

    }

    private void fillArrays(ArrayList<WayPoint> a,WayPoint p) {
        a.add(p);
    }

    public void addDrawerItems() {
        listviewArray = new ArrayList<WayPoint>();
        listviewArray.add(A);
        listviewArray.add(B);
        listviewArray.add(C);
        listviewArray.add(D);
        listviewArray.add(E);
        listviewArray.add(F);
        listviewArray.add(G);
        listviewArray.add(H);
        listviewArray.add(I);
        listviewArray.add(J);
        listviewArray.add(K);
        listviewArray.add(L);
        listviewArray.add(M);
        listviewArray.add(N);
        listviewArray.add(O);
        listviewArray.add(P);
        listviewArray.add(Q);

        myAdapter = new MyAdapter(MainActivity.this, listviewArray, mRecyclerView,latLng);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myAdapter);


    }

    public void clearLines(View v){
        if(dest != null && adapterMarker != null && adapterPolyline != null){
            adapterPolyline.remove();
            adapterMarker.remove();
        }
    }

    public GoogleMap getMap(){
        return mMap;
    }




    public void showList(View v){

        if(!visibility) {
            if(layout_AboutUs.getVisibility() == View.VISIBLE ||
                    lv_Schedules.getVisibility() == View.VISIBLE){
                layout_AboutUs.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                lv_Schedules.setVisibility(View.GONE);
                img_aboutUs.setBackgroundColor(Color.WHITE);
                img_showSchedule.setBackgroundColor(Color.WHITE);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            img_showList.setBackgroundColor(Color.rgb(214, 234, 248));
            visibility = true;
//                    btn_drawLine.setText("Hide Places List");
        }else{
//                    mListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            img_showList.setBackgroundColor(Color.WHITE);
            visibility = false;
            mMarker.remove();
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));
//                    btn_drawLine.setText("Show Places List");
        }
    }

    public void showSchedule(View v){

        if(!scheduleVisibility){
            if(mRecyclerView.getVisibility() == View.VISIBLE ||
                    layout_AboutUs.getVisibility() == View.VISIBLE){
                mRecyclerView.setVisibility(View.GONE);
                layout_AboutUs.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                img_showList.setBackgroundColor(Color.WHITE);
                img_aboutUs.setBackgroundColor(Color.WHITE);
            }
            lv_Schedules.setVisibility(View.VISIBLE);
            scheduleVisibility = true;
            img_showSchedule.setBackgroundColor(Color.rgb(214, 234, 248));
        }else{
            lv_Schedules.setVisibility(View.GONE);
            img_showSchedule.setBackgroundColor(Color.WHITE);
            scheduleVisibility = false;
            mMarker.remove();
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));
        }
    }

    public void aboutUs(View v){

        if(!aboutUsVisibility){
            if(mRecyclerView.getVisibility() == View.VISIBLE ||
                    lv_Schedules.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(View.GONE);
                lv_Schedules.setVisibility(View.GONE);
                img_showList.setBackgroundColor(Color.WHITE);
                img_showSchedule.setBackgroundColor(Color.WHITE);
            }
            scrollView.setVisibility(View.VISIBLE);
            layout_AboutUs.setVisibility(View.VISIBLE);
            img_aboutUs.setBackgroundColor(Color.rgb(214, 234, 248));
            aboutUsVisibility = true;

        }else{
            layout_AboutUs.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            img_aboutUs.setBackgroundColor(Color.WHITE);
            aboutUsVisibility = false;
            mMarker.remove();
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));
        }


    }


}
