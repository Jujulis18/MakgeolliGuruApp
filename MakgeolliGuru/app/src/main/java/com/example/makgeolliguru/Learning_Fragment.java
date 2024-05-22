package com.example.makgeolliguru;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.ARTICLE_LIST;
import static com.example.makgeolliguru.MainActivity.FAVORITE_LIST;
import static com.example.makgeolliguru.MainActivity.MAK_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class Learning_Fragment extends Fragment  {
    public Learning_Fragment(){
        // require a empty public constructor
    }
    static String[][] articleListTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String articleListString = prefs.getString(ARTICLE_LIST, null);

        if(articleListString == null) {
            getData();
            articleListString = prefs.getString(ARTICLE_LIST, null);
            articleListTab = new MakgeolliList(articleListString).ReadFileInto2DArray();

        }else{
            articleListTab = new MakgeolliList(articleListString).ReadFileInto2DArray();

        }

        for (int id = 0; id < 5; id++) {
            String title = articleListTab[id][1]; // Supposons que articleListTab est votre tableau de données

            // Formez l'ID en concaténant "article" avec le numéro d'article
            String textViewId = "article" + (id + 1);

            // Utilisez getResources().getIdentifier() pour obtenir l'ID de ressource
            int resourceId = getResources().getIdentifier(textViewId, "id", getActivity().getPackageName());

            // Assurez-vous que resourceId n'est pas 0 avant d'essayer de trouver le TextView
            if (resourceId != 0) {
                // Trouvez le TextView par son ID
                TextView myTextView = view.findViewById(resourceId);

                // Définissez le texte dans le TextView
                if (myTextView != null) {
                    myTextView.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    Log.e("TextView Error", "TextView not found for ID: " + textViewId);
                }
            } else {
                Log.e("Resource Error", "Resource ID not found for: " + textViewId);
            }

            String cardViewId = "card" + (id + 1);
            int resourceCardId = getResources().getIdentifier(cardViewId, "id", getActivity().getPackageName());

            // Assurez-vous que resourceId n'est pas 0 avant d'essayer de trouver le TextView
            if (resourceCardId != 0) {
                CardView cardView = view.findViewById(resourceCardId);
                final int currentId = id+1;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Action à exécuter lorsque la CardView est cliquée
                        Toast.makeText(getActivity(), "CardView cliquée!", Toast.LENGTH_SHORT).show();
                        Article_Fragment articleFragment = Article_Fragment.newInstance(currentId);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flFragment, articleFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                });
            }
        }
        /*ImageButton closebtn = view.findViewById(R.id.closebtn);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, new Profile_Fragment())
                        .addToBackStack(null)  // Optional: Add to back stack for fragment navigation
                        .commit();
            }
        });*/

        // Inflate the layout for this fragment
        return view;
    }



    public Void getData(){
        InputStream inputStream = getResources().openRawResource(R.raw.makgeolliarticle);
        //CSVFile csvFile = new CSVFile(inputStream);
        //MainActivity.makgeolliList = csvFile.ReadFileInto2DArray();
        SharedPreferences.Editor editor = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
        String text = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        editor.putString(ARTICLE_LIST, text);
        editor.apply();
        //Globals.getInstance().setMakgeolliList(makgeolliList);
        return null;
    }
}