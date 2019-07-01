package chat;

import java.io.Serializable;
import java.util.Calendar;

public class ChatMessage implements Serializable {
    private String text;
    private String sender;
    private Calendar timeStamp;

    public ChatMessage(String t, String s, Calendar ts){
        this.text = t;
        this.sender = s;
        this.timeStamp = ts;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

}
