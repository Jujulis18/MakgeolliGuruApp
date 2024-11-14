package com.example.makgeolliguru;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ProfileFragment extends Fragment  {
    public ProfileFragment(){
        // require a empty public constructor
    }
    InfoFragment infoFragment = new InfoFragment();
    QuestionnaireFragment questionnaireFragment = new QuestionnaireFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton infoBtn = view.findViewById(R.id.buttonInfo);
        ImageView launchQuestionnaire = view.findViewById(R.id.imageView1);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (launchQuestionnaire == null) {
            Log.e("ProfileFragment", "launch_questionnaire is null");
        }
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, infoFragment)
                        .addToBackStack(null)  // Optional: Add to back stack for fragment navigation
                        .commit();
            }
        });

        launchQuestionnaire.setOnClickListener(v -> {
            QuestionnaireFragment questionnaireFragment = QuestionnaireFragment.newInstance(0);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flFragment, questionnaireFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}