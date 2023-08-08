package com.example.techforum;

import java.io.Serializable;

public class userDetails{
    String phoneNo,name,email,password,profilePic;

    public userDetails() {
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public userDetails(String phoneNo, String name, String email, String password, String profilePic) {
        this.phoneNo = phoneNo;
        this.name = name;
        this.email = email;
        this.profilePic=profilePic;
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
