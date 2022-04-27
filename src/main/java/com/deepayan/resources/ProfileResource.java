package com.deepayan.resources;

import com.deepayan.model.Profile;
import com.deepayan.service.ProfileService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("profiles")
public class ProfileResource {

    private ProfileService profileService = new ProfileService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @Path("/{profileName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Profile getProfileByName(@PathParam("profileName") String profileName) {
        return profileService.getProfileByName(profileName);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Profile addProfile(Profile profile) {
        return profileService.addProfile(profile);
    }

    @PUT
    @Path("/{profileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Profile updateProfile(@PathParam("profileName")String profileName, Profile profile) {
        profile.setProfileName(profileName);
        return profileService.updateProfile(profile);
    }

    @Path("/{profileName}")
    @DELETE
    public void deleteProfile(@PathParam("profileName") String profileName) {
        profileService.removeProfile(profileName);
    }
}
