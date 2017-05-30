package com.example.uncolor.yandextranslator;

/**
 * Created by uncolor on 24.05.17.
 */

public class Translate {

    private String textFrom;
    private String textTo;
    private String lang;

    Translate(String textTo,String textFrom, String lang){
        this.textTo = textTo;
        this.textFrom = textFrom;
        this.lang = lang;
    }


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
}
