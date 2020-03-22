package com.prepr.b4walkin.ui.bookings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.repo.BookingRepo;

import java.util.List;

public class BookingsViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private BookingRepo mBookingRepo;
    private LiveData<List<Booking>> mBookingsByUser;
    private LiveData<List<Booking>> mBookingsByClinic;

    public BookingsViewModel(@NonNull Application application) {
        super(application);

        mText = new MutableLiveData<>();
        mText.setValue("Sign-in is required.");

        mBookingRepo = new BookingRepo(application);
        mBookingsByUser = mBookingRepo.getBookingsByUser();
        mBookingsByClinic = mBookingRepo.getBookingsByClinic();
    }

    public void setUserId(Integer id) {
        mBookingRepo.setUserId(id);
    }

    public LiveData<List<Booking>> getBookingsByUser() {
        return mBookingsByUser;
    }

    public LiveData<List<Booking>> getBookingsByClinic() {
        return mBookingsByClinic;
    }

    public LiveData<String> getText() {
        return mText;
    }
}