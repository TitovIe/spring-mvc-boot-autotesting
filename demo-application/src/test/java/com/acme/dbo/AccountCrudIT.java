package com.acme.dbo;

import com.acme.dbo.config.TestConfig;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.acme.dbo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class AccountCrudIT {
    @Autowired private AccountRepository accountRepositoryStub;
    @Autowired private AccountService accountService;
    private Account account;

    @BeforeEach
    public void init() {
        account = new Account(0, new BigDecimal("1.131"));
    }

    @Test
    public void shouldGetNoAccountsWhenNoCreated() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.emptyList());
        assertEquals(accountService.findAll(), Collections.emptyList());
        verify(accountRepositoryStub).findAll();
    }

    @Test
    public void shouldGetAccountWhenFindById() {
        when(accountRepositoryStub.findById(account.getId())).thenReturn(account);
        assertEquals(accountService.findById(account.getId()), account);
        verify(accountRepositoryStub).findById(account.getId());
    }

    @Test
    public void shouldGetAccountWhenCreateAccount() {
        when(accountRepositoryStub.create(account)).thenReturn(account);
        assertEquals(accountService.create(account), account);
        verify(accountRepositoryStub).create(account);
    }
}
