package com.selector.account;

import com.selector.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Account findByEmail(String email);

    Account findByNickName(String nickName);
}
