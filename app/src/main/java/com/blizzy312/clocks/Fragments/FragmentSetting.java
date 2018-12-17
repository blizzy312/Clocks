package com.blizzy312.clocks.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.blizzy312.clocks.MainActivity;
import com.blizzy312.clocks.R;

public class FragmentSetting extends Fragment {

    View view;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private boolean showDays;

    private CheckBox showDaysBox, showDateBox;

    public FragmentSetting() {
        // Required empty public constructor
    }

    public static FragmentSetting newInstance(String param1, String param2) {
        FragmentSetting fragment = new FragmentSetting();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = ((MainActivity)getActivity()).getSharedPreferences("MySharedPref", 0);
        editor = settings.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity) getActivity()).showNav();

        showDaysBox = view.findViewById(R.id.showDaysCheckBox);
        showDaysBox.setChecked(settings.getBoolean("showDaysCheckBox", false));
        showDaysBox.setOnCheckedChangeListener(checkBoxDays);

        showDateBox = view.findViewById(R.id.showDateCheckBox);
        showDateBox.setChecked(settings.getBoolean("showDateCheckBox", false));
        showDateBox.setOnCheckedChangeListener(checkBoxDate);

        return view;
    }

    CheckBox.OnCheckedChangeListener checkBoxDays =
            new CheckBox.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // update your model (or other business logic) based on isChecked
                    if (isChecked){
                        editor.putBoolean("showDaysCheckBox",true);
                    }else{
                        editor.putBoolean("showDaysCheckBox",false);
                    }
                    editor.commit();
                }
            };
    CheckBox.OnCheckedChangeListener checkBoxDate =
            new CheckBox.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // update your model (or other business logic) based on isChecked
                    if (isChecked){
                        editor.putBoolean("showDateCheckBox",true);
                    }else{
                        editor.putBoolean("showDateCheckBox",false);
                    }
                    editor.commit();
                }
            };
}
