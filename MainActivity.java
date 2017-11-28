package com.example.android.autoresizetext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String CONTENTDESCRIPTIONTAG = "AutoResizeText"; // Should be a string resource.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    /*  Main Method
        -----------
        Launched each time the app window gets focus, ie at startup, reload and when orientation changes.
    */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            PaintIterator(findViewById(R.id.MainLayout));
        }
    }

    /*  Iterates through the given Layout, looking for TextView
        ---------------------------------
        Author : Philippe Bartolini (PhB-fr @ GitHub)
        Yes another iterator ;) I think it is very adaptable
    */
    public void PaintIterator(View thisView){
        ViewGroup thisViewGroup = null;
        boolean isTextView = false;
        int childrenCount = 0;

        try {
            thisViewGroup = (ViewGroup) thisView;
            childrenCount = thisViewGroup.getChildCount();
        }
        catch (Exception e){

        }

        if(childrenCount == 0){
            try {
                isTextView = ((TextView) thisView).getText() != null;
            }
            catch (Exception e){

            }

            if(isTextView){
                Paint(thisView.getId());
            }
        }
        else {
            for(int i = 0; i < childrenCount; i++){
                PaintIterator(thisViewGroup.getChildAt(i));
            }
        }
    }

    /*  Post a runnable to do the job when the UI is loaded
        ---------------
    */
    public void Paint(int textViewId) {
        final View contentView = findViewById(textViewId);

        contentView.post(new Runnable() {
            public void run() {
                autoResizeText(contentView.getId());
            }
        });

    }
    // https://stackoverflow.com/questions/21937224/does-posting-runnable-to-an-ui-thread-guarantee-that-layout-is-finished-when-it


    /*  Convert pixels to Sp
        --------------------
        Perhaps the ultimate magical formula, I'm not sure about that but it seems ok ;)
        Based on this Q&A : https://stackoverflow.com/questions/6263250/convert-pixels-to-sp
    */
    public int pixelsToSp(int px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float ret = (float) px / scaledDensity * ((float) 2 / 3);
        return ((int) ret);
    }

    /*  Resize automatically the text size inside a TextView
        ----------------------------------------------------
        Author : Philippe Bartolini (PhB-fr @ GitHub)
        Android O introduces the ability to dynamically adjust the text size of a TexView.
        For the older versions, I wrote my own implementation.
    */

    public void autoResizeText(int textViewId) {

        TextView thisTextView = findViewById(textViewId);
        float thisTextViewTextSize = thisTextView.getTextSize();
        String thisTextViewContentDescription = (String) thisTextView.getContentDescription();

        // This condition is my choice, but it's up to you to implement it another way.
        Boolean isContentDescriptionSet = (thisTextViewContentDescription != null);
        Boolean doResize = (isContentDescriptionSet && thisTextViewContentDescription.equals(CONTENTDESCRIPTIONTAG));

        if(doResize){

            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //thisTextView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            //} else {

                int thisTextViewWidth = thisTextView.getWidth();
                int thisTextViewHeight = thisTextView.getHeight();
                String thiTextViewText = thisTextView.getText().toString();

                TextView fakeTextView = new TextView(getBaseContext());
                fakeTextView.setText(thisTextView.getText());
                fakeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, pixelsToSp(thisTextViewHeight));
                TextPaint fakeTextViewPaint = fakeTextView.getPaint();

                int fakeTextViewWidth = (int) fakeTextViewPaint.measureText(thiTextViewText, 0, thiTextViewText.length());
                float ratio = Math.min((float) thisTextViewWidth / fakeTextViewWidth, (float) 1);
                int finalSp = pixelsToSp((int) (ratio * thisTextViewHeight));
                int currentSp = pixelsToSp((int) (thisTextViewTextSize));

                thisTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Math.max(finalSp, currentSp));
                thisTextView.setGravity(Gravity.CENTER_VERTICAL);
            //}
        }

    }
}
