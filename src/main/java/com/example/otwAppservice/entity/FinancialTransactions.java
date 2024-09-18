package com.example.otwAppservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FinancialTransactions")
public class FinancialTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int fee;

    @Column(nullable = false)
    private String currency;

    private int refunded;

    @Column(name = "refunded_at")
    private String refundedAt;

    private int captured;

    @Column(name = "captured_at")
    private String capturedAt;

    @Column(name = "voided_at")
    private String voidedAt;


    private String description;

    @Column(name = "amount_format")
    private String amountFormat;

    @Column(name = "fee_format")
    private String feeFormat;

    @Column(name = "refunded_format")
    private String refundedFormat;

    @Column(name = "captured_format")
    private String capturedFormat;

    @Column(name = "invoice_id")
    private String invoiceId;

    private String ip;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;


    private String type;
    private String company;
    private String name;
    private String number;

    @Column(name = "gateway_id")
    private String gatewayId;

    @Column(name = "reference_number")
    private String referenceNumber;

    private String token;
    private String message;

    @Column(name = "transaction_url")
    private String transactionUrl;


}
