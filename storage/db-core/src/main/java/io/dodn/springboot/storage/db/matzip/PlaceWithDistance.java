package io.dodn.springboot.storage.db.matzip;

public interface PlaceWithDistance {
    Long getPlaceId();
    String getName();
    String getAddress();
    String getPhoneNumber();
    String getDescription();
    Double getLongitude();
    Double getLatitude();
    Double getDistance();
}
