package edu.ucsb.g01.dialog;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    /** Message source user id */
    int srcUid;

    /** Message destination user id */
    int destUid;

    /** Message timestamp (time sent) */
    long timestamp;

    /** Message body */
    String messageBody;

    public Message(int srcUid, int destUid, long timestamp, String messageBody) {
        this.srcUid = srcUid;
        this.destUid = destUid;
        this.timestamp = timestamp;
        this.messageBody = messageBody;
    }

    public int getSrcUid() {
        return srcUid;
    }

    public void setSrcUid(int srcUid) {
        this.srcUid = srcUid;
    }

    public int getDestUid() {
        return destUid;
    }

    public void setDestUid(int destUid) {
        this.destUid = destUid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
