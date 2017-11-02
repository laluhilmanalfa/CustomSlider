package com.laluhilman.customslider.customviews;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.laluhilman.customslider.R;

public class Slider extends View implements View.OnClickListener {

    private int maxValue = 100;
    private int currentValue = 0;

    private boolean animated;
    private float valueToDraw;
    private long animationDuration = 1000;
    ValueAnimator animation = null;

    private int barHeight;
    private int circleRadius;
    private int circleColor;
    private int baseColor;
    private int fillColor;
    private int shadowColor;
    private int toggleIndicatorValue;

    private Paint barBasePaint;
    private Paint barFillPaint;
    private Paint shadowcirclePaint;
    private Paint circlePaint;
    private Paint innerCirclePaint;
    private Paint hourPaint;
    private Paint minutePaint;
    private Paint currentValuePaint;


    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        invalidate();
        requestLayout();
    }

    public void setValue(int newValue) {
        int previousValue = currentValue;
        if(newValue < 0) {
            currentValue = 0;
        } else if (newValue > maxValue) {
            currentValue = maxValue;
        } else {
            currentValue = newValue;
        }

        if(animation != null) {
            animation.cancel();
        }

        if(animated) {
            animation = ValueAnimator.ofFloat(previousValue, currentValue);
            //animationDuration specifies how long it should take to animate the entire graph, so the
            //actual value to use depends on how much the value needs to change
            int changeInValue = Math.abs(currentValue - previousValue);
            long durationToUse = (long) (animationDuration * ((float) changeInValue / (float) maxValue));
            animation.setDuration(durationToUse);

            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    valueToDraw = (float) valueAnimator.getAnimatedValue();
                    Slider.this.invalidate();
                }
            });

            animation.start();
        } else {
            valueToDraw = currentValue;
        }

        invalidate();
    }

    public int getValue() {
        return currentValue;
    }

    private void init(Context context, AttributeSet attrs) {
        setSaveEnabled(true);

        //read xml attributes
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Slider, 0, 0);
        baseColor = ta.getColor(R.styleable.Slider_baseColor, Color.BLACK);
        fillColor = ta.getColor(R.styleable.Slider_fillColor, Color.BLACK);
        circleColor = ta.getColor(R.styleable.Slider_circleColor, Color.BLACK);
        shadowColor = ta.getColor(R.styleable.Slider_shadowColor, Color.BLACK);
        toggleIndicatorValue = ta.getInt(R.styleable.Slider_shadowColor, 75);
        ta.recycle();


        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBasePaint.setColor(baseColor);

        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(fillColor);

        innerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setColor(Color.WHITE);
        innerCirclePaint.setStrokeWidth(5);
        innerCirclePaint.setStyle(Paint.Style.STROKE);
        innerCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);

        shadowcirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowcirclePaint.setColor(shadowColor);

        hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourPaint.setColor(Color.WHITE);
        hourPaint.setStrokeWidth(5);

        minutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minutePaint.setColor(Color.WHITE);
        minutePaint.setStrokeWidth(5);

    }

    private int measureHeight(int measureSpec) {

        int size = getPaddingTop() + getPaddingBottom();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {

        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        size += bounds.width();

        return resolveSizeAndState(size, measureSpec, 0);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw (Canvas canvas) {
        drawBar(canvas);
    }

    private void drawBar(Canvas canvas) {
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft()  ;
        float barCenter = getBarCenter();
        float halfBarHeight = getHeight() / 2;
        circleRadius = getHeight()/2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);

        float percentFilled = (float) valueToDraw / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;

        if(fillPosition<circleRadius){
            fillPosition = circleRadius;
            circlePaint.setColor(Color.RED);
        }

        if(fillPosition> barLength-circleRadius){
            fillPosition = (barLength-circleRadius);
            circlePaint.setColor(Color.BLUE);
        }

        if(valueToDraw<95)
            canvas.drawCircle(fillPosition+8, barCenter+3, circleRadius, shadowcirclePaint);

        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);
        canvas.drawCircle(fillPosition, barCenter, ((float)(circleRadius * 0.6) ), innerCirclePaint);

        canvas.save();
        canvas.rotate((valueToDraw/100) * 360, fillPosition, barCenter);
        canvas.drawLine(fillPosition, barCenter, fillPosition + (float)(circleRadius * 0.5), barCenter, minutePaint);
        canvas.restore();
        canvas.drawLine(fillPosition, barCenter, fillPosition-((float)(circleRadius * 0.2)), barCenter, hourPaint);


    }


    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
        return barCenter;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touch = false;
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius  ;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                float newValuw = (event.getX(0)/getWidth()) *100;
                invalidate();
                touch = true;

            break;

            case MotionEvent.ACTION_MOVE:
                float barCenter = getBarCenter();
                float halfBarHeight = barHeight / 2;
                float top = barCenter - halfBarHeight;
                float bottom = barCenter + halfBarHeight;
                float left = getPaddingLeft();
                float right = getPaddingLeft() + barLength;

                float percentFilled = (float) valueToDraw / (float) maxValue;
                float fillLength = barLength * percentFilled;
                float fillPosition = getPaddingLeft() + fillLength;
                if(event.getX(0) > fillPosition-circleRadius && event.getX() < fillPosition+circleRadius){

                    newValuw = ((event.getX(0) / right) *100);
                    valueToDraw = newValuw;
                    invalidate();
                    touch = true;

                } else {
                }

                break;


            case MotionEvent.ACTION_UP:

                currentValue = (int)valueToDraw;
                touch = false;
                if(valueToDraw<75){
                    setValue(0);
                } else {
                    setValue(100);

                }

                break;
        }
        return touch;
    }


    public void setAnimated(boolean animated) {
        this.animated = animated;
    }


    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = currentValue;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        currentValue = ss.value;
        valueToDraw = currentValue; //set valueToDraw directly to prevent re-animation
    }

    @Override
    public void onClick(View view) {

    }

    private static class SavedState extends BaseSavedState {
        int value;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


}
