package com.example.nazdeeqapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nazdeeqapp.CustomerHome;
import com.example.nazdeeqapp.DeleteLater.DriverMapActivity;
import com.example.nazdeeqapp.DriverCreatePostActivity;
import com.example.nazdeeqapp.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        v.findViewById(R.id.driver_mode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), DriverCreatePostActivity.class);
                startActivity(in);
            }
        });

        v.findViewById(R.id.rider_mode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), CustomerHome.class);
                startActivity(in);
            }
        });


        return v;

    }
}
