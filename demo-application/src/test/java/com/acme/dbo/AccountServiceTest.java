package com.acme.dbo;

import com.acme.dbo.controller.AccountNotFoundException;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.acme.dbo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Look ma, no Spring at all!
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    /**
     * see also {@link org.mockito.MockitoAnnotations#openMocks} instead of {@link org.mockito.junit.jupiter.MockitoExtension}
     */
    @Mock private AccountRepository accountRepositoryStub;
    @Mock private RestTemplateBuilder restTemplateBuilder;
    @Mock private RestTemplate restTemplateStub;
    @InjectMocks private AccountService sut;

    @Test
    public void shouldGetNoAccountsWhenEmptyRepository() {
        when(accountRepositoryStub.findAll()).thenReturn(Collections.emptyList());

        List<Account> accounts = new ArrayList<>();
        Iterable<Account> accountIterable = sut.findAll();
        accountIterable.forEach(accounts::add);

        verify(accountRepositoryStub).findAll();

        assertTrue(accounts.isEmpty());
    }

    @Test
    public void shouldGetAccountsWhenNoEmptyRepository() {
        when(accountRepositoryStub.findAll()).thenReturn(
                Collections.singletonList(new Account(2, new BigDecimal("2.22"))));

        List<Account> accounts = new ArrayList<>();
        Iterable<Account> accountsIterator = sut.findAll();
        accountsIterator.forEach(accounts::add);

        verify(accountRepositoryStub).findAll();

        assertTrue(accounts.contains(
                new Account(2, new BigDecimal("2.22"))));
    }

    @Test
    public void shouldGetAccountWhenFindById() throws AccountNotFoundException {
        when(accountRepositoryStub.findById(1)).thenReturn(java.util.Optional.of(new Account(1, new BigDecimal("1.1"))));

        assertEquals(sut.findById(1), new Account(1, new BigDecimal("1.1")));
        verify(accountRepositoryStub).findById(1);
    }

    @Test
    public void shouldGetAccountWhenSave() {
        Account account = new Account(2, new BigDecimal("2.22"));
        when(accountRepositoryStub.save(account)).thenReturn(account);

        assertEquals(sut.create(account), account);
        verify(accountRepositoryStub).save(account);
    }
}
