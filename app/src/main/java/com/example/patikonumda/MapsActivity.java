package com.example.patikonumda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static final LatLng MASLAK = new LatLng(41.106445, 29.015945);
    static final LatLng USKUDAR = new LatLng(41.000005, 29.000045);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        /*map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.your_vector_asset))
                .title(title);*/
        //gMap= mapFragment.getMap();
        mapFragment.getMapAsync(this);// harita hazır olduğunda tetikleme mekanizması

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
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions().position(MASLAK).title("MASLAK"));
            mMap.addMarker(new MarkerOptions().position(USKUDAR).title("USKUDAR").snippet("Tarihi yerler").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
            PolylineOptions options = new PolylineOptions().add(MASLAK).add(USKUDAR).width(5).color(Color.BLUE).visible(true).geodesic(true);
            mMap.addPolyline(options);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.setTrafficEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            UiSettings uis=googleMap.getUiSettings();
            uis.setCompassEnabled(true);
            uis.setZoomControlsEnabled(true); //yakınlaştırma efektleri
            uis.setMyLocationButtonEnabled(true); //bu kısımda konuma erişim ve konum atmayı sağlıyoruz bu sayede  haritada konum olarak gösterebiliyoruz kendimizi
            addMarker("yeni yer",41.054365, 39.225768);//istediğimiz yere konum atabiliyoruz
            mMap.setOnMyLocationChangeListener(new LocationGozlemci());//hareketimizi haritada canlı olarak gösterir.konumuzu güncel tutar.
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MASLAK,12));
    }
    private void addMarker(String title, double latitude, double longitude){
        MarkerOptions m= new MarkerOptions();
        m.title(title);
        m.draggable(true);
        m.position(new LatLng(latitude,longitude));
        mMap.addMarker(m);
    }
    public class LocationGozlemci implements GoogleMap.OnMyLocationChangeListener{

        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc=new LatLng(location.getLatitude(),location.getLongitude());
            Projection p=mMap.getProjection();
            Point point = p.toScreenLocation(loc);
            CircleOptions circle=new CircleOptions();
            circle.center(loc);
            circle.fillColor(Color.RED);
            circle.radius(10);
            circle.strokeWidth(1);
            mMap.addCircle(circle);
        }
    }
}