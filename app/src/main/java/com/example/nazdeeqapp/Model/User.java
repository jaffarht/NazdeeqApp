package com.example.nazdeeqapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    public String email;
    public String name;
    public String phoneNumber;
    public String imageURL;
    private String user_id;

    public User() {
        //empty constructor for firebase
    }

    // Sets email and sets all other user attributes to empty string ("").
    public User(String email, String first, String last, String phone, String imageURL) {
        this.email = email;

        // set the other fields as empty strings:
        this.name = first;
        this.phoneNumber = phone;
        this.imageURL = imageURL;
        this.user_id = user_id;

    }

    protected User(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        imageURL = in.readString();
        user_id = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(String email, String password, String name, String phone) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(imageURL);
    }
}


