package com.example.android.autoresizetextdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/*  Custom view AutoResizeTextView
    ------------------------------
    Author Philippe Bartolini
    Last revision : 2017 12 01
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

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        autoResizeText(this, w, h);
    }

    /*  Convert pixels to Sp
    --------------------
    Perhaps the ultimate magical formula, I'm not sure about that but it seems ok ;)
    Based on this Q&A : https://stackoverflow.com/questions/6263250/convert-pixels-to-sp
*/
    private float pixelsToSp(float px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float ret = px / scaledDensity * ((float) 2 / 3);
        return (ret);
    }


    /*  Resize automatically the text size inside a AutoResizeTextView
        --------------------------------------------------------------
        Author : Philippe Bartolini (PhB-fr @ GitHub)
        Last revision : 2017 12 01
        Licence : MIT https://github.com/PhB-fr/AutoResizeText/blob/master/LICENSE
    */

    private void autoResizeText(AutoResizeTextView thisTextView, int thisTextViewWidth, int thisTextViewHeight) {

        float thisTextViewTextSize = thisTextView.getTextSize();

        String thisTextViewText = thisTextView.getText().toString();

        TextView fakeView = thisTextView;
        fakeView.setTextSize(pixelsToSp(thisTextViewHeight));
        Paint fake = fakeView.getPaint();

        float ratio = Math.min((float) thisTextViewWidth / fake.measureText(thisTextViewText), 1);
        float finalSp = pixelsToSp(ratio * thisTextViewHeight);
        float currentSp = pixelsToSp(thisTextViewTextSize);

        thisTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) Math.max(finalSp, currentSp));

    }
}