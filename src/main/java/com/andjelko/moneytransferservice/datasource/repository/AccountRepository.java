package com.andjelko.moneytransferservice.datasource.repository;

import com.andjelko.moneytransferservice.datasource.entities.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findById(Long id);
    
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select a from AccountEntity a where a.id = :id")
    Optional<AccountEntity> findByIdWithLock(Long id);
}
