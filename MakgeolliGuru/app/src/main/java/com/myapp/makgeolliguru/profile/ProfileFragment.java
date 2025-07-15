package com.myapp.makgeolliguru.profile;

import static android.content.Context.MODE_PRIVATE;
import static com.myapp.makgeolliguru.MainActivity.MAK_PROFILE;
import static com.myapp.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.makgeolliguru.InfoFragment;
import com.myapp.makgeolliguru.R;


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

        SharedPreferences prefs = container.getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String mak_profile = prefs.getString(MAK_PROFILE, null);
        if (mak_profile != null) {
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.profile_name);
            TextView description = view.findViewById(R.id.profile_description);
            if (mak_profile != null) {
                CardView cardView = view.findViewById(R.id.profile_card_view);
                cardView.setVisibility(View.VISIBLE);

                switch (mak_profile) {
                    case "Sparkling":
                        imageView.setImageResource(R.drawable.sparkling_perso);
                        name.setText(getContext().getString(R.string.sparkling_name));
                        description.setText(getContext().getString(R.string.sparkling_profile));
                        break;
                    case "Fruity":
                        imageView.setImageResource(R.drawable.fruity_perso);
                        name.setText(getContext().getString(R.string.fruity_name));
                        description.setText(getContext().getString(R.string.fruity_profile));
                        break;
                    case "Sweet":
                        imageView.setImageResource(R.drawable.nuts_perso);
                        name.setText(getContext().getString(R.string.sweet_name));
                        description.setText(getContext().getString(R.string.sweet_profile));
                        break;

                }

            }
        }

        CardView launchQuestionnaire = view.findViewById(R.id.questionnaire_card_view);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

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

        CardView favoriteCardView = view.findViewById(R.id.favorite_card_view);
        favoriteCardView.setOnClickListener ( v ->{

            FavoriteFragment favoriteFragment = new FavoriteFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.flFragment, favoriteFragment)
                    .addToBackStack(null);

            fragmentTransaction.commit();
        });

        CardView savedCardView = view.findViewById(R.id.saved_card_view);
        savedCardView.setOnClickListener ( v ->{

            SavedFragment savedFragment = new SavedFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.flFragment, savedFragment)
                    .addToBackStack(null);

            fragmentTransaction.commit();
        });

        return view;
    }
}