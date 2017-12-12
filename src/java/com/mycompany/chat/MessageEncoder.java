package com.mycompany.chat;
 
import org.primefaces.json.JSONObject;
import org.primefaces.push.Encoder;
 
/**
 * A Simple {@link org.primefaces.push.Encoder} that decode a {@link ChatMessage} into a simple JSON object.
 */
public final class MessageEncoder implements Encoder<ChatMessage, String> {
 
    //@Override
    public String encode(ChatMessage message) {
        return new JSONObject(message).toString();
    }
}