package com.ramodal.gembong.wordfilteredittext;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.List;

/**
 * Created by gembong on 11/17/15.
 */
public class MyFilter implements InputFilter {
    private List<String> forbiddenList;
    private String inputRange;
    private OnFilteredListener listener;
    private String currentForbidWord;
    public MyFilter(OnFilteredListener listener){
        this.listener = listener;
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if(isForbid(source)){
            listener.onFiltered(currentForbidWord);
            return "";
        }
        for (int i = start; i < end; i++) {
            if (!isOnRange(source.charAt(i))) { // Accept only letter & digits ; otherwise just return
                //Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return null;
    }


    public void setForbiddenWordList(List<String> forbid){
        this.forbiddenList = forbid;
    }

    public void setInputRange(String range){
        inputRange = range;
    }

    private boolean isOnRange(char c){
        if(inputRange == null){
            return true;
        }
        int length = inputRange.length();
        for(int i = 0; i < length ; ++i){
            if(c == inputRange.charAt(i)){
                return true;
            }
        }
        return false;
    }

    private boolean isForbid(CharSequence str){
        String in = str.toString();
        String[] strArray = in.split(" ");
        if(forbiddenList == null){
            return false;
        }

        for (String aStrArray : strArray) {
            for (String forbidItem : forbiddenList) {
                if (forbidItem.compareToIgnoreCase(aStrArray) == 0) {
                    currentForbidWord = forbidItem;
                    return true;
                }
            }
        }
        return false;
    }

}
