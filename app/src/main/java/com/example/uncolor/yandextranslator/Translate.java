package com.example.uncolor.yandextranslator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by uncolor on 24.05.17.
 */

public class Translate implements Parcelable  {

    private String textFrom;
    private String textTo;
    private String lang;
    private boolean isFavorite;

    Translate(){
        this.textTo = "";
        this.textFrom = "";
        this.lang = "";
        this.isFavorite = false;
    }

    Translate(String textTo,String textFrom, String lang, boolean isFavorite){
        this.textTo = textTo;
        this.textFrom = textFrom;
        this.lang = lang;
        this.isFavorite = isFavorite;
    }

    Translate(Translate other){
        this(other.getTextTo(),other.getTextFrom(),other.getLang(),other.isFavorite());
    }


    protected Translate(Parcel in) {
        textFrom = in.readString();
        textTo = in.readString();
        lang = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Translate> CREATOR = new Creator<Translate>() {
        @Override
        public Translate createFromParcel(Parcel in) {
            return new Translate(in);
        }

        @Override
        public Translate[] newArray(int size) {
            return new Translate[size];
        }
    };

    public String getTextTo() {
        return textTo;
    }

    public void setTextTo(String text) {
        this.textTo = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textFrom);
        dest.writeString(textTo);
        dest.writeString(lang);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
