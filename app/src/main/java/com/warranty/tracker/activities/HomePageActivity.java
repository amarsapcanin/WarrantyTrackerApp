package com.warranty.tracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.warranty.tracker.R;
import com.warranty.tracker.fragments.AddWarrantyFragment;
import com.warranty.tracker.fragments.HomeFragmentFragment;
import com.warranty.tracker.fragments.MyWarrantiesFragment;

public class HomePageActivity extends AppCompatActivity {

    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home_page);
        container = findViewById(R.id.container);

        addNew(new HomeFragmentFragment(),"Warranty Tracker",false);
    }

    public void addNew(Fragment fragment, String title, boolean isAddToBack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        if(isAddToBack) {
            ft.addToBackStack(fragment.getTag());
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}