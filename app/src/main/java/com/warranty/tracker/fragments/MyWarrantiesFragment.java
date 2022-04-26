package com.warranty.tracker.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.warranty.tracker.R;
import com.warranty.tracker.activities.HomePageActivity;
import com.warranty.tracker.adapters.WarrentyAdapter;
import com.warranty.tracker.modal.OnWarrantyClickListener;
import com.warranty.tracker.modal.WarrantyItem;
import com.warranty.tracker.sqlite.DBHandler;

import java.util.Objects;

public class MyWarrantiesFragment extends Fragment {


    private RecyclerView warrantyItem;
    private WarrentyAdapter warrentyAdapter;
    private DBHandler dbHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_warranties, container, false);
        dbHandler = new DBHandler(getActivity());

        warrantyItem = view.findViewById(R.id.rvWarrantyItem);
        warrentyAdapter = new WarrentyAdapter(getActivity(),dbHandler.getAllWarrantyList());
        warrantyItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        warrantyItem.setAdapter(warrentyAdapter);
        warrentyAdapter.setListener(new OnWarrantyClickListener() {
            @Override
            public void onWarrantClick(WarrantyItem warrantyItem) {
                Bundle bundle = new Bundle();
                bundle.putString("id", ""+warrantyItem.getId());
                WarrantyItemInfoFragment warrantyItemInfoFragment = new WarrantyItemInfoFragment();
                warrantyItemInfoFragment.setArguments(bundle);
                ((HomePageActivity)getActivity()).addNew(warrantyItemInfoFragment,"Info",true);
            }
        });

        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePageActivity)getActivity()).onBackPressed();
            }
        });

        return view;
    }


}