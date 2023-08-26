package com.example.makgeolliguru;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDatabase {
    private List<String[]> favoriteTab;

    public FavoriteDatabase() {
        favoriteTab = new ArrayList<>();
    }

    public List<String[]> getFavoriteTab() {
        return this.favoriteTab;
    }

    public void setVar(int id, String[] makgeolliInfo) {
        String[] newTab = new String[makgeolliInfo.length+1];
        newTab[0] = String.valueOf(id);
        for (int i=0;i< makgeolliInfo.length;i++) {
            newTab[i+1] = makgeolliInfo[i];
        }
        favoriteTab.add(newTab) ;
    }

    public void deleteFavorite(int id) {
        int indexTab;
        for (String[] makgeolli : favoriteTab) {
            if (makgeolli[0].equals(String.valueOf(id))) {
                favoriteTab.remove(makgeolli);
            }
        }
    }
}
