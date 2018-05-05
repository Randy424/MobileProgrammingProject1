package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;

public class Message {

    private String sender;
    private String receiver;
    private String content;
    protected @ServerTimestamp
    Date time;

    public Message() {
        super();
        // Must have a public no-argument constructor ?
    }

    Message(String theSender, String theReceiver, String messageContent) {
        super();
        sender = theSender;
        receiver = theReceiver;
        content = messageContent;
        time = null;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "\"" + this.content + "\"";
    }


}
