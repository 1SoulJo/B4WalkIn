package com.prepr.b4walkin.ui.bookings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prepr.b4walkin.R;
import com.prepr.b4walkin.credential.AccountUtil;
import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.data.user.User;

import java.util.List;
import java.util.Objects;

public class BookingsFragment extends Fragment {

    private BookingsViewModel bookingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings, container, false);
        RecyclerView list = root.findViewById(R.id.bookingsList);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        final BookingsAdapter adapter = new BookingsAdapter(getContext());
        list.setAdapter(adapter);

        bookingsViewModel =
                new ViewModelProvider(this).get(BookingsViewModel.class);
        bookingsViewModel.getBookingsByUser().observe(
                getViewLifecycleOwner(), bookings -> {
                    TextView tv = root.findViewById(R.id.txtDefault);
                    if (bookings.size() == 0) {
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                    }
                    adapter.setBookings(bookings);
                });

        AccountUtil util = AccountUtil.get(Objects.requireNonNull(getActivity()).getApplication());
        User user = util.getLoggedInUser();
        if (user != null) {
            bookingsViewModel.setUserId(user.getId());
        }

        return root;
    }
}
