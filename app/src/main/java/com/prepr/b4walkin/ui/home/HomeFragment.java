package com.prepr.b4walkin.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prepr.b4walkin.R;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.ui.clinicdetail.ClinicDetailActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.prepr.b4walkin.ui.clinicdetail.ClinicDetailActivity.INTENT_KEY_CLINIC_ID;

public class HomeFragment extends Fragment {
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final static LatLng INITIAL_LOCATION = new LatLng(43.575279, -79.571297);

    private MapView mMapView;
    private GoogleMap mMap;
    private HomeViewModel mHomeViewModel;
    private int mSelectedClinic = -1;

    private Map<Marker, Integer> mMarkerMap = new HashMap<>();

    // Observer to update clinics in current view area
    private final Observer<List<Clinic>> mClinicObserver = new Observer<List<Clinic>>() {
        @Override
        public void onChanged(List<Clinic> clinics) {
            mMap.clear();
            mMarkerMap.clear();
            for (Clinic clinic : clinics) {
                LatLng cord = new LatLng(clinic.getLatitude(), clinic.getLongitude());
                MarkerOptions opt = new MarkerOptions().position(cord).title(clinic.getName());
                opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_clinic));
                opt.snippet(clinic.getOperationTime());

                Marker m = mMap.addMarker(opt);
                if (clinic.getId() == mSelectedClinic) {
                    m.showInfoWindow();
                }
                mMarkerMap.put(m, clinic.getId());
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = root.findViewById(R.id.map_view);

        if (ContextCompat.checkSelfPermission(
                Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setupMap();
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
        }

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mHomeViewModel.getClinics().observe(getViewLifecycleOwner(), mClinicObserver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        setupMap();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void setupMap() {
        mMapView.getMapAsync(googleMap -> {
            mMap = googleMap;

            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INITIAL_LOCATION, 14.0f));

            // Add marker event handler
            mMap.setOnInfoWindowClickListener(marker -> {
                Intent i = new Intent(getContext(), ClinicDetailActivity.class);
                i.putExtra(INTENT_KEY_CLINIC_ID, mMarkerMap.get(marker));
                startActivity(i);
            });

            mMap.setOnCameraIdleListener(() -> {
                mHomeViewModel.setVisualRegion(mMap.getProjection().getVisibleRegion());
            });

            mMap.setOnMarkerClickListener(marker -> {
                mSelectedClinic = mMarkerMap.get(marker);
                return false;
            });

            mMap.setOnMapClickListener(latLng -> mSelectedClinic = -1);

            mMapView.setVisibility(View.VISIBLE);
        });
    }
}
