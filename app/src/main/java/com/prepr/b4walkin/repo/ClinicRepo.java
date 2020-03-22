package com.prepr.b4walkin.repo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.prepr.b4walkin.data.AppDatabase;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.data.clinic.ClinicDao;

import java.util.List;

public class ClinicRepo {
    private ClinicDao dao;
    private LiveData<List<Clinic>> mAllClinics;
    private MutableLiveData<VisibleRegion> mVisibleRegion = new MutableLiveData<>();
    private LiveData<Clinic> mClinicById;
    private MutableLiveData<Integer> mClinicId = new MutableLiveData<>();

    public ClinicRepo(Application application) {
        AppDatabase db = AppDatabase.get(application);
        dao = db.clinicDao();

        mAllClinics = Transformations.switchMap(mVisibleRegion, vr -> {
            LatLng sw = vr.latLngBounds.southwest;
            LatLng ne = vr.latLngBounds.northeast;
            return dao.getClinicsByPosition(sw.latitude, sw.longitude, ne.latitude, ne.longitude);
        });

        mClinicById = Transformations.switchMap(mClinicId, id -> {
            return dao.getClinicById(id);
        });
    }

    public void setVisibleRegion(VisibleRegion vr) {
        mVisibleRegion.setValue(vr);
    }

    public void setClinicId(Integer id) {
        mClinicId.setValue(id);
    }

    public LiveData<List<Clinic>> getAllClinics() {
        return mAllClinics;
    }

    public LiveData<Clinic> getClinicById() {
        return mClinicById;
    }

    public void insert(Clinic clinic) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(clinic);
        });
    }
}
