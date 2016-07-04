package com.hxw.android.piechartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

/**
 * 饼状图控件。
 * Created by tony.hu on 2016/5/17.
 */
public class PieChart extends View {
    /**
     * 饼状图的动画方式：整个环从起点到逐渐画满360度。
     */
    public static final int ANIM_MODE_SINGLE_SECTION = 1;
    /**
     * 饼状图的动画方式：组成整个360度环的各个环分别从其起点画满其弧度。
     */
    public static final int ANIM_MODE_MULTI_SECTION = 2;
    /**
     * 饼状图的动画方式：圆环行进的方式从头到尾出现直至完全展示。
     */
    public static final int ANIM_MODE_PROCEED = 3;
    private Paint mPaintOuter, mPaintInner;
    private RectF mBigOval, mSmallOval;
    private int[] colors;
    private float[] angles;
    private final float DEFAULT_WIDTH_FACTOR = 0.3529f;
    private float mRingWidthFactor = DEFAULT_WIDTH_FACTOR;
    private final int animDuration = 600;
    /** 当动画开始时间重置为0时，在onDraw时会开始动画绘制。 */
    private long animStartTime = 0L;
    private float startAngle = 0f;
    private int animMode = ANIM_MODE_PROCEED;

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    private void initPaints() {
        mPaintOuter = new Paint();
        mPaintOuter.setAntiAlias(true);
        mPaintOuter.setStyle(Paint.Style.FILL);
        mPaintOuter.setColor(Color.WHITE);

        mPaintInner = new Paint(mPaintOuter);
        mPaintInner.setColor(Color.WHITE);
    }

    /**
     * 设置饼状图的动画方式。
     * @param mode 常量ANIM_MODE_XXX。
     */
    public void setAnimMode(int mode) {
        if (mode < 1) {
            mode = 1;
        } else if (mode > 2) {
            mode = 2;
        }
        animMode = mode;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        if (colors == null) return;
        if (mBigOval == null) {
            setRingWidthFactor(mRingWidthFactor);
        }

        if (animStartTime == 0) animStartTime = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - animStartTime;
        if (duration > animDuration) {
            duration = animDuration;
        }
        
        // to reverse: progress = 1f- duration / (animDuration + 0f);
        float progress = duration / (animDuration + 0f);

        if (animMode == ANIM_MODE_SINGLE_SECTION) {
            animDrawSingle(canvas, progress);
        } else if (animMode == ANIM_MODE_PROCEED) {
            animDrawProceed(canvas, progress);
        } else {
            animDrawMulti(canvas, progress);
        }

        // 画内部圆，形成环。
        canvas.drawArc(mSmallOval, 0, 360, true, mPaintInner);

        if (duration < animDuration) {
            invalidate();
        }
    }

    /**
     * 设置饼状图绘制各个分段的起点角度，X轴正方向为0度。
     * @param startAngle
     */
    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * 根据进度值，以动画方式画饼状图。动画方式：各个分段分别从0%-100%进行绘制。
     * @param canvas
     * @param progress
     */
    private void animDrawMulti(Canvas canvas, float progress) {
        float start = startAngle;
        float sweep;
        for (int i = 0; i < angles.length; i++) {
            sweep = (float) (angles[i] * progress + 0.5f);
            mPaintOuter.setColor(colors[i]);
            canvas.drawArc(mBigOval, start, sweep, true, mPaintOuter);
            start += angles[i];
        }
    }

    /**
     * 根据进度值，以动画方式画饼状图。动画方式：整个环从0-360度进行绘制。
     * @param canvas
     * @param progress
     */
    private void animDrawSingle(Canvas canvas, float progress) {
        float start = startAngle;
        float sweep;
        float end = start + 360f * progress;
        for (int i = 0; i < angles.length; i++) {
            sweep = (float) (angles[i] + 0.5f);
            mPaintOuter.setColor(colors[i]);
            if (sweep + start <= end) {
                canvas.drawArc(mBigOval, start, sweep, true, mPaintOuter);
            } else {
                sweep = end - start;
                canvas.drawArc(mBigOval, start, sweep, true, mPaintOuter);
                break;
            }
            start += angles[i];
        }
    }
    
    /**
     * 根据进度值，以动画方式画饼状图。动画方式：圆环行进的方式从头到尾出现直至完全展示。
     * @param canvas
     * @param progress
     */
    private void animDrawProceed(Canvas canvas, float progress) {
        float sweepStart;
        float sweep;
        float end = this.startAngle + 360f * progress;
        for (int i = angles.length - 1; i >= 0; i--) {
            sweep = (float) (angles[i] + 0.5f);
            sweepStart = end - sweep;
            mPaintOuter.setColor(colors[i]);
            if (sweepStart >= this.startAngle) {
                canvas.drawArc(mBigOval, sweepStart, sweep, true, mPaintOuter);
            } else {
                sweep = end - this.startAngle;
                canvas.drawArc(mBigOval, this.startAngle, sweep, true, mPaintOuter);
                break;
            }
            end -= sweep;
        }
    }

    /**
     * 设置饼状图数据。格式为“数值-颜色”的列表。
     */
    public void setPieData(ArrayList<Map.Entry<Double, Integer>> pieList) {
        ArrayList<Map.Entry<Double, Integer>> tempPieList = pieList;
        if (tempPieList == null) {
            tempPieList = new ArrayList<>();
        }

        double total = 0;
        colors = new int[tempPieList.size()];

        for (int i = 0; i < tempPieList.size(); i++) {
            total += tempPieList.get(i).getKey();
            colors[i] = tempPieList.get(i).getValue();
        }

        angles = new float[tempPieList.size()];
        for (int i = 0; i < tempPieList.size(); i++) {
            angles[i] = (float) (tempPieList.get(i).getKey() / total) * 360f;
        }

        animStartTime = 0L;
        invalidate();
    }

    private void updateBounds() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int diameter = width > height ? height : width;
        int radius = diameter / 2;
        mBigOval = new RectF(0, 0, diameter, diameter);
        mSmallOval = new RectF(radius * mRingWidthFactor,
                radius * mRingWidthFactor,
                diameter - radius * mRingWidthFactor,
                diameter - radius * mRingWidthFactor);
    }

    /**
     * 设置环的宽度参数factor，设置factor并不会引起View重新绘制，如果需要，调用invalidate()。
     * @param factor 表示环的宽度和外圆半径的比值。
     */
    public void setRingWidthFactor(float factor) {
        if (factor <= 0 || factor >= 1) {
            factor = DEFAULT_WIDTH_FACTOR;
        }
        mRingWidthFactor = factor;
        updateBounds();
    }
}
