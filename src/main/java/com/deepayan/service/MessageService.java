package com.deepayan.service;

import com.deepayan.exceptions.ResourceNotFoundException;
import com.deepayan.model.Message;
import com.deepayan.stubs.DatabaseStub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MessageService {

    private Map<Long, Message> messages = DatabaseStub.getMessageMap();

    public MessageService() {
        messages.put(1L, new Message(1L, "Hello World", "Deepayan"));
        messages.put(2L, new Message(2L, "Hello Jersey", "Jersey"));
    }

    public List<Message> getAllMessages() {
        return new ArrayList<Message>(messages.values());
    }

    public Message getMessageById(Long id) {
        Message message = messages.get(id);
        if(message == null)
            throw new ResourceNotFoundException("Message not found with id = " + id);
        return message;
    }

    public Message addMessage(Message message) {
        message.setId(messages.size()+1L);
        messages.put(message.getId(), message);
        return message;
    }

    public Message updateMessage(Message message) {
        if(message.getId() <= 0)
            return null;
        if(!messages.containsKey(message.getId()))
            throw new ResourceNotFoundException("Cannot update message which does not exist with id = " + message.getId());
        messages.put(message.getId(), message);
        return message;
    }

    public Message removeMessage(Long id) {
        if(!messages.containsKey(id))
            throw new ResourceNotFoundException("Cannot delete message which does not exist for id = " + id);
        return messages.remove(id);
    }

    public List<Message> getMessagesByYear(int year) {
        List<Message> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for(Message msg : messages.values()) {
            cal.setTime(msg.getCreated());
            if(cal.get(Calendar.YEAR) == year) {
                result.add(msg);
            }
        }
        return result;
    }

    public List<Message> getMessagesPaginated(int start, int size) {
        ArrayList<Message> allMessages = new ArrayList<>(messages.values());
        if(start+size > allMessages.size())
            return allMessages.subList(start, allMessages.size());
        return allMessages.subList(start, start+size);
    }
}
