package com.weex.wdialog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuwx on 2018/10/23.
 */

public class XModeView extends View {

    private Paint mPaint;
    private Paint paint;
    private Paint paint2;

    public XModeView(Context context) {
        this(context, null);
    }

    public XModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.YELLOW);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(100, 100, 100, mPaint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(100, 100, 300, 300, paint);

        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(120, 120, 300, 300, paint2);
    }
}
