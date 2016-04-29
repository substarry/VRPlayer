package me.substarry.vrplayer;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.samskrut.vrplayer.R;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.vr.renderer.RajawaliVRRenderer;

import java.io.File;

public class VideoRenderer extends RajawaliVRRenderer {

    // Context mContext;
    MainActivity mainActivity;
    String videopath;
    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;
    private Sphere mSphere;

    public VideoRenderer(Activity activity, String _path) {
        super(activity.getApplicationContext());

        videopath = _path;
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void initScene() {


        if(TextUtils.isEmpty(videopath)){
            mMediaPlayer = MediaPlayer.create(getContext(), R.raw.demo);
        }
        else{
            File file = new File(videopath);
            Uri uri = Uri.fromFile(file);
            Log.d("bis", "uri= " + uri.toString());
            mMediaPlayer = MediaPlayer.create(getContext(), uri);

        }
        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("sintelTrailer", mMediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        mSphere = new Sphere(50, 64, 32);
        mSphere.setScaleX(-1);
        mSphere.setMaterial(material);

        getCurrentScene().addChild(mSphere);

        getCurrentCamera().setPosition(Vector3.ZERO);

        getCurrentCamera().setFieldOfView(75);

        mMediaPlayer.start();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("bis", "video completed");
                mp.stop();
                mainActivity.finish();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d("bis", "video seek completed");
                mp.stop();
                mainActivity.finish();
            }
        });

    }

    @Override
    public void addGestureRotateAngle(float rotateXAngle, float rotateYAngle){

        super.addGestureRotateAngle(rotateXAngle, rotateYAngle);
//        mGestureYAngle = 0;
//        mSphere.rotate(Vector3.Y, rotateYAngle);
    }


    @Override
    public void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mVideoTexture.update();
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }


    public void pauseVideo() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    public void resumeVideo() {
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }
}
