package com.prepr.b4walkin.ui.bookings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepr.b4walkin.R;
import com.prepr.b4walkin.data.booking.Booking;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {
    private final LayoutInflater mInflater;
    private List<Booking> mBookings;

    public BookingsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.booking_item, parent, false);

        return new BookingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        if (mBookings != null) {
            holder.mClinicName.setText(mBookings.get(position).getClinicName());
            holder.mSubTitle.setText(mBookings.get(position).getDate().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (mBookings != null) {
            return mBookings.size();
        }
        return 0;
    }

    public void setBookings(List<Booking> list) {
        Log.w("BookingAdapter", "setBookings : " + list.size());
        mBookings = list;

        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView mClinicName;
        private TextView mSubTitle;

        private BookingViewHolder(View itemView) {
            super(itemView);

            mClinicName = itemView.findViewById(R.id.txtClinic);
            mSubTitle = itemView.findViewById(R.id.txtSubtitle);
        }
    }
}
