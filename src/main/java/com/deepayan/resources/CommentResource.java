package com.deepayan.resources;

import com.deepayan.model.Comment;
import com.deepayan.service.CommentService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.glassfish.jersey.server.Uri;

import java.net.URI;
import java.util.List;

@Path("/")
public class CommentResource {

    private CommentService commentService = new CommentService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllComments(@PathParam("messageId") Long messageId) {
        return commentService.getAllComments(messageId);
    }

    @Path("/{commentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Comment getAllComments(@PathParam("commentId") Long commentId,
                                  @PathParam("messageId") Long messageId) {
        return commentService.getCommentById(messageId, commentId);
    }

    /*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment createComment(@PathParam("messageId") Long messageId, Comment comment) {
        return commentService.createNewCommentForMessage(messageId, comment);
    }*/

    // Taking control of the response.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("messageId") Long messageId,
                                  @Context UriInfo uriInfo,
                                  Comment comment) {
        Comment newComment = commentService.createNewCommentForMessage(messageId, comment);
        URI newCommentUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newComment.getId())).build();
        return Response.created(newCommentUri).entity(newComment).build();
    }

    @Path("/{commentId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment updateComment(@PathParam("messageId") Long messageId,
                                 @PathParam("commentId") Long commentId,
                                 Comment comment) {
        comment.setId(commentId);
        return commentService.updateCommentForMessage(messageId, commentId, comment);
    }

    @DELETE
    @Path("/{commentId}")
    public void deleteComment(@PathParam("commentId") Long commentId,
                              @PathParam("messageId") Long messageId) {
        commentService.removeComment(messageId, commentId);
    }
}
