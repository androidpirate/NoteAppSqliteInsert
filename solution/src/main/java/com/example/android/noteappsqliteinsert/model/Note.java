package com.example.android.noteappsqliteinsert.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO model class.
 */
public class Note implements Parcelable {
    private int _id;
    private String title;
    private String description;

    public Note() {
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Add _id field to Parcel
        dest.writeInt(this._id);
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    protected Note(Parcel in) {
        // Set _id field from Parcel
        this._id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
