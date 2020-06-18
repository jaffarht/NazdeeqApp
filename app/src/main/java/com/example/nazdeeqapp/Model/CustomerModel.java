package com.example.nazdeeqapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerModel implements Parcelable {
    private String Date;
    private String sourceName;
    private String destinationName;
    private String Time;
    private String PostID;
    private long price;
    private String driverID;
    private String driverCarModel;
    private String driverCarNumber;
    private String driverName;

    public CustomerModel() {
    }

    public CustomerModel(String date, String sourceName, String destinationName, String time, String postID, long price, String driverID, String driverCarModel,
                         String driverCarNumber, String driverName) {
        Date = date;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        Time = time;
        PostID = postID;
        this.price = price;
        this.driverID = driverID;
        this.driverCarModel = driverCarModel;
        this.driverCarNumber = driverCarNumber;
        this.driverName = driverName;
    }

    protected CustomerModel(Parcel in) {
        Date = in.readString();
        sourceName = in.readString();
        destinationName = in.readString();
        Time = in.readString();
        PostID = in.readString();
        price = in.readLong();
        driverID = in.readString();
        driverCarModel = in.readString();
        driverCarNumber = in.readString();
        driverName = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverCarModel() {
        return driverCarModel;
    }

    public void setDriverCarModel(String driverCarModel) {
        this.driverCarModel = driverCarModel;
    }

    public String getDriverCarNumber() {
        return driverCarNumber;
    }

    public void setDriverCarNumber(String driverCarNumber) {
        this.driverCarNumber = driverCarNumber;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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
        dest.writeString(Time);
        dest.writeString(PostID);
        dest.writeLong(price);
        dest.writeString(driverID);
        dest.writeString(driverCarModel);
        dest.writeString(driverCarNumber);
        dest.writeString(driverName);
    }
}
