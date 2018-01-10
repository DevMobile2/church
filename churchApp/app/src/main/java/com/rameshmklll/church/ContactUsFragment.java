package com.rameshmklll.church;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by venkatesh on 19/12/17.
 */

@SuppressLint("ValidFragment")
public class ContactUsFragment extends Fragment implements OnMapReadyCallback {


    private View mView;
    MapView mapView;
    GoogleMap googleMap;
    public ContactUsFragment(){

    }





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact Us");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView = inflater.inflate(R.layout.contact_us_frag, container, false);
        mapView = (MapView) mView.findViewById(R.id.ivMap);
        mapView.onCreate(savedInstanceState);

             mapView.getMapAsync(this);
//            googleMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on))
//                    .anchor(0.0f, 1.0f)
//                    .position(new LatLng(55.854049, 13.661331)));
//            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return mView;
            }
         /*   googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            MapsInitializer.initialize(this.getActivity());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(55.854049, 13.661331));
            LatLngBounds bounds = builder.build();
            int padding = 0;
            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cameraUpdate);*/

       return mView;
    }




    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googlMap) {
        googleMap = googlMap;
        googleMap.setMinZoomPreference(12);
        LatLng ny = new LatLng(16.7034739, 81.1122904);
        googleMap.addMarker(new MarkerOptions()
                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on))
                   .anchor(0.0f, 1.0f)
                   .position(new LatLng(16.7034739, 81.1122904)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
