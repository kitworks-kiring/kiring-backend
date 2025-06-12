package io.dodn.springboot.storage.db.common.entity;

public enum EventCategory {
    BIRTHDAY("생일"),
    STUDY("스터디"),
    DINNER( "회식"),
    HOLIDAY("휴일"),
    NOTICE("공지");

    private final String description;

    EventCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
