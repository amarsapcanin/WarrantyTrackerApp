package com.warranty.tracker.fragments;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.warranty.tracker.R;
import com.warranty.tracker.activities.HomePageActivity;
import com.warranty.tracker.modal.WarrantyItem;
import com.warranty.tracker.sqlite.DBHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WarrantyItemInfoFragment extends Fragment {

    private ImageView imvWarrantyItem, imvCalendar;
    private TextView tvWarrantyItemName;
    private EditText edtWarrantyDate;

    private DBHandler dbHandler;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warranty_item_info, container, false);
        dbHandler = new DBHandler(getActivity());

        imvWarrantyItem = view.findViewById(R.id.imvItem);
        imvCalendar = view.findViewById(R.id.imvCalender);
        tvWarrantyItemName = view.findViewById(R.id.tvWarrantyItemName);
        edtWarrantyDate = view.findViewById(R.id.edtWarrantyDate);

        WarrantyItem warrantyItem = dbHandler.getAllWarrantyItemById(getArguments().getString("id"));

        imvWarrantyItem.setImageURI(Uri.parse(warrantyItem.getImagePath()));
        tvWarrantyItemName.setText(warrantyItem.getName());


        edtWarrantyDate.setText(warrantyItem.getDate());

        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePageActivity)getActivity()).onBackPressed();
            }
        });
        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
        edtWarrantyDate.setText(dateFormat.format(myCalendar.getTime()));
    }

}