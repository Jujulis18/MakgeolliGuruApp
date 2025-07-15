package com.myapp.makgeolliguru.profile;

import static android.content.Context.MODE_PRIVATE;
import static com.myapp.makgeolliguru.MainActivity.MAK_PROFILE;
import static com.myapp.makgeolliguru.MainActivity.SHARED_PREF;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.myapp.makgeolliguru.R;

import java.util.Locale;

public class QuestionnaireFragment extends Fragment {

    public QuestionnaireFragment() {
        // Required empty public constructor
    }

    public static QuestionnaireFragment newInstance(int questionId) {
        QuestionnaireFragment fragment = new QuestionnaireFragment();
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
            "How do you typically spend your free time?",
            "How do you typically unwind after a stressful day?",
            "What genre of movies do you enjoy the most?",
            "What could be your life mantra or guiding principle?",
            "How do you approach making important decisions?",
            "Where do you dream of going for a vacation?"
    };

    private String[][] options = {
            {"Exploring nature", "Reading a good book", "Socializing with friends"},
            {"Exercise or outdoor activities", "Meditation and relaxation", "Talking it out with friends or family"},
            {"Action and adventure", "Drama and documentaries", "Comedy and light-hearted films"},
            {"\"Carpe Diem\" - Seize the day!", "\"To thine own self be true\" - Authenticity is key.", "\"Hakuna Matata\" - No worries, live carefree."},
            {"Analyzing pros and cons", "Listening to your intuition", "Seeking advice from others"},
            {"Mountains or wilderness", "Cultural cities and museums", "Beach or resort relaxation"}
    };

    private String[] questions_kr = {
            "보통 여가 시간을 어떻게 보내시나요?",
            "스트레스가 많은 하루를 마친 후, 보통 어떻게 풀리시나요?",
            "가장 좋아하는 영화 장르는 무엇인가요?",
            "당신의 인생 만트라는 무엇인가요, 또는 중요한 원칙은 무엇인가요?",
            "중요한 결정을 내릴 때, 보통 어떤 방식으로 접근하시나요?",
            "휴가를 가고 싶은 꿈의 장소는 어디인가요?"
    };

    private String[][] options_kr = {
            {"자연을 탐험하기", "좋은 책 읽기", "친구들과 사회적 활동하기"},
            {"운동이나 야외 활동", "명상과 휴식", "친구나 가족과 이야기 나누기"},
            {"액션과 모험", "드라마와 다큐멘터리", "코미디와 가벼운 영화"},
            {"\"Carpe Diem\" - 오늘을 즐기세요!", "\"To thine own self be true\" - 진실된 자신이 되세요.", "\"Hakuna Matata\" - 걱정하지 말고, 마음 편히 살아요."},
            {"장단점 분석하기", "직감을 따르기", "다른 사람의 조언을 구하기"},
            {"산이나 자연", "문화 도시와 박물관", "해변이나 리조트에서 휴식"}
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
                    QuestionnaireFragment questionnaireFragment = QuestionnaireFragment.newInstance(questionId);
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
            String[] currentOptions;
            if (Locale.getDefault().getLanguage().equals("ko")) {
                questionTextView.setText(questions_kr[questionId]);
                currentOptions = options_kr[questionId];
            }
            else{
                questionTextView.setText(questions[questionId]);
                currentOptions = options[questionId];
            }

            optionsRadioGroup.clearCheck();
            optionsRadioGroup.removeAllViews();

            for (String option : currentOptions) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(option);
                optionsRadioGroup.addView(radioButton);
            }
        }

    }

    private void showResults(View view) {
        // Calculate makgeolli category based on user's answers
        String makgeolliCategory = questionnaire.calculateMakgeolliCategory(getContext());

        // Display the result in a pop-up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);


        builder.setTitle("Makgeolli Recommendation");
        builder.setMessage(makgeolliCategory);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when OK button is clicked, if needed
                SharedPreferences prefs = view.getContext().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                String mak_profile = prefs.getString(MAK_PROFILE, null);
                if (makgeolliCategory == getContext().getString(R.string.sparkling_profile)){
                    mak_profile = "Sparkling";
                }
                else if (makgeolliCategory== getContext().getString(R.string.fruity_profile)){
                    mak_profile = "Fruity";
                }
                else if (makgeolliCategory== getContext().getString(R.string.sweet_profile)){
                    mak_profile = "Sweet";
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(MAK_PROFILE, mak_profile);
                editor.apply();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flFragment, profileFragment)
                        .addToBackStack(null) // Ajout à l'historique de navigation
                        .commit();

                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary));

    }
}

