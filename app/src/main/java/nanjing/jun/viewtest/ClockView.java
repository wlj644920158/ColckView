package nanjing.jun.viewtest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 王黎军 on 2017/7/6.
 */

public class ClockView extends View {
    private Bitmap clockBitmap;//表盘背景
    private Bitmap hourPointerBitmap;//小时指针
    private Bitmap minPointerBitmap;//分钟指正
    private Bitmap coverBitmap;//中间小圆点
    private int width;
    private int height;
    private static final float ratio = 0.317f;
    private static final float DEGREEE_PER_HOUR = 30.f;
    private static final float DEGREEE_PER_MIN = 6.f;
    private Calendar calendar;
    private Paint paint;
    private ValueAnimator valueAnimator;
    private float secDegree;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        clockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door5);
        hourPointerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door6);
        minPointerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door7);
        coverBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door8);
        width = clockBitmap.getWidth();
        height = clockBitmap.getHeight();
        calendar = Calendar.getInstance();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        valueAnimator = ValueAnimator.ofFloat(0f, 6f);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float avalue = (float) animation.getAnimatedValue();
                secDegree = sec2Degree(calendar.get(Calendar.SECOND)) + avalue;
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                calendar.setTime(new Date());
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawClockBg(canvas);
        drawHourPointer(canvas);
        drawMinPointer(canvas);
        drawSecPointer(canvas);
        drawCover(canvas);
    }

    private void drawClockBg(Canvas canvas) {
        canvas.drawBitmap(clockBitmap, 0, 0, null);
    }

    private void drawHourPointer(Canvas canvas) {
        float d = hour2Degrees(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * ratio);
        canvas.rotate(d - 90);
        canvas.drawBitmap(hourPointerBitmap, 0, -hourPointerBitmap.getHeight() / 2, null);
        canvas.restore();
    }

    private void drawMinPointer(Canvas canvas) {
        float d = min2Degrees(calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * ratio);
        canvas.rotate(d - 90);
        canvas.drawBitmap(minPointerBitmap, 0, -minPointerBitmap.getHeight() / 2, null);
        canvas.restore();
    }

    private void drawSecPointer(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * ratio);
        canvas.rotate(secDegree - 90);
        canvas.drawLine(0, -2, minPointerBitmap.getWidth(), 2, paint);
        canvas.restore();
    }

    private void drawCover(Canvas canvas) {
        canvas.drawBitmap(coverBitmap, getWidth() / 2 - coverBitmap.getWidth() / 2, getHeight() * ratio - coverBitmap.getHeight() / 2, null);
    }


    private float min2Degrees(int min, int sec) {
        return DEGREEE_PER_MIN * (min + sec / 60.0f);
    }

    private float hour2Degrees(int hour, int min) {
        hour = hour % 12;
        return DEGREEE_PER_HOUR * (hour + min / 60.0f);
    }

    private float sec2Degree(int sec) {
        return DEGREEE_PER_MIN * sec;
    }

}
