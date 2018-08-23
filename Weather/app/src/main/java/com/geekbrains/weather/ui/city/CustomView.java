package com.geekbrains.weather.ui.city;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.geekbrains.weather.R;

/**
 * Created by shkryaba on 21/07/2018.
 */

public class CustomView extends View {

    private final static String TAG = "CustomView";
    private Paint paint;
    private int radius;
    private int color;
    private boolean pressed = false;    // Признак нажатия
    View.OnClickListener listener;      // Слушатель

    public CustomView(Context context) {
        super(context);
        init();
    }

    // Вызывается при вставке элемента в макет
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    // Вызывается при вставке элемента в макет, если был добавлен стиль
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    // Обработка параметров в xml
    private void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        setRadius(typedArray.getResourceId(R.styleable.CustomView_cv_Radius, 100));
        setColor(typedArray.getResourceId(R.styleable.CustomView_cv_Color, Color.BLUE));
        typedArray.recycle();
    }

    // Подготовка элемента
    private void init() {
        Log.d(TAG, "Constructor");
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setRadius(int radius){
        this.radius = radius;
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.d(TAG, "layout");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawCircle(radius, radius, radius, paint);
        if(pressed){
            canvas.drawCircle(radius, radius, radius/10, paint);
        }
        else {
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    @Override
    public void invalidate() {
        Log.d(TAG, "invalidate");
        super.invalidate();
    }

    @Override
    public void requestLayout() {
        Log.d(TAG, "requestLayout");
        super.requestLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        int Action = event.getAction();
        if(Action == MotionEvent.ACTION_DOWN){ // Нажали
            pressed = true;
            invalidate();           // Перерисовка элемента
            if (listener != null) listener.onClick(this);
        }
        else if(Action == MotionEvent.ACTION_UP){ // Отпустили
            pressed = false;
            invalidate();           // Перерисовка элемента
        }
        return true;
    }

    // Установка слушателя
    @Override
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
}
