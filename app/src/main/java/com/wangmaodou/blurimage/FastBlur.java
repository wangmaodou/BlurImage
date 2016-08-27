package com.wangmaodou.blurimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Maodou on 2016/8/17.
 */
public class FastBlur {

    public static Bitmap getBlurBitmap(Bitmap bitmap,Context context,float radius){
        Bitmap result=null;
        result=Bitmap.createBitmap(bitmap);

        RenderScript rs=RenderScript.create(context);
        ScriptIntrinsicBlur blur=ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmp_bitmap=Allocation.createFromBitmap(rs,bitmap);
        Allocation tmp_result=Allocation.createFromBitmap(rs,result);

        blur.setRadius(radius);
        blur.setInput(tmp_bitmap);
        blur.forEach(tmp_result);
        tmp_result.copyTo(result);

        return result;
    }
}
