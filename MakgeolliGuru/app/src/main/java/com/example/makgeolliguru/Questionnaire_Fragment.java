package com.example.makgeolliguru;

import com.example.makgeolliguru.QuestionnaireRules;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Questionnaire_Fragment extends Fragment {

    public Questionnaire_Fragment() {
        // Required empty public constructor
    }

    public static Questionnaire_Fragment newInstance(int questionId) {
        Questionnaire_Fragment fragment = new Questionnaire_Fragment();
        Bundle args = new Bundle();
        args.putInt("QUESTION_ID", questionId);
        fragment.setArguments(args);
        return fragment;
    }
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;

    private QuestionnaireRules questionnaire;

    private String[] questions = {
            "Makgeolli Setting:",
            "Stress-relief Style:",
            "Preferred Makgeolli Mood:",
            "Life Mantra:",
            "Decision-making Approach:",
            "Vacation Destination Preference:"
    };

    private String[][] options = {
            {"Exploring nature", "Reading a good book", "Socializing with friends"},
            {"Exercise or outdoor activities", "Meditation and relaxation", "Talking it out with friends or family"},
            {"Action and adventure", "Drama and documentaries", "Comedy and light-hearted films"},
            {"\"Carpe Diem\" - Seize the day!", "\"To thine own self be true\" - Authenticity is key.", "\"Hakuna Matata\" - No worries, live carefree."},
            {"Analyzing pros and cons", "Listening to your intuition", "Seeking advice from others"},
            {"Mountains or wilderness", "Cultural cities and museums", "Beach or resort relaxation"}
    };

    private char[] userAnswers = new char[questions.length];

    // Retrieve the ID passed through the arguments

    private int questionId = -1;
    public int getIdQuestion(){
        Bundle args = getArguments();
        if (args != null) {
            return args.getInt("QUESTION_ID", -1);
        }
        return -1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        questionId = getIdQuestion();

        questionTextView = view.findViewById(R.id.questionTextView);
        optionsRadioGroup = view.findViewById(R.id.optionsRadioGroup);
        nextButton = view.findViewById(R.id.nextButton);

        questionnaire = new QuestionnaireRules();

        displayQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Record user's answer
                int checkedRadioButtonId = optionsRadioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton radioButton = view.findViewById(checkedRadioButtonId);
                    int selectedOptionIndex = optionsRadioGroup.indexOfChild(view.findViewById(checkedRadioButtonId));
                    userAnswers[questionId] = (char) ('A' + selectedOptionIndex);
                    questionnaire.recordAnswer(userAnswers[questionId]);
                }

                // Move to the next question or show results if all questions are answered
                if (questionId < questions.length - 1) {
                    questionId++;
                    //todo : increment la page
                    Questionnaire_Fragment questionnaireFragment = Questionnaire_Fragment.newInstance(questionId);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragment, questionnaireFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // All questions answered, show results
                    showResults(view);
                }
            }
        });
        return view;
    }

    private void displayQuestion() {
        if (questionId >= 0 && questionId < questions.length) {
            questionTextView.setText(questions[questionId]);

            optionsRadioGroup.clearCheck();
            optionsRadioGroup.removeAllViews();
            String[] currentOptions = options[questionId];
            for (String option : currentOptions) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(option);
                optionsRadioGroup.addView(radioButton);
            }
        }

    }

    private void showResults(View view) {
        // Calculate makgeolli category based on user's answers
        String makgeolliCategory = questionnaire.calculateMakgeolliCategory();

        // Display the result in a pop-up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Makgeolli Recommendation");
        builder.setMessage(makgeolliCategory);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when OK button is clicked, if needed
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        // Display the result or navigate to the next activity to show the result
        // Example: Intent intent = new Intent(QuestionnaireActivity.this, ResultActivity.class);
        // intent.putExtra("makgeolliCategory", makgeolliCategory);
        // startActivity(intent);
    }
}

