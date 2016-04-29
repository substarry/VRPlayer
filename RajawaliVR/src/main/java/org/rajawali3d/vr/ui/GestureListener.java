package org.rajawali3d.vr.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GestureListener implements OnTouchListener {
    private int mode = 0;
    private long touchStartTime;
    private float x, y;
    private float mTouchStartX;
    private float mTouchStartY;
    private float mClickTouchStartX, mClickTouchStartY;
    private float oldDist;
    private TOUCHEMODE mTouchMode = TOUCHEMODE.NOTHING;
    private TOUCHTYPE mTouchType;
    private OnEventGesture mOnEventGesture;

    public enum TOUCHEMODE {
        MODE_ZOOM_AND_MOVE, MODE_CTRL_SBP, NOTHING
    }

    public enum TOUCHTYPE {
        TYPE_GLIDE_LEFT_RIGHT, TYPE_GLIDE_UP_DOWN_IN_LEFT, TYPE_GLIDE_UP_DOWN_IN_RIGHT,
    }

    public enum GestureState {
        STATE_GESTURE_START, STATE_GESTURE_ING, STATE_GESTURE_END
    }

    public void setToucheMode(TOUCHEMODE mode) {
        mTouchMode = mode;
    }

    public void setOnEventGesture(OnEventGesture onEventGesture) {
        this.mOnEventGesture = onEventGesture;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointCount = event.getPointerCount();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = 1;
                touchStartTime = System.currentTimeMillis();
                mTouchStartX = event.getRawX();
                mTouchStartY = event.getRawY();
                mClickTouchStartX = mTouchStartX;
                mClickTouchStartY = mTouchStartY;
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                mode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode >= 2) {

                    float newDist = spacing(event);
                    if (mTouchMode == TOUCHEMODE.MODE_ZOOM_AND_MOVE)
                        mOnEventGesture.onZoomGesture(v, newDist, oldDist);
                    oldDist = newDist;

                } else {
                    singleMove(event, v);

                }
                break;

            case MotionEvent.ACTION_UP:
                mode = 0;
                if (pointCount >= 2)
                    break;

                if (mTouchType != null) {
                    switch (mTouchType) {
                        case TYPE_GLIDE_LEFT_RIGHT:

                            mOnEventGesture.onLeftRightGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_END);
                            break;
                        case TYPE_GLIDE_UP_DOWN_IN_LEFT:
                            mOnEventGesture.onUpDownLeftGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_END);
                            break;
                        case TYPE_GLIDE_UP_DOWN_IN_RIGHT:
                            mOnEventGesture.onUpDownRightGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_END);
                            break;
                    }
                    mTouchType = null;

                } else {
                    if (Math.abs(event.getRawX() - mClickTouchStartX) < 30
                            && Math.abs(event.getRawY() - mClickTouchStartY) < 30
                            && System.currentTimeMillis() - touchStartTime < 300) {
                        mOnEventGesture.onClick(v, event);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void singleMove(MotionEvent event, View v) {
        switch (mTouchMode) {
            case MODE_CTRL_SBP:
                if (mTouchType != null) {
                    switch (mTouchType) {
                        case TYPE_GLIDE_LEFT_RIGHT:

                            mOnEventGesture.onLeftRightGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_ING);
                            break;
                        case TYPE_GLIDE_UP_DOWN_IN_LEFT:
                            mOnEventGesture.onUpDownLeftGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_ING);
                            break;
                        case TYPE_GLIDE_UP_DOWN_IN_RIGHT:
                            mOnEventGesture.onUpDownRightGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_ING);
                            break;
                    }
                } else {

                    if (Math.abs(event.getY() - y) < 50
                            && Math.abs(event.getX() - x) > 50) {
                        mTouchType = TOUCHTYPE.TYPE_GLIDE_LEFT_RIGHT;
                        mOnEventGesture.onLeftRightGesture(v, event, x, y,
                                GestureState.STATE_GESTURE_START);
                        x = event.getX();
                        y = event.getY();
                    } else if (Math.abs(event.getX() - x) < 50
                            && Math.abs(event.getY() - y) > 50) {

                        if (x < v.getWidth() / 2) {
                            mTouchType = TOUCHTYPE.TYPE_GLIDE_UP_DOWN_IN_LEFT;
                            mOnEventGesture.onUpDownLeftGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_START);
                        } else {
                            mTouchType = TOUCHTYPE.TYPE_GLIDE_UP_DOWN_IN_RIGHT;
                            mOnEventGesture.onUpDownRightGesture(v, event, x, y,
                                    GestureState.STATE_GESTURE_START);
                        }
                        x = event.getX();
                        y = event.getY();
                    }

                }
                break;
            case MODE_ZOOM_AND_MOVE:
                mOnEventGesture.onMoveGesture(v, event, mTouchStartX, mTouchStartY);
                mTouchStartX = event.getRawX();
                mTouchStartY = event.getRawY();
                break;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    public interface OnEventGesture {
        public void onZoomGesture(View v, float newDist, float oldDist);

        public void onMoveGesture(View v, MotionEvent currentMoveEvent,
                                  float mTouchStartX, float mTouchStartY);

        public void onClick(View v, MotionEvent currentMoveEvent);

        public void onLeftRightGesture(View v, MotionEvent currentEventm,
                                       float startx, float starty, GestureState state);

        public void onUpDownLeftGesture(View v, MotionEvent currentEventm,
                                        float startx, float starty, GestureState state);

        public void onUpDownRightGesture(View v, MotionEvent currentEventm,
                                         float startx, float starty, GestureState state);
    }
}