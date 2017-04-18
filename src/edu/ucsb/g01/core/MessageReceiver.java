package edu.ucsb.g01.core;

import client.ClientTalker;

/**
 * {@link MessageReceiver} is a handler instance that is called by
 * {@link ClientTalker#recv()} upon message arrival. It is used to handle user-
 * specified task.
 */
public interface MessageReceiver {

    /**
     * Method that is called by {@link ClientTalker#recv()} upon message arrival.
     * @param msg the newly incoming message
     */
    void messageHandler(Message msg);
}
