package dcom.citycab12;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dcom.citycab12.Helper.DirectionJSONParser;

public class DriverTracking extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener{

    private GoogleMap mMap;
    double riderLat,riderLng;
    private Circle riderMarker;
    private Marker driverMarker;
    private Polyline direction;
    IGoogleAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(getIntent() != null)
        {
            riderLat = getIntent().getDoubleExtra("lat",-1.0);
            riderLng = getIntent().getDoubleExtra("lng",-1.0);
        }

        mService = common.getGoogleAPI();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        riderMarker = mMap.addCircle(new CircleOptions()
        .center(new LatLng(riderLat,riderLng)))
                .radius(10)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f);
    }


    private void displayLocation()
    {
        if(driverMarker != null)

            driverMarker.remove();
            driverMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude))
            .title("you")
            .icon(BitmapDescriptorFactory.defaultMarker()));

            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude),17.0f));

            if(direction != null)
                direction.remove();
            getDirection();



    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        displayLocation();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getDirection()
    {
        try{
        new ParserTask().execute(response.body().toString());

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

        private class ParserTask extends AsyncTask<String, Integer,List<List<HashMap<String,String>>>>
        {
            ProgressDialog.mDialog = new ProgressDialog(DriverTracking.this);

            protected void onPreExecute(){

                super.onPreExecute();
                mDialog.serMessage("Please waiting....");
                mDialog.show();

            }

            @Override
                protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
                mDialog.dismiss();

                Arraylist points = null;
                PolylineOptions polylineOptions = null;

                for(int i=0;i<lists.size();i++)
                {
                    points = new ArrayList();
                    PolylineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = lists.get(i);

                    for(int j=0;j<path.size();j++)
                    {
                        List<HashMap<String, String>> point = path.get(j);

                        double lat = Double.parseDouble(Point.get("lat"));
                         double lng = Double.parseDouble(Point.get("lng"));

                            LatLng position = new LatLng(lat,lng);
                            points.add(position);
                    }

                    polylineOptions.addAll(points);
                    polylineOptions.width(10);
                    polylineOptions.color(Color.RED);
                    PolylineOptions.geodesic(true);

                }

                direction = mMap.addPolyline(PolylineOptions);


            }

            protected  List<List<HashMap<String,String>>>  doInBackground(String string)
            {
                JSONObject jObject;
                List<List<HashMap<String,String>>> routes = null;

                try{
                    jObject = new JSONObject(strings[0]);
                    DirectionJSONParser parser=new DirectionJSONParser();
                    routes = parser.parse(jObject);

                }
                catch (JSONException e)
                {
                     e.printStackTrace();
                }

                return routes;

            }
        }
        }
}
