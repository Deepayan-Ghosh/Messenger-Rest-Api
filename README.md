# Messenger REST Api

## Overview
This is a project made while learning about REST Api design and implementation in java using JAX-RS (Jersey).
In this project the backend apis for a messenger app are defined using which users can post messages and comment 
on those messages. Since the focus was on learning REST Api, the database part has been replaced by a stub map implementation.

To illustrate the concept for resource, the project defines two resources - `Message` and `Profile`.
Below are the endpoints for `Message` resource:
- To get all the messages (Collection URI)
```
GET http://localhost:8080/rest_messenger_app/rest/messages
```
- To get a particular message with id
```
GET http://localhost:8080/rest_messenger_app/rest/messages/{messageId}
```
- To Create a message, send message data in json or xml format to Collection URI as
```
POST http://localhost:8080/rest_messenger_app/rest/messages
```
- To Update a message with id as `messageId`, send modified message data in json or xml format to
```
PUT http://localhost:8080/rest_messenger_app/rest/messages/{messageId}
```
- To Delete a message
```
DELETE http://localhost:8080/rest_messenger_app/rest/messages/{messageId}
```

For resource `Profile` similar endpoints are present

These resources are root level resources. In certain cases, sub resources are also possible. To illustrate dealing with sub-resources, 
the project deals with creating endpoints for `Comment` resource. One or more `Comment`s always belongs to a `Message` resource. So `Comment` 
is a sub-resource of `Message`. The endpoints for dealing with `Comment`s are:

- To get all comments belonging to a message with id `messageId`
```
GET http://localhost:8080/rest_messenger_app/rest/messages/{messageId}/comments
```
- To get a particular comment with `commentId` for a message 'messageId'
```
GET http://localhost:8080/rest_messenger_app/rest/messages/{messageId}/comments/{commentId}
```
- To Create a comment for message `messageId`
```
POST http://localhost:8080/rest_messenger_app/rest/messages/{messageId}/comments
```
- To Update a comment with `commentId` for message `messageId`
```
PUT http://localhost:8080/rest_messenger_app/rest/messages/{messageId}/comments/{commentId}
```
- To delete a comment with `commentId` for message `messageId`
```
DELETE http://localhost:8080/rest_messenger_app/rest/messages/{messageId}/comments/{commentId}
```

The endpoints are present in JAX-RS classes `MessageResource`,`ProfileResource` and `CommentResource`.

## Deployment
This project has been developed to be deployed on servlet container like Tomcat which support servlet 2.5 and above.
Traditional deployment descriptor is used for deployment, where all incoming requests at `/rest/*` 
are redirected to the Jersey container `org.glassfish.jersey.servlet.ServletContainer` which processes the remaining 
portion of the resource URI endpoints and hands over control to appropriate methods in our JAX-RS resource classes.

## Concepts / Annotations Covered In This Project
- `@Path` - used to map a class / method to handle a URI invocation. In JAX-RS, for each resource we have a class which contains the 
api endpoints for that resource. In REST design, all resource URIs have the resource name in plural followed by the resourceId.
This plural resource name part of the URI is present in @Path definition at class level. The @Path defined at the method are 
appended to the @Path defined at class level. In our project, endpoints for `Message` is handled by `MessageResource` which
 is annotated with `@Path("messages")`. Every URI endpoint defined inside this class will be prefixed with `/messages`


- `@GET/@POST/@PUT/@DELETE` - Used at method level. Defines what HTTP verb is handled by the method. One single URI 
with different verbs in REST can perform different operations, like GET is used to fetch resource, POST to create a new resource, 
PUT to update an existing resource and DELETE to delete a resource. So, the combination of URI and HTTP verb is mapped to a method to handle
the operation intended by that verb on the resource.


- `@Produces` - It provides a list of media types that can be returned by the endpoint. In our resource method, the resource or list
of resources is returned and not the representation like  JSON/XML which the client expects. JAX-RS is responsible for 
converting the resource into the final format. The possible formats which this endpoint supports is given by the 
`@Produces` annotation at method level. The final format returned depends on the value of the `Accept` header sent by client.
The client can send a list of formats in which the resource is acceptable to client as part of `Accept` header. Out of this,
the final format which is sent is also intimated to the client by setting the `Content-type` response header. For example,
the client might send `Accept: text/xml, application/json` but the endpoint supports only json, then json formatted resource
is sent back and the `Content-type: application/json` is set ont to the response.

    If the client expects the resource in a format the endpoint doesn't support, `406 Not Acceptable` status code is sent back by the server in the response.


- `@Consumes` - PUT and POST requests contain payload in the request body which can be in any format. The format of the 
request payload supported by our api endpoint is mentioned at the method level using `@Consumes`. It tells JAX-RS
that the endpoint expects the incoming request payload to be in these formats, so that JAX-RS can appropriately
transform the incoming request payload into object and inject them into the method parameter. For example, an api 
endpoint method may accept JSON and the request payload may be in XML. In such case, the endpoint cannot handle the request
and sends an error code status `415 Unsupported Media Type` in the response. The format of the request payload is given
in the `Content-type` request header.

  
- `@PathParam` - used to capture the variable portions of URI. For example, in the URIs `/messages/1` and `/messages/2` 
the message id is the variable part. To capture this, the URI is defined as `@Path("/messages/{messageId}")` where messageId
is the placeholder, and it can be captured by `@PathParam("messageId")` and injected into a method parameter. The
parameter name passed to PathParam should match the placeholder used while defining the URI in @Path.


- `@QueryParam` - Used to capture the value of query parameters from the query string in the URI. The query string is the part of 
URI after `?`. The name of the query parameter must be known beforehand as it needs to be passed to `@QueryParam` so that
the value of that param can be captured and injected into method parameter.


- `@HeaderParam` - Used to capture the value of particular request header. 


- `@CookieParam` - Used to capture the value of particular cookie.


- `@BeanParam` - All these param annotations are defined at method parameter level. The values are captured and injected 
into method parameters. If there are too many such annotations in method signature, it gets cluttered. A way to make it cleaner
is to create a class and place all these annotated parameters with getters-setters inside the class. Then, define a method
parameter of this class type and annotate with `@BeanParam`. Example in our project is class `MessageFilterBean`.


- `@Context` - Unlike other param annotations which can be used with primitive types,
`@Context` can be used only with special types like `UriInfo`,`HttpHeader`, etc. It injects special objects which contains
contextual information. For example, when used with UriInfo parameter like `@Context UriInfo uriInfo` an instance of
UriInfo is injected which contains metadata about the URI like the current URI, base URI, absolute URI, the path segments,
the query parameters, etc. This is useful when we don't know the parameter names beforehand and hence cannot use `@QueryParam`.
We can obtain the list of query parameters from the injected UriInfo object. Similarly `@Context HttpHeader httpHeader` 
will inject an object of type HttpHeader which contain metadata about all the request headers and cookies. This can be
useful when we don't know the exact cookie names which can be used with `@CookieParam`. Using HttpHeader we can iterate
over all the cookies.


- `@Provider` - It is used on classes which implement special interfaces that allow us to hook into Jersey runtime 
and execute certain functions. One example of this is when exception occurs which is not handled, it bubbles up from 
our method and reaches Jersey which does nothing and the exception bubbles up further to tomcat container which returns 
an HTML error page. To make Jersey handle this exception, an exception mapper class which implements `ExceptionMapper`
is created. This class must be marked with `@Provider` so that JAX-RS registers this as JAX-RS resource. 
@Provider classes are singletons.
