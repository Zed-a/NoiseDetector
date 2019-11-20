package com.nan.noisedetector.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nan.noisedetector.R;

/**
 * @author nan_xu
 */
public class SesameCreditView extends View {

    private int mMaxNum, mStartAngle, mSweepAngle;
    private int mSweepInWidth, mSweepOutWidth;
    private Paint mPaint, mPaint_1, mPaint_2, mPaint_3, mPaint_4;
    private int mWSize, mHSize;
    private int mWMode, mHMode;
    private int mWidth, mHeight;
    private int mRadius;
    private Context mContext;

    private int[] indicatorColor = {0xFF2C3E50, 0x99ffffff, 0xaaffffff, 0xbbffffff, 0xccffffff, 0xddffffff, 0xeeffffff, 0xffffffff};
    private int[] indicatorColorBg = {ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), ContextCompat.getColor(getContext(), R.color.colorPrimary), Color.WHITE};

    private int currentNum;

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        invalidate();
    }

    public void setCurrentNumAnim(float numF) {
        int num = (int) (numF * 500);
        float duration = (float) Math.abs(num - currentNum) / mMaxNum * 1500 + 500;
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentNum", num);
        anim.setDuration((long) Math.min(duration, 2000));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public SesameCreditView(Context context) {
        this(context, null);
    }

    public SesameCreditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //对当前控件不使用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initAttr(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setDither(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.WHITE);
        mPaint_1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void initAttr(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SesameCreditView);
//        mMaxNum = typedArray.getInt(R.styleable.SesameCreditView_maxNum, 500);
//        mStartAngle = typedArray.getInt(R.styleable.SesameCreditView_startAngle, 90);
//        mSweepAngle = typedArray.getInt(R.styleable.SesameCreditView_sweepAngle, 360);
//        currentNum = typedArray.getInt(R.styleable.SesameCreditView_currentNum, 0);

        mMaxNum = 500;
        mStartAngle = 90;
        mSweepAngle = 360;
        currentNum = 0;

        //圆环弧度的宽度
        mSweepOutWidth = dp2px(context, 4);
//        typedArray.recycle();
//        currentNum = mMaxNum;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWSize = MeasureSpec.getSize(widthMeasureSpec);
        mWMode = MeasureSpec.getMode(widthMeasureSpec);
        mHSize = MeasureSpec.getSize(heightMeasureSpec);
        mHMode = MeasureSpec.getMode(heightMeasureSpec);

        if (mWMode == MeasureSpec.EXACTLY) {
            mWidth = mWSize;
        } else {
            mWidth = dp2px(mContext, 300);
        }

        if (mHMode == MeasureSpec.EXACTLY) {
            mHeight = mHSize;
        } else {
            mHeight = dp2px(mContext, 400);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadius = getMeasuredWidth() / 4;
        canvas.save();
        canvas.translate((float) mWidth / 2, (float) mHeight / 2);
        drawRound(canvas);//画内外圆弧
//        drawScale(canvas);//画刻度
        drawIndicator(canvas);//画当前进度值
        drawCenterText(canvas);//画中间文字
        canvas.restore();
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        mPaint_3.setStyle(Paint.Style.FILL);
        mPaint_3.setTextSize((float) (mRadius / 1.5));
        mPaint_3.setColor(0xffffffff);
//        mPaint_3.setTypeface(AppContext.getInstance().getCustomTypeface());
        String text = currentNum / 5 + "%";
        canvas.drawText(text, -mPaint_3.measureText(text) / 2, 8, mPaint_3);
        mPaint_3.setTextSize(mRadius / 5);
        mPaint_3.setFakeBoldText(false);
        String content = "处理进度";
        Rect rect = new Rect();
        mPaint_3.getTextBounds(content, 0, content.length(), rect);
        canvas.drawText(content, -rect.width() / 2, rect.height() + 32, mPaint_3);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        mPaint_1.setStyle(Paint.Style.STROKE);
        int sweep;
        if (currentNum <= mMaxNum) {
            sweep = (int) ((float) currentNum / (float) mMaxNum * mSweepAngle);
        } else {
            sweep = mSweepAngle;
        }
        if (sweep < 0) {
            sweep = 0;
        }
        mPaint_1.setStrokeWidth(mSweepOutWidth);
        SweepGradient sweepGradient = new SweepGradient(0, 0, indicatorColor, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle);
        sweepGradient.setLocalMatrix(matrix);
        mPaint_1.setShader(sweepGradient);
        int w = dp2px(mContext, 10);
        RectF rectF = new RectF(-mRadius - w, -mRadius - w, mRadius + w, mRadius + w);
        canvas.drawArc(rectF, mStartAngle, sweep, false, mPaint_1);
        float x = (float) ((mRadius + dp2px(mContext, 10)) * Math.cos(Math.toRadians(mStartAngle + sweep)));
        float y = (float) ((mRadius + dp2px(mContext, 10)) * Math.sin(Math.toRadians(mStartAngle + sweep)));
        if (sweep > 0) {
            mPaint_2.setStyle(Paint.Style.FILL);
            mPaint_2.setColor(0xffffffff);
            mPaint_2.setMaskFilter(new BlurMaskFilter(dp2px(mContext, 4), BlurMaskFilter.Blur.SOLID));
            canvas.drawCircle(x, y, dp2px(mContext, 6), mPaint_2);
        }
        canvas.restore();
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        int w = dp2px(mContext, 10);
        RectF rectF1 = new RectF(-mRadius - w, -mRadius - w, mRadius + w, mRadius + w);

        mPaint_4.setDither(true);
        mPaint_4.setStyle(Paint.Style.STROKE);
        mPaint_4.setStrokeWidth(mSweepOutWidth);
        mPaint_4.setColor(0x23000000);
        mPaint_4.setMaskFilter(new BlurMaskFilter(dp2px(mContext, 10), BlurMaskFilter.Blur.SOLID));
        canvas.drawArc(rectF1, mStartAngle, mSweepAngle, false, mPaint_4);

        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSweepOutWidth);
        SweepGradient sweepGradient = new SweepGradient(0, 0, indicatorColorBg, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(mStartAngle);
        sweepGradient.setLocalMatrix(matrix);
        mPaint.setShader(sweepGradient);
        RectF rectF = new RectF(-mRadius - w, -mRadius - w, mRadius + w, mRadius + w);
        canvas.drawArc(rectF, mStartAngle, mSweepAngle, false, mPaint);
        canvas.restore();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
