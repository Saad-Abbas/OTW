package com.example.otwAppservice.utils.enums;

public enum StatusEnum {

    PENDING("pending"),
    COMPLETED("completed"),
    RESOLVED("resolved"),
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
