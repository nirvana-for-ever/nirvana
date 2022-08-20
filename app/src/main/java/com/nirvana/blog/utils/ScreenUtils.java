package com.nirvana.blog.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/** 
 * 获得屏幕相关的辅助类 
 *  
 *  
 *  
 */  
public class ScreenUtils  
{  
    private ScreenUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");
    }  
  
    /** 
     * 获得屏幕高度 
     *  
     * @param context 
     * @return 
     */  
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);  
        return outMetrics.widthPixels;
    }  
  
    /** 
     * 获得屏幕宽度 
     *  
     * @param context 
     * @return 
     */  
    public static int getScreenHeight(Context context)
    {  
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);  
        return outMetrics.heightPixels;  
    }  
  
    /** 
     * 获得状态栏的高度 
     *  
     * @param context 
     * @return 
     */  
    public static int getStatusHeight(Context context)
    {  
  
        int statusHeight = -1;  
        try  
        {  
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());  
            statusHeight = context.getResources().getDimensionPixelSize(height);  
        } catch (Exception e)
        {  
            e.printStackTrace();  
        }  
        return statusHeight;  
    }  
  
    /** 
     * 获取当前屏幕截图，包含状态栏 
     *  
     * @param activity 
     * @return 
     */  
    public static Bitmap snapShotWithStatusBar(Activity activity)
    {  
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);  
        int height = getScreenHeight(activity);  
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();  
        return bp;  
  
    }  
  
    /** 
     * 获取当前屏幕截图，不包含状态栏 
     *  
     * @param activity 
     * @return 
     */  
    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {  
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
  
        int width = getScreenWidth(activity);  
        int height = getScreenHeight(activity);  
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);  
        view.destroyDrawingCache();  
        return bp;  
  
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static int getNavigationBarHeight(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            //超出系统默认的导航栏高度以上，则认为存在虚拟导航
            if ((realSize.y - size.y) > (height - 10)) {
                return height;
            }

            return 0;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return 0;
            } else {
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                int height = resources.getDimensionPixelSize(resourceId);
                return height;
            }
        }
    }
}