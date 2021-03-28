package eu.franhakase.yada;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class XImageButton extends androidx.appcompat.widget.AppCompatImageButton
{



    public XImageButton(Context context) {
        super(context);
    }

    public XImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_UP:
                performClick();
                return true;
        }
        return false;
    }


    @Override
    public boolean performClick()
    {
        return super.performClick();
    }
}
