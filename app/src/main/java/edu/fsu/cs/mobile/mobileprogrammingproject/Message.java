package edu.fsu.cs.mobile.mobileprogrammingproject;

public class Message {
    private String sender;
    private String receiver;
    private String content;

    public Message() {
        // Must have a public no-argument constructor ?
    }

    public Message(String theSender, String theReceiver, String messageContent)
    {
        sender = theSender;
        receiver = theReceiver;
        content = messageContent;
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



}
