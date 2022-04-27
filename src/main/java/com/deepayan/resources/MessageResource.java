package com.deepayan.resources;

import com.deepayan.model.Message;
import com.deepayan.request.bean.MessageFilterBean;
import com.deepayan.service.MessageService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("messages")
public class MessageResource {

    public MessageService messageService = new MessageService();

    /* Commented to demonstrate @BeanParam

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessages(@QueryParam("year") int year,
                                     @QueryParam("start") int start,
                                     @QueryParam("size") int size) {
        if(year > 0)
            return messageService.getMessagesByYear(year);
        if(start>0 && size>0)
            return messageService.getMessagesPaginated(start-1, size);
        return messageService.getAllMessages();
    }*/

    // Demonstrating @BeanParam
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessages(@BeanParam MessageFilterBean filteringConditions) {
        if(filteringConditions.getYear() > 0)
            return messageService.getMessagesByYear(filteringConditions.getYear());
        if(filteringConditions.getStart()>0 && filteringConditions.getSize()>0)
            return messageService.getMessagesPaginated(filteringConditions.getStart()-1, filteringConditions.getSize());
        return messageService.getAllMessages();
    }


    // Content negotiation : when client put Accept : application/json , this method is called
    @GET
    @Path("/{messageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getMessageByIdInJsonFormat(@PathParam("messageId") Long id, @Context UriInfo uriInfo) {
        Message message = messageService.getMessageById(id);

        // Add links for HATEOAS
        message.addLink("self", getUrlForSelf(uriInfo, message));
        message.addLink("profile", getUrlForAuthor(uriInfo, message));
        message.addLink("comments", getUrlForComments(uriInfo, message));
        return message;
    }

    // Content negotiation : when client put Accept : text/xml , this method is called
    @GET
    @Path("/{messageId}")
    @Produces(MediaType.TEXT_XML)
    public Message getMessageByIdInXmlFormat(@PathParam("messageId") Long id, @Context UriInfo uriInfo) {
        Message message = messageService.getMessageById(id);

        // Add links for HATEOAS
        message.addLink("self", getUrlForSelf(uriInfo, message));
        message.addLink("profile", getUrlForAuthor(uriInfo, message));
        message.addLink("comments", getUrlForComments(uriInfo, message));
        return message;
    }


    /* Here JAX-RS is determining the status code of the response which is 200 for default, but for POST we want 201.
       Also populating some response header by default. We want more control over the response.

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Message addMessage(Message message) {
        return messageService.addMessage(message);
    }*/


    // NOTE: Taking control of Response object.
    // We want to send 201 instead of 200 and a link to new message resource in response header "location"
    // When we want more control over Response, our method needs to return a Response object.
    // We create the Response using Response class' fluent builder api and finally call build()
    // Content-Negotation --> Accept: application/json, Content-type: application/json
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Response addMessage(Message message, @Context UriInfo uriInfo) {
        Message newMessage = messageService.addMessage(message);
        URI newMessageUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMessage.getId())).build();
        return Response.status(Response.Status.CREATED)
                .location(newMessageUri)
                .entity(newMessage)
                .build();
    }

    /*// Content-Negotation --> Accept: application/json, Content-type: text/xml
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMessageWithInputAsXmlAndOutputAsJson(Message message, @Context UriInfo uriInfo) {
        Message newMessage = messageService.addMessage(message);
        URI newMessageUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMessage.getId())).build();
        return Response.status(Response.Status.CREATED)
                .location(newMessageUri)
                .entity(newMessage)
                .build();
    }

    // Content-Negotation --> Accept: text/xml, Content-type: text/xml
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_XML)
    public Response addMessageWithInputAsXmlAndOutputAsXml(Message message, @Context UriInfo uriInfo) {
        Message newMessage = messageService.addMessage(message);
        URI newMessageUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMessage.getId())).build();
        return Response.status(Response.Status.CREATED)
                .location(newMessageUri)
                .entity(newMessage)
                .build();
    }*/

    @PUT
    @Path("/{messageId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Message updateMessage(@PathParam("messageId") Long id, Message message) {
        message.setId(id);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(@PathParam("messageId") Long id) {
        messageService.removeMessage(id);
    }

    @Path("/{messageId}/comments")
    public CommentResource commentResource() {
        return new CommentResource();
    }


    // HATEOAS URL creation
    private String getUrlForSelf(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder()
                .path(MessageResource.class)
                .path(Long.toString(message.getId()))
                .build().toString();
    }

    private String getUrlForAuthor(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder()
                .path(ProfileResource.class)
                .path(message.getAuthor())
                .build().toString();
    }

    private String getUrlForComments(UriInfo uriInfo, Message message) {
        return uriInfo.getBaseUriBuilder()
                .path(MessageResource.class)
                .path(MessageResource.class, "commentResource")
                .path(CommentResource.class)
                .resolveTemplate("messageId", message.getId())
                .build().toString();
    }
}
