package com.deepayan.service;

import com.deepayan.model.Profile;
import com.deepayan.stubs.DatabaseStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileService {

    private Map<String, Profile> profiles = DatabaseStub.getProfileMap();

    public ProfileService() {
        profiles.put("d0g0bp7", new Profile(1L, "d0g0bp7", "Deepayan", "Ghosh"));
        profiles.put("p0k0mn2", new Profile(2L, "p0k0mn2", "Praveen", "Kumar"));
    }

    public List<Profile> getAllProfiles() {
        return new ArrayList<>(profiles.values());
    }

    public Profile getProfileByName(String profileName){
        return profiles.get(profileName);
    }

    public Profile addProfile(Profile profile) {
        profile.setId(profiles.size()+1L);
        profiles.put(profile.getProfileName(), profile);
        return profile;
    }

    public Profile updateProfile(Profile profile) {
        if(profile.getProfileName().isEmpty())
            return null;
        profiles.put(profile.getProfileName(), profile);
        return profile;
    }

    public void removeProfile(String profileName) {
        profiles.remove(profileName);
    }
}
