package com.acme.dbo;

import com.acme.dbo.controller.AccountNotFoundException;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.acme.dbo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Profile("test")
public class AccountServiceIT {
    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountService accountService;

    @Test
    @DirtiesContext
    public void shouldFindByIdWhenExists() throws AccountNotFoundException {
        accountRepository.save(new Account(new BigDecimal("1.11")));

        assertEquals(
                new Account(1, new BigDecimal("1.11")),
                accountService.findById(1));
    }

    @Test
    @DirtiesContext
    public void shouldFindAllWhenExists() {
        accountRepository.saveAll(Arrays.asList(new Account(new BigDecimal("1.11")), new Account(new BigDecimal("2.22"))));
        assertEquals(((ArrayList<Account>) accountService.findAll()).size(), 2);
    }
}
