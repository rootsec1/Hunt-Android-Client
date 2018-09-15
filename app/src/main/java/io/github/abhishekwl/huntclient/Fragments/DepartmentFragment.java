package io.github.abhishekwl.huntclient.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.huntclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentFragment extends Fragment {

    private String departmentName;
    private View rootView;
    private Unbinder unbinder;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public DepartmentFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_department, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeComponents();
        initializeViews();
        return rootView;
    }

    private void initializeComponents() {

    }

    private void initializeViews() {
        Log.v("SCREEN", departmentName);
    }

}
