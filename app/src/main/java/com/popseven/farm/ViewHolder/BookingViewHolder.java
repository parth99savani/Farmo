package com.popseven.farm.ViewHolder;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.popseven.farm.R;

public class BookingViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layoutHader;
    public TextView txtStatusBooking,txtBookingIdBooking,txtNameBooking,txtAddressBooking,txtDateTimeBooking,txtAmountPaidBooking,txtDateCheckInBooking,txtTimeCheckInBooking,txtNightCountBooking,txtDateCheckOutBooking,txtTimeCheckOutBooking,txtGuestBooking;
    public ImageView imageBooking;
    public AppCompatButton btnRateNowBooking;

    public BookingViewHolder(View itemView) {
        super(itemView);
        layoutHader = (LinearLayout) itemView.findViewById(R.id.layoutHader);
        txtStatusBooking = (TextView) itemView.findViewById(R.id.txtStatusBooking);
        txtBookingIdBooking = (TextView) itemView.findViewById(R.id.txtBookingIdBooking);
        txtNameBooking = (TextView) itemView.findViewById(R.id.txtNameBooking);
        txtAddressBooking = (TextView) itemView.findViewById(R.id.txtAddressBooking);
        txtDateTimeBooking = (TextView) itemView.findViewById(R.id.txtDateTimeBooking);
        txtAmountPaidBooking = (TextView) itemView.findViewById(R.id.txtAmountPaidBooking);
        txtDateCheckInBooking = (TextView) itemView.findViewById(R.id.txtDateCheckInBooking);
        txtTimeCheckInBooking = (TextView) itemView.findViewById(R.id.txtTimeCheckInBooking);
        txtNightCountBooking = (TextView) itemView.findViewById(R.id.txtNightCountBooking);
        txtDateCheckOutBooking = (TextView) itemView.findViewById(R.id.txtDateCheckOutBooking);
        txtTimeCheckOutBooking = (TextView) itemView.findViewById(R.id.txtTimeCheckOutBooking);
        txtGuestBooking = (TextView) itemView.findViewById(R.id.txtGuestBooking);
        imageBooking = (ImageView) itemView.findViewById(R.id.imageBooking);
        btnRateNowBooking = (AppCompatButton) itemView.findViewById(R.id.btnRateNowBooking);
    }
}
