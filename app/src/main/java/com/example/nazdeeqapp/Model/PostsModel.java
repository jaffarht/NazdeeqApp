package com.example.nazdeeqapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostsModel implements Parcelable {

    private String Date;
    private String sourceName;
    private String destinationName;
    private String sourceAddress;
    private String destinationAddress;
    private long SeatsAvailable;
    private String Time;
    private String UserID;
    private String PostID;
    private long price;



    public PostsModel() {

    }

    public PostsModel(String date, String sourceName, String destinationName, String sourceAddress, String destinationAddress,
                      long seatsAvailable, String time, String userID, long price)
    {
        Date = date;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        SeatsAvailable = seatsAvailable;
        Time = time;
        UserID = userID;
        this.price = price;
    }

    protected PostsModel(Parcel in) {
        Date = in.readString();
        sourceName = in.readString();
        destinationName = in.readString();
        sourceAddress = in.readString();
        destinationAddress = in.readString();
        SeatsAvailable = in.readLong();
        Time = in.readString();
        UserID = in.readString();
        PostID = in.readString();
        price = in.readLong();
    }

    public static final Creator<PostsModel> CREATOR = new Creator<PostsModel>() {
        @Override
        public PostsModel createFromParcel(Parcel in) {
            return new PostsModel(in);
        }

        @Override
        public PostsModel[] newArray(int size) {
            return new PostsModel[size];
        }
    };

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public long getSeatsAvailable() {
        return SeatsAvailable;
    }

    public void setSeatsAvailable(long seatsAvailable) {
        SeatsAvailable = seatsAvailable;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(sourceName);
        dest.writeString(destinationName);
        dest.writeString(sourceAddress);
        dest.writeString(destinationAddress);
        dest.writeLong(SeatsAvailable);
        dest.writeString(Time);
        dest.writeString(UserID);
        dest.writeString(PostID);
        dest.writeLong(price);
    }
}
