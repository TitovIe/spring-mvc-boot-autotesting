package com.acme.dbo;

import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.acme.dbo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Look ma, no Spring at all!
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    /**
     * see also {@link org.mockito.MockitoAnnotations#openMocks} instead of {@link org.mockito.junit.jupiter.MockitoExtension}
     */
    @Mock private AccountRepository accountRepositoryStub;
    @InjectMocks private AccountService sut;

    @Test
    public void shouldGetNoAccountsWhenEmptyRepository() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.emptyList());
        assertEquals(sut.findAll(), Collections.emptyList());
        verify(accountRepositoryStub).findAll();
    }

    @Test
    public void shouldGetAccountWhenFindById() {
        Account account = new Account(new BigDecimal("1.11"));
        when(accountRepositoryStub.findById(anyInt())).thenReturn(account);
        assertEquals(sut.findById(2), account);
        verify(accountRepositoryStub).findById(2);
    }

    @Test
    public void shouldGetAccountWhenFindAll() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.singletonList(new Account(new BigDecimal("1.11"))));
        assertEquals(sut.findAll().size(), 1);
        verify(accountRepositoryStub).findAll();
    }

    @Test
    public void shouldGetAccountRepoWhenCreate() {
        Account account = new Account(new BigDecimal("2.22"));
        when(accountRepositoryStub.create(any(Account.class))).thenReturn(account);
        assertEquals(sut.create(account), account);
        verify(accountRepositoryStub).create(any(Account.class));
    }
}
