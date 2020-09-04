package com.example.basevideodemo.model;

/**
 * @author puyantao
 * @description :
 * @date 2020/9/4
 */
public class BaseVideoBean {
    private String id;
    private String title;
    private String videoUrl;
    private String videoPic;
    private long videoSeekPos;

    public BaseVideoBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public long getVideoSeekPos() {
        return videoSeekPos;
    }

    public void setVideoSeekPos(long videoSeekPos) {
        this.videoSeekPos = videoSeekPos;
    }
}
