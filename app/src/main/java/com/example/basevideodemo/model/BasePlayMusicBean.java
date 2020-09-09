package com.example.basevideodemo.model;

/**
 * @author puyantao
 * @describe
 * @create 2020/9/4 9:35
 */
public class BasePlayMusicBean {
    private String id;
    /**
     * 标题
     */
    private String contentTitle;
    /**
     * 音频
     */
    private String playUrl;
    /**
     * 图片 url
     */
    private String playPic;
    /**
     * 播放量
     */
    private String playCount;
    /**
     * 时间
     */
    private String playDate;

    private int row;
    private boolean isLocal;


    public BasePlayMusicBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPlayPic() {
        return playPic;
    }

    public void setPlayPic(String playPic) {
        this.playPic = playPic;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}










