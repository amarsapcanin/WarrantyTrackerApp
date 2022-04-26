package com.warranty.tracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.warranty.tracker.R;
import com.warranty.tracker.activities.HomePageActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragmentFragment extends Fragment {

    private Button btnAddNew, btnMyWarranty;

    public static HomeFragmentFragment newInstance(String param1, String param2) {
        HomeFragmentFragment fragment = new HomeFragmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnAddNew = view.findViewById(R.id.btnAddNew);
        btnMyWarranty = view.findViewById(R.id.btnMyWarranty);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePageActivity)getActivity()).addNew(new AddWarrantyFragment(),"Add Warranty",true);
            }
        });

        btnMyWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePageActivity)getActivity()).addNew(new MyWarrantiesFragment(),"My Warranty",true);
            }
        });

        return view;
    }
}