package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUsersRepository extends JpaRepository<TelegramUsers, Long> {
    Optional<TelegramUsers> findByUserId(Long userId);
}
