package com.weex.wdialog_lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by xuwx on 2018/10/23.
 */

public class WaveView extends View {

    /**
     * 初始高度
     */
    private int mBaseline = 400;
    /**
     * view的宽高
     */
    private int mWidth = 200;
    private int mHeight = 200;

    private Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 外圈宽度
     */
    private int arcWidth = 20;
    /**
     * 波浪的高度
     */
    private int mWaveHeight = 75;
    /**
     * 每一个波形的宽度
     */
    private int itemWidth = 0;
    /**
     * 主波形的偏移量
     */
    private int majorWaveOffset = 0;
    /**
     * 次波形的偏移量
     */
    private int juniorWaveOffset = 0;
    private int waveOffset = 0;

    private Path path;
    private boolean halfWeight = false;
    private ValueAnimator waveVerticalAnim;
    private ValueAnimator waveHorizontalAnim;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setHalfWeight(boolean halfWeight) {
        this.halfWeight = halfWeight;
        mBaseline = mHeight / 2;
        postInvalidate();
    }

    public void setJuniorWaveOffset(int juniorWaveOffset) {
        this.juniorWaveOffset = -1 * Math.abs(juniorWaveOffset);
        if (juniorWaveOffset >= (mWidth - waveOffset) || juniorWaveOffset <= (-mWidth + waveOffset))
            this.juniorWaveOffset = waveOffset;
        postInvalidate();
    }

    public void setMajorWaveOffset(int majorWaveOffset) {
        this.majorWaveOffset = majorWaveOffset;
        if (majorWaveOffset >= mWidth || majorWaveOffset <= -mWidth) this.majorWaveOffset = 0;
        postInvalidate();
    }

    private void initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint.setColor(Color.parseColor("#7fffffff"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(Color.parseColor("#66ECCC"));
        arcPaint.setStrokeWidth(arcWidth);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor("#02CFA8"));

        waveVerticalAnim = ValueAnimator.ofFloat(1f, 0.5f);
        waveVerticalAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                mBaseline = (int) (mHeight * percent);
                postInvalidate();
            }
        });
        waveVerticalAnim.setDuration(2000);
        waveVerticalAnim.setInterpolator(new DecelerateInterpolator());
        waveVerticalAnim.setRepeatCount(0);

        waveHorizontalAnim = ValueAnimator.ofFloat(0f, 1f);
        waveHorizontalAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                setMajorWaveOffset((int) (-mWidth * percent));
                setJuniorWaveOffset((int) (-mWidth * percent) + waveOffset);
            }
        });
        waveHorizontalAnim.setDuration(2000);
        waveHorizontalAnim.setRepeatCount(0);

        path = new Path();
    }

    public void startAnim(boolean waveUp) {
        if (waveUp) {
            waveVerticalAnim.start();
        }
        waveHorizontalAnim.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(mWidth, widthMeasureSpec);
        mHeight = getDefaultSize(mHeight, heightMeasureSpec);
        mWidth = mHeight = Math.min(mHeight, mWidth);
        setMeasuredDimension(mWidth, mHeight);
        if (halfWeight) mBaseline = mHeight / 2;

        itemWidth = mWidth / 8;
        waveOffset = -1 * itemWidth + 30;
        juniorWaveOffset = waveOffset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = (mWidth - arcWidth) / 2;

        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, circlePaint);

        canvas.drawPath(getMajorPath(majorWaveOffset), mPaint);
        canvas.drawPath(getJuniorPath(juniorWaveOffset + waveOffset), mPaint);

        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, arcPaint);
    }

    /**
     * @param offset 偏移量
     * @return
     */
    private Path getMajorPath(int offset) {
        path.reset();
        path.moveTo(offset, mBaseline);
        for (int i = 1; i <= 8; i++) {
            int waveHeight = (i % 2 == 0) ? -1 * mWaveHeight : mWaveHeight;
            path.quadTo((2 * i - 1) * itemWidth + offset, mBaseline + waveHeight, 2 * i * itemWidth + offset, mBaseline);
        }
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();
        return path;
    }

    /**
     * 因为第二个波浪有多余偏移量，所以多画一些
     *
     * @param offset 偏移量
     * @return
     */
    private Path getJuniorPath(int offset) {
        path.reset();
        path.moveTo(offset, mBaseline);
        for (int i = 1; i <= 10; i++) {
            int waveHeight = (i % 2 == 0) ? -1 * mWaveHeight : mWaveHeight;
            path.quadTo((2 * i - 1) * itemWidth + offset, mBaseline + waveHeight, 2 * i * itemWidth + offset, mBaseline);
        }
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();
        return path;
    }
}
