package com.prepr.b4walkin.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.repo.ClinicRepo;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private LiveData<List<Clinic>> mClinics;
    private ClinicRepo mClinicRepo;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        mClinicRepo = new ClinicRepo(application);
        mClinics = mClinicRepo.getAllClinics();
    }

    void setVisualRegion(VisibleRegion vr) {
        mClinicRepo.setVisibleRegion(vr);
    }

    LiveData<List<Clinic>> getClinics() {
        return mClinics;
    }
}