package com.duck.appandroid.messages;

public class MessageList {
    private String name, sdt, lastMessage, profilePic, chatKey;

    private int unseenMessages;

    public MessageList(String name, String sdt, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.sdt = sdt;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
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

    public String getChatKey() {
        return chatKey;
    }
}
