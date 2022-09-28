package com.bmlmunjal.mad;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    ImageView Edit_settings;
    TextView logout_settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_settings, container, false);
        Edit_settings = myView.findViewById(R.id.edit_settings);
        Edit_settings.setOnClickListener(this);

        logout_settings= myView.findViewById(R.id.logoutbtn_settings);
        logout_settings.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.edit_settings):
                startActivity(new Intent(getActivity(),profile_page.class));
                break;
            case (R.id.logoutbtn_settings):
                startActivity(new Intent(getActivity(),Login_Activity.class));
        }
    }
}