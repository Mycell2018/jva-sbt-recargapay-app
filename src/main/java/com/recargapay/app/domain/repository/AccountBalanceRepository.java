package com.recargapay.app.domain.repository;

import com.recargapay.app.domain.model.AccountBalance;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, UUID> {}
