package com.example.makgeolliguru;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.ARTICLE_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makgeolliguru.map.MakgeolliList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class LearningFragment extends Fragment  {
    public LearningFragment(){
        // require a empty public constructor
    }
    static String[][] articleListTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String articleListString = prefs.getString(ARTICLE_LIST, null);

        if (articleListString == null) {
            getData();
            articleListString = prefs.getString(ARTICLE_LIST, null);

        }
        articleListTab = new MakgeolliList(articleListString).ReadFileInto2DArray();

        LinearLayout dynamicContent = view.findViewById(R.id.dynamic_content);

        CardView cardView = null;
        for (int id = 0; id < articleListTab.length; id++) {
            String title = articleListTab[id][1]; // Assuming articleListTab is your data array

            // Create a new CardView
            cardView = (CardView) inflater.inflate(R.layout.cardview_layout, dynamicContent, false);

            // Set the content of the CardView
            TextView textView = cardView.findViewById(R.id.articleTextView);
            textView.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));


            final int currentId = id;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Action à exécuter lorsque la CardView est cliquée
                    Toast.makeText(getActivity(), "CardView cliquée!", Toast.LENGTH_SHORT).show();
                    ArticleFragment articleFragment = ArticleFragment.newInstance(currentId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, articleFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
            dynamicContent.addView(cardView);
        }

        /*ImageButton backbtn = view.findViewById(R.id.backbtn);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, new LearningFragment())
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