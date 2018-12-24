package com.tech12.prayertimes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DuasFragment extends Fragment {


    public DuasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_duas, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((HomeActivity) getActivity()).setActionBarTitle("Duas");
    }

}
