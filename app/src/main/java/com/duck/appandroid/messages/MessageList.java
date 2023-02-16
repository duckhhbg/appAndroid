package com.duck.appandroid.messages;

public class MessageList {
    private String name, sdt, lastMessage, profilePic;

    private int unseenMessages;

    public MessageList(String name, String sdt, String lastMessage, String profilePic, int unseenMessages) {
        this.name = name;
        this.sdt = sdt;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
    }

    public String getName() {
        return name;
    }

    public String getSdt() {
        return sdt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }
}
