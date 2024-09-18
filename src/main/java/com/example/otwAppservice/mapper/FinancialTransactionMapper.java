package com.example.otwAppservice.mapper;

import com.example.otwAppservice.entity.FinancialTransactions;
import com.example.otwAppservice.mapper.MoyasarResponses.CardPaymentResponse;

public class FinancialTransactionMapper {

    public static FinancialTransactions mapFinancialResponseToFinancialTransactionEntity(CardPaymentResponse response) {
        FinancialTransactions entity = new FinancialTransactions();

        entity.setTransactionId(response.getId());
        entity.setStatus(response.getStatus());
        entity.setAmount(response.getAmount());
        entity.setFee(response.getFee());
        entity.setCurrency(response.getCurrency());
        entity.setRefunded(response.getRefunded());
        entity.setRefundedAt(response.getRefundedAt());
        entity.setCaptured(response.getCaptured());
        entity.setCapturedAt(response.getCapturedAt());
        entity.setVoidedAt(response.getVoidedAt());
        entity.setDescription(response.getDescription());
        entity.setAmountFormat(response.getAmountFormat());
        entity.setFeeFormat(response.getFeeFormat());
        entity.setRefundedFormat(response.getRefundedFormat());
        entity.setCapturedFormat(response.getCapturedFormat());
        entity.setInvoiceId(response.getInvoiceId());
        entity.setIp(response.getIp());
        entity.setCallbackUrl(response.getCallbackUrl());
        entity.setCreatedAt(response.getCreatedAt());
        entity.setUpdatedAt(response.getUpdatedAt());

        // Mapping source fields
        if (response.getSource() != null) {
            entity.setType(response.getSource().getType());
            entity.setCompany(response.getSource().getCompany());
            entity.setName(response.getSource().getName());
            entity.setNumber(response.getSource().getNumber());
            entity.setGatewayId(response.getSource().getGatewayId());
            entity.setReferenceNumber(response.getSource().getReferenceNumber());
            entity.setToken(response.getSource().getToken());
            entity.setMessage(response.getSource().getMessage());
            entity.setTransactionUrl(response.getSource().getTransactionUrl());
        }

        return entity;
    }
}
