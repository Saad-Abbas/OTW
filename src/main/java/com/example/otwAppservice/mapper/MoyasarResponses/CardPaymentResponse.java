package com.example.otwAppservice.mapper.MoyasarResponses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardPaymentResponse {
    private String id;
    private String status;
    private int amount;
    private int fee;
    private String currency;
    private int refunded;

    @JsonProperty("refunded_at")
    private String refundedAt;

    private int captured;

    @JsonProperty("captured_at")
    private String capturedAt;

    @JsonProperty("voided_at")
    private String voidedAt;

    private String description;

    @JsonProperty("amount_format")
    private String amountFormat;

    @JsonProperty("fee_format")
    private String feeFormat;

    @JsonProperty("refunded_format")
    private String refundedFormat;

    @JsonProperty("captured_format")
    private String capturedFormat;

    @JsonProperty("invoice_id")
    private String invoiceId;

    private String ip;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private Object metadata;

    private Source source;

    // Getters and Setters for CardPaymentResponse fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getRefunded() {
        return refunded;
    }

    public void setRefunded(int refunded) {
        this.refunded = refunded;
    }

    public String getRefundedAt() {
        return refundedAt;
    }

    public void setRefundedAt(String refundedAt) {
        this.refundedAt = refundedAt;
    }

    public int getCaptured() {
        return captured;
    }

    public void setCaptured(int captured) {
        this.captured = captured;
    }

    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }

    public String getVoidedAt() {
        return voidedAt;
    }

    public void setVoidedAt(String voidedAt) {
        this.voidedAt = voidedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmountFormat() {
        return amountFormat;
    }

    public void setAmountFormat(String amountFormat) {
        this.amountFormat = amountFormat;
    }

    public String getFeeFormat() {
        return feeFormat;
    }

    public void setFeeFormat(String feeFormat) {
        this.feeFormat = feeFormat;
    }

    public String getRefundedFormat() {
        return refundedFormat;
    }

    public void setRefundedFormat(String refundedFormat) {
        this.refundedFormat = refundedFormat;
    }

    public String getCapturedFormat() {
        return capturedFormat;
    }

    public void setCapturedFormat(String capturedFormat) {
        this.capturedFormat = capturedFormat;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    // Source class with Getters and Setters
    public static class Source {
        private String type;
        private String company;
        private String name;
        private String number;

        @JsonProperty("gateway_id")
        private String gatewayId;

        @JsonProperty("reference_number")
        private String referenceNumber;

        private String token;
        private String message;

        @JsonProperty("transaction_url")
        private String transactionUrl;

        // Getters and Setters for Source fields

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getGatewayId() {
            return gatewayId;
        }

        public void setGatewayId(String gatewayId) {
            this.gatewayId = gatewayId;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTransactionUrl() {
            return transactionUrl;
        }

        public void setTransactionUrl(String transactionUrl) {
            this.transactionUrl = transactionUrl;
        }

        @Override
        public String toString() {
            return "Source{" +
                    "type='" + type + '\'' +
                    ", company='" + company + '\'' +
                    ", name='" + name + '\'' +
                    ", number='" + number + '\'' +
                    ", gatewayId='" + gatewayId + '\'' +
                    ", referenceNumber='" + referenceNumber + '\'' +
                    ", token='" + token + '\'' +
                    ", message='" + message + '\'' +
                    ", transactionUrl='" + transactionUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CardPaymentResponse{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", currency='" + currency + '\'' +
                ", refunded=" + refunded +
                ", refundedAt='" + refundedAt + '\'' +
                ", captured=" + captured +
                ", capturedAt='" + capturedAt + '\'' +
                ", voidedAt='" + voidedAt + '\'' +
                ", description='" + description + '\'' +
                ", amountFormat='" + amountFormat + '\'' +
                ", feeFormat='" + feeFormat + '\'' +
                ", refundedFormat='" + refundedFormat + '\'' +
                ", capturedFormat='" + capturedFormat + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", ip='" + ip + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", metadata=" + metadata +
                ", source=" + source +
                '}';
    }
}
