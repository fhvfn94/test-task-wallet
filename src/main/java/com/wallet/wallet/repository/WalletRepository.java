package com.wallet.wallet.repository;

import com.wallet.wallet.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WalletRepository  extends JpaRepository<WalletEntity, UUID> {
}
