package com.prepr.b4walkin.data.booking;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Booking booking);

    @Query("SELECT * FROM bookings")
    LiveData<List<Booking>> getBookings();

    @Query("SELECT * FROM bookings WHERE user = :id ORDER BY date")
    LiveData<List<Booking>> getBookingByUserId(Integer id);

    @Query("SELECT * FROM bookings WHERE clinic_id = :id ORDER BY date")
    LiveData<List<Booking>> getBookingByClinicId(Integer id);

    @Query("DELETE FROM bookings")
    void deleteAll();

}
