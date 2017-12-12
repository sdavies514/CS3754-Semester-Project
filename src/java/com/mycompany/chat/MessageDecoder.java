package com.mycompany.chat;
 
import org.primefaces.push.Decoder;
 
/**
 * A Simple {@link org.primefaces.push.Decoder} that decode a String into a {@link ChatMessage} object.
 */
public class MessageDecoder implements Decoder<String,ChatMessage> {
 
    //@Override
    public ChatMessage decode(String s) {
        String[] userAndMessage = s.split(":");
        if (userAndMessage.length >= 2) {
            return new ChatMessage().setUser(userAndMessage[0]).setText(userAndMessage[1]);
        } 
        else {
            return new ChatMessage(s);
        }
    }
}