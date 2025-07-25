package com.myapp.makgeolliguru.profile;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class QuestionnaireRules {
    private Map<Character, Integer> answers;

    public QuestionnaireRules() {
        // Initialize the map to store the count of each option
        answers = new HashMap<>();
        answers.put('A', 0);
        answers.put('B', 0);
        answers.put('C', 0);
    }

    // Method to record user's answer for a question
    public void recordAnswer(char option) {
        // Increment the count for the selected option
        answers.put(option, answers.get(option) + 1);
    }

    // Method to calculate the makgeolli category based on user's answers
    public String calculateMakgeolliCategory(Context context) {
        int countA = answers.get('A');
        int countB = answers.get('B');
        int countC = answers.get('C');

        // Determine the category based on the majority of responses
        if (countA > countB && countA > countC) {
            return context.getString(com.myapp.makgeolliguru.R.string.sparkling_profile);

        } else if (countB > countA && countB > countC) {
            return context.getString(com.myapp.makgeolliguru.R.string.fruity_profile);
        } else if (countC > countA && countC > countB) {
            return context.getString(com.myapp.makgeolliguru.R.string.sweet_profile);
        } else {
            // If there is a tie, suggest trying different types
            return context.getString(com.myapp.makgeolliguru.R.string.other_profile);
        }
    }
}

