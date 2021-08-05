package com.acme.dbo;

import com.acme.dbo.config.TestConfig;
import com.acme.dbo.controller.AccountController;
import com.acme.dbo.controller.AccountNotFoundException;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@ActiveProfiles("Test")
@TestPropertySource("classpath:application-test.properties")
public class AccountCrudIT {
    @Autowired private AccountRepository accountRepositoryStub;
    @Autowired private AccountController accountController;
    private Account account;

    @BeforeEach
    public void init() {
        account = new Account(0, new BigDecimal("1.131"));
    }

    @Test
    public void shouldGetNoAccountsWhenNoCreated() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.emptyList());
        assertEquals(accountController.findAll(), Collections.emptyList());
        verify(accountRepositoryStub).findAll();
    }

    @Test
    public void shouldGetAccountWhenFindById() throws AccountNotFoundException {
        when(accountRepositoryStub.findById(anyInt())).thenReturn(account);
        assertEquals(accountController.findById(account.getId()), account);
        verify(accountRepositoryStub).findById(anyInt());
    }

    @Test
    public void shouldGetAccountWhenCreateAccount() {
        when(accountRepositoryStub.create(any(Account.class))).thenReturn(account);
        assertEquals(accountController.create(account), account);
        verify(accountRepositoryStub).create(any(Account.class));
    }
}
