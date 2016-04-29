package me.substarry.vrplayer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.rajawali3d.util.RajLog;
import org.rajawali3d.vr.RajawaliVRActivity;
import org.rajawali3d.vr.ui.GestureListener;

import java.io.File;


public class MainActivity extends RajawaliVRActivity {

    private VideoRenderer mRenderer;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        GestureListener gestureListener = new GestureListener();
        gestureListener.setOnEventGesture(mOnEventGesture);
        gestureListener.setToucheMode(GestureListener.TOUCHEMODE.MODE_ZOOM_AND_MOVE);
        setOnTouchListener(gestureListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("fpath") != null) {
            String path = getIntent().getExtras().getString("fpath", null);
            File file = new File(path);
            mRenderer = new VideoRenderer(MainActivity.this, path);
        } else {
            mRenderer = new VideoRenderer(MainActivity.this, null);
            Toast.makeText(getApplicationContext(), "Play Demo!", Toast.LENGTH_SHORT).show();
        }

        setRenderer(mRenderer);

        setConvertTapIntoTrigger(true);
    }

    /**
     * Called when the Cardboard trigger is pulled.
     */
    @Override
    public void onCardboardTrigger() {
        RajLog.i("onCardboardTrigger");
    }

    @Override
    public void onPause() {
        mRenderer.pauseVideo();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.resumeVideo();
    }

    private GestureListener.OnEventGesture mOnEventGesture = new GestureListener.OnEventGesture() {
        @Override
        public void onZoomGesture(View v, float newDist, float oldDist) {

        }

        @Override
        public void onMoveGesture(View v, MotionEvent currentMoveEvent, float mTouchStartX, float mTouchStartY) {
            if(!getVRMode()){
                mRenderer.addGestureRotateAngle(
                        ScreenUtils.rawY2Angle(MainActivity.this, currentMoveEvent.getRawY() - mTouchStartY),
                        ScreenUtils.rawX2Angle(MainActivity.this, currentMoveEvent.getRawX() - mTouchStartX)
                );
            }
        }

        @Override
        public void onClick(View v, MotionEvent currentMoveEvent) {
            onCardboardTrigger();
        }

        @Override
        public void onLeftRightGesture(View v, MotionEvent currentEventm, float startx, float starty, GestureListener.GestureState state) {

        }

        @Override
        public void onUpDownLeftGesture(View v, MotionEvent currentEventm, float startx, float starty, GestureListener.GestureState state) {

        }

        @Override
        public void onUpDownRightGesture(View v, MotionEvent currentEventm, float startx, float starty, GestureListener.GestureState state) {

        }
    };


}