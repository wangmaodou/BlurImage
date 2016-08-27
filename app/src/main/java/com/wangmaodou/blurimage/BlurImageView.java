package com.wangmaodou.blurimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;

/**
 * maodou/2016.8.17
 */
public class BlurImageView extends View {

    private int mWidth,mHeight;
    private Bitmap mBitmap,mBlurBitmap;
    private float BLUR_RADIUS=25f;
    private float SCALE=0.5f;
    private Paint mPaint;
    private Rect mRect;

    public BlurImageView(Context context){
        this(context,null);
    }

    public BlurImageView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint.setAlpha(255);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode==MeasureSpec.AT_MOST||heightMode==MeasureSpec.AT_MOST)
            setMeasuredDimension(mWidth,mHeight);

        mRect=new Rect(0,0,mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawColor(Color.GRAY);
        canvas.restore();

        drawBlurBitmap(canvas);
        drawBitmap(canvas);
    }

    /**
     * Draw the Max_Blur_Radius bitmap.
     * @param canvas
     */
    private void drawBlurBitmap(Canvas canvas){
        canvas.save();
        if (mBlurBitmap != null)
            canvas.drawBitmap(mBlurBitmap, null, mRect, null);
        canvas.restore();
    }

    /**
     * Draw the blur bitmap that the radio is 10f.
     * @param canvas
     */
    private void drawBitmap(Canvas canvas){
        canvas.save();
        if (mBitmap!=null) {
            L.d("drawBitmap()==="+mPaint.getAlpha());
            canvas.drawBitmap(mBitmap, null, mRect, mPaint);
        }
        canvas.restore();
    }

    /**
     * Set this view target bitmap.
     * @param bitmap
     */
    public void setImage(Bitmap bitmap){
        if(bitmap!=null) {
            int w = Math.round(bitmap.getWidth() * SCALE);
            int h = Math.round(bitmap.getHeight() * SCALE);
            Bitmap tmp = Bitmap.createScaledBitmap(bitmap, w, h, false);
            mBlurBitmap = Bitmap.createScaledBitmap(getBlurBitmap(tmp,BLUR_RADIUS),
                    bitmap.getWidth(),bitmap.getHeight(),false);
            mBitmap = Bitmap.createScaledBitmap(getBlurBitmap(bitmap,15f),
                    bitmap.getWidth(),bitmap.getHeight(),false);
            invalidate();
        }
    }

    /**
     * Change the radius for the blur.
     * The high performance will act when changes is frequently.
     * @param radius
     */
    public void setBlurRadius(int radius){
        if(radius<=100&&radius>=0) {
            int v = 255-(int)(radius*2.55f);
            mPaint.setAlpha(v);
            invalidate();
        }
    }

    /**
     * To get the blur bitmap with the setting bitmap.
     * @param bitmap
     * @param radius
     * @return
     */
    private Bitmap getBlurBitmap(Bitmap bitmap,float radius){
        Bitmap result=null;
        result=Bitmap.createBitmap(bitmap);

        RenderScript rs=RenderScript.create(getContext());
        ScriptIntrinsicBlur blur=ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmp_b=Allocation.createFromBitmap(rs,bitmap);
        Allocation tmp_result=Allocation.createFromBitmap(rs,result);
        blur.setRadius(radius);
        blur.setInput(tmp_b);
        blur.forEach(tmp_result);
        tmp_result.copyTo(result);

        return result;
    }

}