package com.prepr.b4walkin.data.clinic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClinicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Clinic clinic);

    @Query("SELECT * FROM clinics")
    LiveData<List<Clinic>> getClinics();

    @Query("SELECT * FROM clinics WHERE lat > :lat1 AND lat < :lat2 AND long > :lng1 AND long < :lng2")
    LiveData<List<Clinic>> getClinicsByPosition(Double lat1, Double lng1, Double lat2, Double lng2);

    @Query("SELECT * FROM clinics WHERE id = :id")
    LiveData<Clinic> getClinicById(Integer id);

    @Query("DELETE FROM clinics")
    void deleteAll();
}
