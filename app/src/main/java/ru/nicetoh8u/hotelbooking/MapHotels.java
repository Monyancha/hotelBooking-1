package ru.nicetoh8u.hotelbooking;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.nicetoh8u.hotelbooking.ListView.Apart;

public class MapHotels extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_hotels);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Realm.init(this);
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
        realm = Realm.getDefaultInstance();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        RealmResults<Apart> aparts = realm.where(Apart.class).findAll();
        for (int i=0;i<aparts.size();i++)
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(aparts.get(i).getApart_x()),
                    Double.parseDouble(aparts.get(i).getApart_y())))
                    .title(aparts.get(i).getApart_name()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(
                new LatLng(Double.parseDouble(aparts.get(0).getApart_x()),
                        Double.parseDouble(aparts.get(0).getApart_y()))))
        ;

    }

    public void onDestroy() {

        super.onDestroy();

        realm.close();

    }
}
