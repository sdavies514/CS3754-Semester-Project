package com.mycompany.chat;
 
import org.primefaces.push.Decoder;
 
/**
 * A Simple {@link org.primefaces.push.Decoder} that decode a String into a {@link ChatMessage} object.
 */
public class MessageDecoder implements Decoder<String,ChatMessage> {
 
    @Override
    public ChatMessage decode(String s) {
        String[] userAndMessage = s.split(":");
        // Sample message is "TJ: HI" or "HI"
        // If there is a colon that means a user sent the message and we have to 
        // decode it properly.
        if (userAndMessage.length >= 2) {
            return new ChatMessage().setUser(userAndMessage[0]).setText(userAndMessage[1]);
        } 
        else {
            return new ChatMessage(s);
        }
    }
}
