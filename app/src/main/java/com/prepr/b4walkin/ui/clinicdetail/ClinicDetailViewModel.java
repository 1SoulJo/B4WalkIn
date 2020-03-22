package com.prepr.b4walkin.ui.clinicdetail;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prepr.b4walkin.credential.AccountUtil;
import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.data.user.User;
import com.prepr.b4walkin.repo.BookingRepo;
import com.prepr.b4walkin.repo.ClinicRepo;

import java.util.Calendar;
import java.util.List;

public class ClinicDetailViewModel extends AndroidViewModel {
    private ClinicRepo mClinicRepo;
    private BookingRepo mBookingRepo;
    private LiveData<Clinic> mClinic;
    private LiveData<List<Booking>> mBookings;

    public ClinicDetailViewModel(@NonNull Application application) {
        super(application);

        mClinicRepo = new ClinicRepo(application);
        mBookingRepo = new BookingRepo(application);

        mClinic = mClinicRepo.getClinicById();
        mBookings = mBookingRepo.getBookingsByClinic();
    }

    void setClinicId(Integer id) {
        Log.w("ClinicViewModel", "set id : " + id);
        mClinicRepo.setClinicId(id);
        mBookingRepo.setClinicId(id);
    }

    LiveData<Clinic> getClinic() {
        return mClinic;
    }

    LiveData<List<Booking>> getBookings() {
        return mBookings;
    }

    public void makeAppointment(Clinic clinic, User user) {
        Booking b = new Booking();
        b.setClinicId(clinic.getId());
        b.setClinicName(clinic.getName());
        b.setUserId(user.getId());
        b.setDate(Calendar.getInstance().getTime());
        mBookingRepo.insert(b);
    }
}
