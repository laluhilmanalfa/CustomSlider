package com.laluhilman.customslider.customviews;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.laluhilman.customslider.R;

/**
 * Created by laluhilman on 25/10/17.
 */

public class JJASliderCheckOut extends RelativeLayout {


    ShimmerTextView tv;
    Shimmer shimmer;


    public JJASliderCheckOut(Context context) {
        super(context);
        initControl(context);

    }

    public JJASliderCheckOut(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);

    }

    public JJASliderCheckOut(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JJASliderCheckOut(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.slider_layout, this);

        assignUiElements();

    }

    private void assignUiElements() {
        tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);

        shimmer = new Shimmer();
        shimmer.start(tv);

        final Slider valueBar = (Slider) findViewById(R.id.valueBar);
        valueBar.setMaxValue(100);
        valueBar.setAnimated(true);
        valueBar.setAnimationDuration(1000l);

    }

}
