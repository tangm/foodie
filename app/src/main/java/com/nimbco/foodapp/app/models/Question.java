package com.nimbco.foodapp.app.models;

/**
 * Created by mtan on 9/07/2016.
 */
public class Question {

    private final String _id;
    private final String header;
    private final String text;


    public Question(final String id, final String header, final String text) {
        _id = id;
        this.header = header;
        this.text = text;
    }

    public String getId() {
        return _id;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }
}
