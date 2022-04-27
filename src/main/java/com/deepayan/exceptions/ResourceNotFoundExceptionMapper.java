package com.deepayan.exceptions;

import com.deepayan.model.errors.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException e) {
        ErrorResponse errResp = new ErrorResponse(e.getMessage(),
                Response.Status.NOT_FOUND.getStatusCode(), null);
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errResp)
                .build();
    }
}
