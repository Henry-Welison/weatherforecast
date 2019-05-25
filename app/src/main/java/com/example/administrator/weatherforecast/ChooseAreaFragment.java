package com.example.administrator.weatherforecast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChooseAreaFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.choose_area,container,false);
        return view1;
    }
}
