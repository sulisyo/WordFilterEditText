package com.ramodal.gembong.wordfilteredittext;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ramodal.gembong.library.R;

import java.util.ArrayList;

/**
 * Created by gembong on 11/17/15.
 */
public class FilterEditText extends EditText implements  OnFilteredListener{

    private ToolToast toolToast;
    private String inputRange;
    private CharSequence[] forbiddenArray;
    public FilterEditText(Context context) {
        super(context);
    }

    public FilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FilterEditText, 0, 0);
        try {
            inputRange = attributes.getString(R.styleable.FilterEditText_input_range);
            forbiddenArray = attributes.getTextArray(R.styleable.FilterEditText_android_entries);
        } finally {
            attributes.recycle();
        }
        initFilter();
    }

    public FilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFilter();
    }

    private void initFilter(){
        MyFilter myFilter = new MyFilter(this);
        if(forbiddenArray != null && forbiddenArray.length > 0) {
            ArrayList<String> listWord = new ArrayList<>();
            for(CharSequence item : forbiddenArray){
                listWord.add(item.toString());
            }
            myFilter.setForbiddenWordList(listWord);
        }
        if(inputRange != null && inputRange.length() > 0) {
            myFilter.setInputRange(inputRange);
        }
        toolToast = new ToolToast(getContext());
        toolToast.setDuration(Toast.LENGTH_SHORT);
        setFilters(new InputFilter[]{myFilter});
        if((getInputType() & InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){
            addTextChangedListener(new emailTextWatcher());
        }
    }

    @Override
    public void onFiltered(String text) {
        int top = (int) getY();
        int loc[] = new int[2];
        getLocationOnScreen(loc);
        toolToast.setGravity(Gravity.LEFT | Gravity.TOP, loc[0], loc[1]);
        toolToast.setCustomText("\"" + text + "\" not boleh");
        toolToast.show();
    }

    private class ToolToast extends Toast{

        /**
         * Construct an empty Toast object.  You must call {@link #setView} before you
         * can call {@link #show}.
         *
         * @param context The context to use.  Usually your {@link Application}
         *                or {@link Activity} object.
         */

        private TextView text;

        public ToolToast(Context context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.simple_text_toast, null);
            text = (TextView) layout;
            setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            setDuration(Toast.LENGTH_LONG);
            setView(layout);
        }

        public void setCustomText(String t){
            text.setText(t);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void setRightDrawable(int resId){
        setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
    }

    private void setEmailValidSign(){
        setRightDrawable(android.R.drawable.checkbox_on_background);
    }

    private void clearEmailValidSign(){
        setRightDrawable(0);
    }


    private class emailTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(isValidEmail(s)){
                setError(null);
                setEmailValidSign();
            }else {
                clearEmailValidSign();
               // setError("gakboleh");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}
