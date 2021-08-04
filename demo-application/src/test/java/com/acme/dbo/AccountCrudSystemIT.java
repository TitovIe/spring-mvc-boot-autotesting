package com.acme.dbo;

import com.acme.dbo.config.Config;
import com.acme.dbo.controller.AccountController;
import com.acme.dbo.controller.AccountNotFoundException;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource("classpath:application-test.properties")
public class AccountCrudSystemIT {
    @Autowired private AccountController accountController;
    @Autowired private AccountRepository accountRepository;

    @Test
    public void shouldGetNoAccountsWhenNoCreated() {
        assertTrue(accountController.findAll().isEmpty());
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenFindByIdAndExist() throws AccountNotFoundException {
        Account account = new Account(new BigDecimal("0.15"));
        accountRepository.create(account);
        assertEquals(accountController.findById(account.getId()), new Account(account.getId(), new BigDecimal("0.15")));
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenFindAllAndExists() {
        Account account = new Account(new BigDecimal("1.15"));
        accountRepository.create(account);
        assertTrue(accountController.findAll().contains(new Account(account.getId(), new BigDecimal("1.15"))));
    }
}
