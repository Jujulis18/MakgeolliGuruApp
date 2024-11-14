package com.example.makgeolliguru;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class InfoFragment extends Fragment {

    public InfoFragment(){
        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView myTextView = view.findViewById(R.id.infoText);

        String info = "Version 1.0.0 <ul><li>Fix Async problem or display popup</li><li>Add the Get your makgeolli personality questionnaire/li><li>Add article feature</li><li>Second mode on map for bar and shop</li><li>Add makgeolli picture</li></ul>";

        myTextView.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT));


        ImageButton closebtn = view.findViewById(R.id.closebtn);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, new ProfileFragment())
                        .addToBackStack(null)  // Optional: Add to back stack for fragment navigation
                        .commit();
            }
        });

        return view;
    }
}
