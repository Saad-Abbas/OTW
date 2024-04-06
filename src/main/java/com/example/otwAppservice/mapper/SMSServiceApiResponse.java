package com.example.otwAppservice.mapper;

public class SMSServiceApiResponse {
    private String details;


    private int errorCode;

    private Message[] messages;

    // Getters and setters for the fields
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    // Inner class representing the 'messages' array
    public static class Message {
        private String to;
        private String status;
        private String reference;
        private int parts;


        private String messageDetails;


        private int messageErrorCode;

        // Getters and setters for the fields
        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public int getParts() {
            return parts;
        }

        public void setParts(int parts) {
            this.parts = parts;
        }

        public String getMessageDetails() {
            return messageDetails;
        }

        public void setMessageDetails(String messageDetails) {
            this.messageDetails = messageDetails;
        }

        public int getMessageErrorCode() {
            return messageErrorCode;
        }

        public void setMessageErrorCode(int messageErrorCode) {
            this.messageErrorCode = messageErrorCode;
        }
    }
}
