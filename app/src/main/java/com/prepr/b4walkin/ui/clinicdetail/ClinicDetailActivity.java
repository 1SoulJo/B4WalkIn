package com.prepr.b4walkin.ui.clinicdetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.prepr.b4walkin.R;
import com.prepr.b4walkin.credential.AccountUtil;
import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.data.user.User;

import java.util.List;

public class ClinicDetailActivity extends AppCompatActivity {
    public static final String INTENT_KEY_CLINIC_ID = "clinicId";
    private static final int CALL_PERMISSION_REQUEST_CODE = 2;

    private ClinicDetailViewModel mViewModel;
    private Toolbar mToolbar;
    private Clinic mClinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_detail);
        mToolbar = findViewById(R.id.toolbar);

        // ViewModel
        mViewModel = new ViewModelProvider(this).get(ClinicDetailViewModel.class);
        mViewModel.getClinic().observe(this, this::updateClinicInfo);
        mViewModel.getBookings().observe(this, this::updateBookingInfo);

        int clinicId = getIntent().getIntExtra(INTENT_KEY_CLINIC_ID, 0);
        mViewModel.setClinicId(clinicId);

        // Button event listeners
        findViewById(R.id.btnDirection).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="
                            + mClinic.getLatitude() + ","
                            + mClinic.getLongitude()));
            startActivity(intent);
        });
        findViewById(R.id.btnMakeCall).setOnClickListener(v -> makeCall());
        findViewById(R.id.btnChat).setOnClickListener(v -> sendMessage());
        findViewById(R.id.btnAppointment).setOnClickListener(v -> makeAppointment());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeCall();
        }
    }

    private void updateClinicInfo(Clinic clinic) {
        mClinic = clinic;

        // Toolbar
        mToolbar.setTitle(clinic.getName());
        setSupportActionBar(mToolbar);

        // Clinic info
        TextView tv = findViewById(R.id.txtAddress);
        tv.setText(mClinic.getAddress());

        tv = findViewById(R.id.txtCall);
        tv.setText(mClinic.getPhone());

        tv = findViewById(R.id.txtOpenTime);
        tv.setText(mClinic.getOperationTime());
    }

    private void updateBookingInfo(List<Booking> bookings) {
        TextView tv = findViewById(R.id.queueCount);
        tv.setText("" + bookings.size());
    }

    private void makeCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.CALL_PHONE },
                    CALL_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mClinic.getPhone()));
            startActivity(intent);
        }
    }

    private void sendMessage() {
        Uri uri = Uri.parse("smsto:" + mClinic.getPhone());
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
    }

    private void makeAppointment() {
        AccountUtil util = AccountUtil.get(getApplication());

        User user = util.getLoggedInUser();
        if (user == null) {
            Toast.makeText(getApplication(), "Log-in required.", Toast.LENGTH_LONG).show();
            return;
        }

        mViewModel.makeAppointment(mClinic, user);

        Toast.makeText(getApplication(), "Completed", Toast.LENGTH_LONG).show();
    }
}
