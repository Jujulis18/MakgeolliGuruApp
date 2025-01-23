package com.example.makgeolliguru.profile;
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
    public String calculateMakgeolliCategory() {
        int countA = answers.get('A');
        int countB = answers.get('B');
        int countC = answers.get('C');

        // Determine the category based on the majority of responses
        if (countA > countB && countA > countC) {
            return "Mostly A's: You might enjoy a makgeolli that fits well with outdoor activities, perhaps a sparkling and refreshing option.";
        } else if (countB > countA && countB > countC) {
            return "Mostly B's: A smooth and contemplative makgeolli with fruity or sour notes could be your ideal choice.";
        } else if (countC > countA && countC > countB) {
            return "Mostly C's: A lively and sociable makgeolli with sweet and light-hearted characteristics may suit your taste.";
        } else {
            // If there is a tie, suggest trying different types
            return "Your preferences are diverse. You may enjoy exploring different types of makgeolli.";
        }
    }
}

