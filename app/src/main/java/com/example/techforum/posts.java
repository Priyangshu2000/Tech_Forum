package com.example.techforum;

public class posts {
    String caption;
    String profilePic;
    String time;
    String postImage;
    String userName;
    String likes;

    public posts(String caption, String profilePic, String time, String postImage, String userName, String likes) {
        this.caption = caption;
        this.profilePic = profilePic;
        this.time = time;
        this.postImage = postImage;
        this.userName = userName;
        this.likes = likes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }



    public posts() {
    }

    public posts(String caption, String profilePic, String time, String postImage, String userName) {
        this.caption = caption;
        this.profilePic = profilePic;
        this.time = time;
        this.postImage = postImage;
        this.userName = userName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
