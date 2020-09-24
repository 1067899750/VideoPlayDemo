package com.example.basevideodemo.until;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.basevideodemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.IntDef;

/**
 * @author puyantao
 * @describe Glide 图片加载工具
 * @create 2019/10/9 9:30
 */
public class GlideUtils {
    public static final int FIT_CENTER = 0x00000000;
    public static final int CENTER_CROP = 0x00000001;

    /**
     * 配置缩放方式
     */
    @IntDef({FIT_CENTER, CENTER_CROP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CenterType {
    }

    private WeakReference<Context> mWeakReference;
    private Builder mBuilder;

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public GlideUtils(Builder builder) {
        this.mWeakReference = new WeakReference<>(builder.context);
        this.mBuilder = builder;
        loadUrlToView();
    }


    private void loadUrlToView() {
        if (mBuilder.mDefaultCorner <= 0) {
            Glide.with(mWeakReference.get()).load(mBuilder.mBaseUrl)
                    .apply(getOptions())
                    .into(mBuilder.mImageView);
        } else {
            Glide.with(mWeakReference.get()).load(mBuilder.mBaseUrl)
                    .apply(getOptions())
                    .transform(new CenterCrop(), new RoundedCorners(dp2px(mBuilder.mDefaultCorner)))
                    .into(mBuilder.mImageView);
        }


    }

    /**
     * Gilded 配置
     *
     * @return
     */
    private RequestOptions getOptions() {
        RequestOptions options = new RequestOptions()
                //加载成功之前占位图
                .placeholder(mBuilder.mPlaceholderId == 0 ? R.drawable.photo_detail_empty : mBuilder.mPlaceholderId)
                //加载错误之后的错误图
                .error(mBuilder.mErrorId == 0 ? R.drawable.photo_detail_empty : mBuilder.mErrorId)
                //指定图片的尺寸
//                .override(100,100)
                //是否不使用内存缓存
                .skipMemoryCache(mBuilder.mMemoryCache)
                //只缓存最终的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (mBuilder.mCenterType == FIT_CENTER) {
            //等比例缩放图片，宽或者是高等于 ImageView的宽或者是高。是指其中一个满足即可不会一定铺满 imageview
            options.fitCenter();
        } else if (mBuilder.mCenterType == FIT_CENTER) {
            //等比例缩放图片，直到图片的宽高都 大于等于ImageView的宽度，然后截取中间的显示。）
            options.centerCrop();
        }
        return options;
    }

    public int dp2px(float dpValue) {
        float scale = mWeakReference.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }


    public static class Builder {
        private Context context;
        private String mBaseUrl;
        private ImageView mImageView;
        private int mPlaceholderId;
        private int mErrorId;
        private int mDefaultCorner = 0;
        private int mCenterType = FIT_CENTER;
        private boolean mMemoryCache = true;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 传入圆角大小
         *
         * @param corner
         * @return
         */
        public Builder corner(int corner) {
            this.mDefaultCorner = corner;
            return this;
        }


        /**
         * 传入图片路径
         *
         * @param url
         * @return
         */
        public Builder load(String url) {
            this.mBaseUrl = url;
            return this;
        }

        /**
         * 需要加载图片的试图
         *
         * @param imageView
         * @return
         */
        public Builder into(ImageView imageView) {
            this.mImageView = imageView;
            return this;
        }

        /**
         * 加载成功之前占位图
         *
         * @param sourceId
         * @return
         */
        public Builder placeholder(int sourceId) {
            this.mPlaceholderId = sourceId;
            return this;
        }


        /**
         * 加载错误之后的错误图
         *
         * @param sourceId
         * @return
         */
        public Builder error(int sourceId) {
            this.mErrorId = sourceId;
            return this;
        }

        /**
         * 设置图片的缩放方式
         *
         * @param type
         * @return
         */
        public Builder setCenterType(@CenterType int type) {
            this.mCenterType = type;
            return this;
        }

        /**
         * 是否不使用内存缓存
         *
         * @param cache
         * @return
         */
        public Builder setMemoryCache(boolean cache) {
            this.mMemoryCache = cache;
            return this;
        }


        public GlideUtils create() {
            return new GlideUtils(this);
        }

    }


}










