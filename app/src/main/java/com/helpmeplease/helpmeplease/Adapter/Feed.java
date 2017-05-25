package com.helpmeplease.helpmeplease.Adapter;

/**
 * Created by Phattarapong on 25-May-17.
 */

public class Feed {
    private String feedTitle, thumbnailUrl,feedSub,feedContent;


    public Feed() {
    }

    public Feed(String feedTitle, String thumbnailUrl, String feedSub, String feedContent
                ) {
        this.feedTitle = feedTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.feedSub = feedSub;
        this.feedContent = feedContent;


    }

    public String getfeedTitled() {
        return feedTitle;
    }

    public void setfeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getfeedSub() {
        return feedSub;
    }
    public void setfeedSub(String feedSub) {
        this.feedSub = feedSub;
    }

    public String getfeedContent() {
        return feedContent;
    }
    public void setfeedContent(String feedContent) {
        this.feedContent = feedContent;
    }

}
