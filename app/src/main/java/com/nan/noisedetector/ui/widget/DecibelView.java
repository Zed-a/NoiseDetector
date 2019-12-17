package com.nan.noisedetector.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nan.noisedetector.R;
import com.nan.noisedetector.util.DecibelUtil;

public class DecibelView extends AppCompatImageView {

    private int newWidth, newHeight;
    private Matrix mMatrix = new Matrix();
    private Bitmap indicatorBitmap;
    private Paint paint = new Paint();
    static final long  ANIMATION_INTERVAL = 20;

    public DecibelView(Context context) {
        super(context);
    }

    public DecibelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noise_index);
        int bitmapWidth = myBitmap.getWidth();
        int bitmapHeight = myBitmap.getHeight();
        newWidth = getWidth();
        newHeight = getHeight();
        float scaleWidth = ((float) newWidth) / (float) bitmapWidth;  // 获取缩放比例
        float scaleHeight = ((float) newHeight) / (float) bitmapHeight;  //获取缩放比例
        mMatrix.postScale(scaleWidth, scaleHeight);   //设置mMatrix的缩放比例
        indicatorBitmap = Bitmap.createBitmap(myBitmap, 0, 0, bitmapWidth, bitmapHeight, mMatrix,true);  //获取同等和背景宽高的指针图的bitmap

        paint = new Paint();
        paint.setTextSize(22* getDensity(getContext()));
        paint.setAntiAlias(true);  //抗锯齿
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
    }

    /**
     * 屏幕密度
     */
    private float getDensity(Context context){
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    public void refresh() {
        postInvalidateDelayed(ANIMATION_INTERVAL); //子线程刷新view
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indicatorBitmap == null) {
            init();
        }
        mMatrix.setRotate(getAngle(DecibelUtil.INSTANCE.getDbCount()), newWidth >> 1, (float) newHeight * 215 / 460);   //片相对位置
        canvas.drawBitmap(indicatorBitmap, mMatrix, paint);
        canvas.drawText((int) DecibelUtil.INSTANCE.getDbCount()+" DB", newWidth >> 1,(float) newHeight * 36 / 46, paint); //图片相对位置
    }

    private float getAngle(float db){
        return (float) ((db-81.5)*5/3);
    }
}
