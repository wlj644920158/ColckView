package nanjing.jun.viewtest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 王黎军 on 2017/7/6.
 */

public class ClockView extends View {
    private static final float CLOCK_CENTER_RATIO = 0.320f;
    private static final float CLOCK_CENTER_RATIO_ = 0.317f;
    private static final float DEGREEE_PER_HOUR = 30.f;
    private static final float DEGREEE_PER_MIN = 6.f;

    private Bitmap clockBitmap;//表盘背景
    private Bitmap hourPointerBitmap;//小时指针
    private Bitmap minPointerBitmap;//分钟指针
    private Bitmap coverBitmap;//中间小圆点


    private Bitmap bgCircleBitmap;//兼容低版本加入圆环的图片

    private int width;//控件宽度
    private int height;//控件高度

    private Calendar calendar;
    private Paint secPaint;
    private Paint bgCirclePaint;
    private ValueAnimator valueAnimator;
    private float secDegree;
    private float bgCircleRadius;

    private ArrayList<TimeDurationDegree> degreeArrayList;//时间段列表

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        clockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door5_);
        hourPointerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door6);
        minPointerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door7);
        coverBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door8);

        //根据版本来巨鼎橙色圆环的实现方式
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bgCircleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_door1);
        } else {
            bgCirclePaint = new Paint();
            bgCirclePaint.setStyle(Paint.Style.FILL);
            bgCirclePaint.setColor(Color.parseColor("#ffd85819"));
            bgCirclePaint.setAntiAlias(true);
        }

        width = clockBitmap.getWidth();
        height = clockBitmap.getHeight();
        bgCircleRadius = height * CLOCK_CENTER_RATIO;
        calendar = Calendar.getInstance();
        secPaint = new Paint();
        secPaint.setColor(Color.RED);
        secPaint.setStrokeWidth(5);

        degreeArrayList = new ArrayList<>();
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
        drawBgYellowCircle(canvas);
        drawClock(canvas);
        drawHourPointer(canvas);
        drawMinPointer(canvas);
        drawSecPointer(canvas);
        drawCover(canvas);
    }

    /**
     * 绘制橙色圆环
     * @param canvas
     */
    private void drawBgYellowCircle(Canvas canvas) {
        if (degreeArrayList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.save();
                for (TimeDurationDegree degree : degreeArrayList) {
                    canvas.drawArc(getWidth() / 2 - bgCircleRadius, 0, getWidth() / 2 + bgCircleRadius, bgCircleRadius * 2, degree.getStartDegree(), degree.getSweepDegree(), true, bgCirclePaint);
                }
                canvas.restore();
            } else {
                for (TimeDurationDegree degree : degreeArrayList) {
                    canvas.save();
                    canvas.translate(getWidth() / 2, getHeight() * CLOCK_CENTER_RATIO);
                    int rotateCount = (int) (degree.getSweepDegree() / DEGREEE_PER_HOUR);
                    if (rotateCount > 0) {
                        canvas.rotate(degree.getStartDegree());
                        for (int i = 0; i < rotateCount; i++) {
                            canvas.drawBitmap(bgCircleBitmap, 0, 0, null);
                            canvas.rotate(DEGREEE_PER_HOUR);
                        }
                    }
                    canvas.restore();
                }
            }
        }
    }

    /**
     * 绘制时钟表盘
     * @param canvas
     */
    private void drawClock(Canvas canvas) {
        canvas.drawBitmap(clockBitmap, 0, 0, null);
    }

    /**
     * 绘制时针
     * @param canvas
     */
    private void drawHourPointer(Canvas canvas) {
        float d = hour2Degrees(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * CLOCK_CENTER_RATIO);
        canvas.rotate(d - 90);
        canvas.drawBitmap(hourPointerBitmap, 0, -hourPointerBitmap.getHeight() / 2, null);
        canvas.restore();
    }


    /**
     * 绘制分针
     * @param canvas
     */

    private void drawMinPointer(Canvas canvas) {
        float d = min2Degrees(calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * CLOCK_CENTER_RATIO);
        canvas.rotate(d - 90);
        canvas.drawBitmap(minPointerBitmap, 0, -minPointerBitmap.getHeight() / 2, null);
        canvas.restore();
    }

    /**
     * 绘制秒针
     * @param canvas
     */
    private void drawSecPointer(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() * CLOCK_CENTER_RATIO);
        canvas.rotate(secDegree - 90);
        canvas.drawLine(0, -2, minPointerBitmap.getWidth(), 2, secPaint);
        canvas.restore();
    }


    /**
     * 绘制中间的小圆点
     * @param canvas
     */
    private void drawCover(Canvas canvas) {
        canvas.drawBitmap(coverBitmap, getWidth() / 2 - coverBitmap.getWidth() / 2, getHeight() * CLOCK_CENTER_RATIO_ - coverBitmap.getHeight() / 2, null);
    }


    /**
     * 计算分针对应的监督
     * @param min
     * @param sec
     * @return
     */
    private float min2Degrees(int min, int sec) {
        return DEGREEE_PER_MIN * (min + sec / 60.0f);
    }

    /**
     * 计算时针对应的角度
     * @param hour
     * @param min
     * @return
     */
    private float hour2Degrees(int hour, int min) {
        hour = hour % 12;
        return DEGREEE_PER_HOUR * (hour + min / 60.0f);
    }

    /**
     * 计算秒针对应的角度
     * @param sec
     * @return
     */
    private float sec2Degree(int sec) {
        return DEGREEE_PER_MIN * sec;
    }


    /**
     * 设置时间段
     * @param durationList
     */
    public void setDurationList(List<TimeDuration> durationList) {
        valueAnimator.pause();
        degreeArrayList.clear();
        for (TimeDuration timeDuration : durationList) {
            TimeDurationDegree degree = new TimeDurationDegree();
            degree.setStartDegree(timeDuration.getStart() * DEGREEE_PER_HOUR - 90);
            degree.setSweepDegree((timeDuration.getEnd() - timeDuration.getStart()) * DEGREEE_PER_HOUR);
            degreeArrayList.add(degree);
        }
        valueAnimator.start();
    }

}
