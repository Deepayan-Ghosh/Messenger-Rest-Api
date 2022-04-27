package com.deepayan.service;

import com.deepayan.exceptions.ResourceNotFoundException;
import com.deepayan.model.Comment;
import com.deepayan.model.Message;
import com.deepayan.stubs.DatabaseStub;

import java.util.List;
import java.util.Map;

public class CommentService {

    private Map<Long, Message> messages = DatabaseStub.getMessageMap();

    public List<Comment> getAllComments(Long messageId) {
        if(!messages.containsKey(messageId))
            throw new ResourceNotFoundException("Message not found with id = " + messageId);
        return messages.get(messageId).getCommentList();
    }

    public Comment getCommentById(Long messageId, Long commentId) {
        List<Comment> allComments = getAllComments(messageId);
        return allComments
                .stream().filter(comment -> comment.getId() == commentId)
                .findFirst().orElseThrow(() ->{
                    throw new ResourceNotFoundException(
                            "Comment not found with id = " + commentId + " for message with id = " + messageId
                    );
                });
    }

    public Comment createNewCommentForMessage(Long messageId, Comment comment) {
        List<Comment> allComments = getAllComments(messageId);
        comment.setId(allComments.size()+1L);
        allComments.add(comment);
        return comment;
    }

    public Comment updateCommentForMessage(Long messageId, Long commentId, Comment comment) {
        removeComment(messageId, commentId);
        messages.get(messageId).getCommentList().add(comment);
        return comment;
    }

    public void removeComment(Long messageId, Long commentId) {
        List<Comment> allComments = getAllComments(messageId);
        int i=0;
        for(i=0; i<allComments.size();i++) {
            if(allComments.get(i).getId() == commentId)
                break;
        }
        // TODO; Handle exception if comment does not exist and hence i>size
        if(i>=allComments.size())
            throw new ResourceNotFoundException("Comment not found with id = " + commentId);
        allComments.remove(i);
    }
}
