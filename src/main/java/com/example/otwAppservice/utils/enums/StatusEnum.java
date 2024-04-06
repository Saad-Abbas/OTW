package com.example.otwAppservice.utils.enums;

public enum StatusEnum {

    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");
    private final String statusText;

    StatusEnum(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }
}
