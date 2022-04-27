package com.deepayan.stubs;

import com.deepayan.model.Message;
import com.deepayan.model.Profile;

import java.util.HashMap;
import java.util.Map;

public class DatabaseStub {
    private static Map<Long, Message> messageMap = new HashMap<>();
    private static Map<String, Profile> profileMap = new HashMap<>();

    public static Map<Long, Message> getMessageMap() {
        return messageMap;
    }

    public static Map<String, Profile> getProfileMap() {
        return profileMap;
    }
}
