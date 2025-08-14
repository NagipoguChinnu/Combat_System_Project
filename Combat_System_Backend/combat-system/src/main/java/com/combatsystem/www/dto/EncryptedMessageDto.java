package com.combatsystem.www.dto;

public class EncryptedMessageDto {
    private String message;
    private String latitude;
    private String longitude;

    // Constructors
    public EncryptedMessageDto() {}

    public EncryptedMessageDto(String message, String latitude, String longitude) {
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

