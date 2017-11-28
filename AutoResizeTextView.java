package com.example.android.autoresizetextdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/*  Custom view com.example.android.autoresizetextdemo.AutoResizeTextView
    ------------------------------
    Author Philippe Bartolini
    Last revision : 2017 11 28
    Licence : MIT https://github.com/PhB-fr/AutoResizeText/blob/master/LICENSE
    Just use this new type of View in your layout XML & have fun !
*/

public class AutoResizeTextView extends android.support.v7.widget.AppCompatTextView {

    public AutoResizeTextView(Context context) {
        super(context);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        this.autoResizeText(this);
    }

    /*  Convert pixels to Sp
    --------------------
    Perhaps the ultimate magical formula, I'm not sure about that but it seems ok ;)
    Based on this Q&A : https://stackoverflow.com/questions/6263250/convert-pixels-to-sp
*/
    private int pixelsToSp(int px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float ret = (float) px / scaledDensity * ((float) 2 / 3);
        return ((int) ret);
    }


    /*  Resize automatically the text size inside a com.example.android.autoresizetextdemo.AutoResizeTextView
        --------------------------------------------------------------
        Author : Philippe Bartolini (PhB-fr @ GitHub)
        Last revision : 2017 11 28
        Licence : MIT https://github.com/PhB-fr/AutoResizeText/blob/master/LICENSE
    */

    private void autoResizeText(AutoResizeTextView thisTextView) {

        float thisTextViewTextSize = thisTextView.getTextSize();


        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //thisTextView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        //} else {

        int thisTextViewWidth = thisTextView.getWidth();
        int thisTextViewHeight = thisTextView.getHeight();
        String thiTextViewText = thisTextView.getText().toString();

        TextView fakeTextView = new TextView(thisTextView.getContext());
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
