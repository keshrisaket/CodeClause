package com.example.real_timelanguagetranslationapp;

public class Modellanguage {

    private String languageCode;
    private String languageTitle;

    Modellanguage(String languageCode,String languageTitle){
        this.languageCode=languageCode;
        this.languageTitle=languageTitle;
    }

    Modellanguage()
    {
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}
