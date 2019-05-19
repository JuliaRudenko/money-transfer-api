package com.revolut.bank.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer {
    private Long id;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal sum;
    private String currency;
    private Date createdAt;

    public Transfer(Long id, Long sourceAccountId, Long destinationAccountId,
                    BigDecimal sum, String currency, Date createdAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.sum = sum;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        return id.equals(transfer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", sum=" + sum +
                ", currency=" + currency +
                ", createdAt=" + createdAt +
                '}';
    }
}
