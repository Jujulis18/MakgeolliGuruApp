package com.example.makgeolliguru;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


public class Profile_Fragment extends Fragment  {
    public Profile_Fragment(){
        // require a empty public constructor
    }
    Info_Fragment infoFragment = new Info_Fragment();
    Questionnaire_Fragment questionnaireFragment = new Questionnaire_Fragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        ImageButton infobtn = view.findViewById(R.id.buttonInfo);
        ImageView launch_questionnaire = view.findViewById(R.id.imageView1);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (launch_questionnaire == null) {
            Log.e("Profile_Fragment", "launch_questionnaire is null");
        }
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, infoFragment)
                        .addToBackStack(null)  // Optional: Add to back stack for fragment navigation
                        .commit();
            }
        });

        launch_questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Questionnaire_Fragment questionnaireFragment = Questionnaire_Fragment.newInstance(0);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flFragment, questionnaireFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}