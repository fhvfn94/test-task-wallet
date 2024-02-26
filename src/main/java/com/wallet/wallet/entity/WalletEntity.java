package com.wallet.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "amount")
    private BigDecimal amount;
}
