package com.example.makgeolliguru;

import static android.content.Context.MODE_PRIVATE;
import static com.example.makgeolliguru.MainActivity.ARTICLE_LIST;
import static com.example.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.makgeolliguru.map.MakgeolliList;

public class ArticleFragment extends Fragment {

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance(int itemId) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt("ITEM_ID", itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        // Retrieve the ID passed through the arguments
        Bundle args = getArguments();
        int itemId = -1;
        if (args != null) {
            itemId = args.getInt("ITEM_ID", -1);
        }

        String[][] articleListTab;
        SharedPreferences prefs = getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String articleListString = prefs.getString(ARTICLE_LIST, null);
        articleListTab = new MakgeolliList(articleListString).ReadFileInto2DArray();

        //if (itemId != -1 && articleListTab != null && itemId < articleListTab.length) {
            String[] articleData = articleListTab[itemId];

            // Display the ID or use it to load data
            TextView textView = view.findViewById(R.id.titleTextView);
            textView.setText(articleData[1]);
            TextView intro = view.findViewById(R.id.IntroView);
            intro.setText(articleData[2]);
            TextView textView1 = view.findViewById(R.id.paragraphTitle1);
            textView1.setText(articleData[3]);
            TextView textView2 = view.findViewById(R.id.paragraphText1);
            textView2.setText(articleData[4]);
            TextView textView3 = view.findViewById(R.id.paragraphTitle2);
            textView3.setText(articleData[6]);
            TextView textView4 = view.findViewById(R.id.paragraphText2);
            textView4.setText(articleData[7]);
            TextView textView5 = view.findViewById(R.id.paragraphTitle3);
            textView5.setText(articleData[9]);
            TextView textView6 = view.findViewById(R.id.paragraphText3);
            textView6.setText(articleData[10]);
            TextView textView7 = view.findViewById(R.id.ConclusionView);
            textView7.setText(articleData[12]);
        //}
        return view;
    }
}

