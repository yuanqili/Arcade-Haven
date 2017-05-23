package networking;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>An {@link SBMessage} is a message being transmitted between a
 * {@link SBServer} and a {@link SBClient}. It encapsulates necessary
 * information in a message, making parsing easier. Every {@link SBMessage} has
 * a type and action. You can think of action as a subtype. It also has a
 * timestamp representing the time this message being created, which is useful
 * in some cases. {@link SBMessage} applies factory constructor design pattern,
 * so you can set message fields in a way you feel most comfortable. For
 * example, the following expression creates a conversation message:</p>
 *
 * <pre>
 * {@code
 * networking.SBMessage msg = new networking.SBMessage()
 *         .setType(networking.Type.message)
 *         .setAction(networking.Action.send)
 *         .setSequence(sequence++)
 *         .setSender("Alice")
 *         .setReceiver("Bob")
 *         .setBody("Hey how are you doing?");
 * }
 * </pre>
 *
 * <p>For parsing, as long as you know message type and action, you know what
 * fields are related and valid.</p>
 *
 * <p>There is a sequence number for each message. At the beginning, a client
 * and a server decide the initial sequence number. For each of the following
 * message pairs, sequence should be incremented by 1. This is useful when you
 * have a lot of messages sent and want to figure out which message do all
 * responses you receive from the server response to. The detail design is still
 * under working.</p>
 *
 * <p>{@link Type#message} is a conversation message. It doesn't have an action.
 * It should set {@link SBMessage#receiver} and {@link SBMessage#body} fields.</p>
 *
 * <p>{@link Type#control} is a general purpose message that is used to perform
 * something. It uses {@link SBMessage#action} field to specify what exactly
 * it wants to do. Currently it supports</p>
 *
 * <ul>
 *     <li>{@link Action#signup} for user signup, with {@link SBMessage#sender}
 *     (for username) and {@link SBMessage#body} (for password) set;</li>
 *     <li>{@link Action#login} for user login, with {@link SBMessage#sender}
 *     (for username) and {@link SBMessage#body} (for password) set;</li>
 *     <li>{@link Action#bye} for user logoff, which doesn't need any fields</li>
 *     <li>{@link Action#userlist} to acquire online user list.</li>
 * </ul>
 *
 * <p>{@link Type#info} is a message sent by the server to tell client some
 * information. Currently this type isn't being used.</p>
 */
public class SBMessage implements Serializable {

    /** Message type. */
    public Type type;

    /** Message action, associated to its type. */
    public Action action;

    /** Time this instance initialized. */
    public Date timestamp = new Date();

    /** Sequence number. Invalid if smaller than 0. */
    public int sequence = -1;

    /** Sender username, if any. */
    public String sender;

    /** Recipient username, if any. */
    public String receiver;

    /** Message body. */
    public String body;

    /**
     * General purpose flag. You can use it to indicate something that is
     * either true of false, e.g., whether login succeeds or not, etc.
     */
    boolean flag;

    public SBMessage() {
    }

    public SBMessage setType(Type type) {
        this.type = type;
        return this;
    }

    public SBMessage setAction(Action action) {
        this.action = action;
        return this;
    }

    public SBMessage setSequence(int sequence) {
        this.sequence = sequence;
        return this;
    }

    public SBMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public SBMessage setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public SBMessage setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public SBMessage setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Action getAction() {
        return action;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getSequence() {
        return sequence;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return "networking.SBMessage{" +
                "type=" + type +
                ", action=" + action +
                ", timestamp=" + timestamp +
                ", sequence=" + sequence +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", body='" + body + '\'' +
                ", flag=" + flag +
                '}';
    }
}

enum Type {
    message, control, info
}

enum Action {
    login, signup, bye, userlist, send, recv
}
