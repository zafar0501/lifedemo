package ness.com.etw.common.model;


import android.graphics.drawable.Drawable;

public class TitleDO {

    private String title;
    private String subTitle;
    private Drawable imgThumbnail;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }


    public Drawable getImgThumbnail() {
        return imgThumbnail;
    }

    public void setImgThumbnail(Drawable imgThumbnail) {
        this.imgThumbnail = imgThumbnail;
    }
}
