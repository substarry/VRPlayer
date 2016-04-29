package me.substarry.vrplayer;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by 何凌 on 2016/4/6.
 */
public class ScreenUtils {

    private static final String TAG = "ScreenUtils";
    private static float mScreenWidth = 0, mScreenHeight = 0;

    /**
     * 手指横向滑动距离与旋转角度换算，一屏的距离对应180度
     * @param activity
     * @param rawX 横向滑动距离
     * @return 角度
     */
    public static float rawX2Angle(Activity activity, float rawX){
        if(getScreenWidth(activity) == 0){
            return rawX * 0.1f;
        }
        return  rawX * 180/mScreenWidth;
    }

    /**
     * 手指纵向滑动距离与旋转角度换算，一屏的距离对应180度
     * @param activity
     * @param rawY 纵向滑动距离
     * @return 角度
     */
    public static float rawY2Angle(Activity activity, float rawY){
        if(getScreenHeight(activity) == 0){
            return rawY * 0.2f;
        }
        return  rawY * 180/mScreenHeight;
    }

    public static float getScreenWidth(Activity activity){
        if(mScreenWidth == 0){
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            mScreenWidth = size.x;
            Log.d(TAG, "screen size is " + size);
        }
        return mScreenWidth;
    }

    public static float getScreenHeight(Activity activity){
        if(mScreenHeight == 0){
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            mScreenHeight = size.y;
            Log.d(TAG, "screen size is " + size);
        }
        return mScreenHeight;
    }
}
