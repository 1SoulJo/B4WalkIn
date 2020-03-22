package com.prepr.b4walkin.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.prepr.b4walkin.data.AppDatabase;
import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.data.booking.BookingDao;

import java.util.List;

public class BookingRepo {
    private BookingDao dao;
    private LiveData<List<Booking>> mBookingsByClinic;
    private LiveData<List<Booking>> mBookingsByUser;
    private MutableLiveData<Integer> mClinicId = new MutableLiveData<>();
    private MutableLiveData<Integer> mUserId = new MutableLiveData<>();

    public BookingRepo(Application application) {
        AppDatabase db = AppDatabase.get(application);
        dao = db.bookingDao();

        mBookingsByClinic =
                Transformations.switchMap(mClinicId, id -> dao.getBookingByClinicId(id));

        mBookingsByUser =
                Transformations.switchMap(mUserId, id -> dao.getBookingByUserId(id));
    }

    public void setUserId(Integer id) {
        mUserId.setValue(id);
    }

    public void setClinicId(Integer id) {
        mClinicId.setValue(id);
    }

    public LiveData<List<Booking>> getBookingsByClinic() {
        return mBookingsByClinic;
    }

    public LiveData<List<Booking>> getBookingsByUser() {
        return mBookingsByUser;
    }

    public void insert(Booking booking) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(booking);
        });
    }
}
