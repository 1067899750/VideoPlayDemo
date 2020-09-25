package com.example.basevideodemo.widget.exo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.basevideodemo.widget.exo.video.ExoPlayVideoView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author puyantao
 * @description :
 * @date 2020/9/11
 */
public class ExoUtils {
    public static final String TAG = "Exo";
    public static int SYSTEM_UI = 0;

    /**
     * Get AppCompatActivity from context
     *
     * @param context context
     * @return AppCompatActivity if it's not null
     */
    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * Get activity from context object
     *
     * @param context context
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }


    //如果是沉浸式的，全屏前就没有状态栏
    @SuppressLint("RestrictedApi")
    public static void hideStatusBar(Context context) {
        if (ExoPlayVideoView.TOOL_BAR_EXIST) {
            ExoUtils.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @SuppressLint("RestrictedApi")
    public static void showStatusBar(Context context) {
        if (ExoPlayVideoView.TOOL_BAR_EXIST) {
            ExoUtils.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    public static Window getWindow(Context context) {
        if (ExoUtils.getAppCompActivity(context) != null) {
            return ExoUtils.getAppCompActivity(context).getWindow();
        } else {
            return ExoUtils.scanForActivity(context).getWindow();
        }
    }


    public static void setRequestedOrientation(Context context, int orientation) {
        if (ExoUtils.getAppCompActivity(context) != null) {
            ExoUtils.getAppCompActivity(context).setRequestedOrientation(
                    orientation);
        } else {
            ExoUtils.scanForActivity(context).setRequestedOrientation(
                    orientation);
        }
    }


    @SuppressLint("NewApi")
    public static void hideSystemUI(Context context) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        SYSTEM_UI = ExoUtils.getWindow(context).getDecorView().getSystemUiVisibility();
        ExoUtils.getWindow(context).getDecorView().setSystemUiVisibility(uiOptions);
    }

    @SuppressLint("NewApi")
    public static void showSystemUI(Context context) {
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        ExoUtils.getWindow(context).getDecorView().setSystemUiVisibility(SYSTEM_UI);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取视频第一针图片
     *
     * @return
     */
    public static Bitmap getVideoFirstDosePic(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url, new HashMap());
        //获得第10帧图片 这里的第一个参数 以微秒为单位
        Bitmap bitmap  = retriever.getFrameAtTime(10000000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        retriever.release();
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int options = 90;

        while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
            byteArrayOutputStream.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }



}















