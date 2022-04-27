package com.deepayan.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/injectdemo")
public class InjectContextInfoResource {

    @GET
    @Path("/annotations")
    @Produces(MediaType.TEXT_PLAIN)
    public String testDiffInfoUsingAnnotations(@MatrixParam("year") Integer year,
                                               @HeaderParam("authSessionId") String authId,
                                               @CookieParam("name") String name) {
        return "Year = " + year + " :: Auth Id = " + authId + "  :: Cookie-> name = " + name;
    }

    @GET
    @Path("/context")
    @Produces(MediaType.TEXT_PLAIN)
    public String testDiffInfoUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
        String path = uriInfo.getAbsolutePath().getPath();
        String allQueryParams = uriInfo.getQueryParameters().toString();
        String authId = httpHeaders.getRequestHeader("authSessionId").toString();
        String cookieName = httpHeaders.getCookies().get("name").getValue();
        return "Path = " + path + "allQueryParams = " + allQueryParams + " :: Auth Id = " + authId + "  :: Cookie-> name = " + cookieName;
    }

}
