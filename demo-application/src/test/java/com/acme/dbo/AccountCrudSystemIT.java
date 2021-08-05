package com.acme.dbo;

import com.acme.dbo.config.Config;
import com.acme.dbo.dao.AccountRepository;
import com.acme.dbo.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * see {@link org.springframework.test.context.junit.jupiter.SpringJUnitConfig} annotation
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@WebAppConfiguration
@TestPropertySource("classpath:application-test.properties")
public class AccountCrudSystemIT {
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private AccountRepository accountRepository;
    @Autowired private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetNoAccountsWhenNoCreated() throws Exception {
        mockMvc.perform(get("/api/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenCreateAndAccountsIsNotEmpty() throws Exception {
        accountRepository.create(new Account(new BigDecimal("2.22")));
        mockMvc.perform(post("/api/account")
                .content(objectMapper.writeValueAsString(new Account(new BigDecimal("1.11"))))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(1.11));
        assertEquals(accountRepository.findById(1), new Account(1, new BigDecimal("1.11")));
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenCreateAndAccountsIsEmpty() throws Exception {
        mockMvc.perform(post("/api/account")
                .content(objectMapper.writeValueAsString(new Account(new BigDecimal("1.11"))))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.amount").value(1.11));
        assertEquals(accountRepository.findById(0), new Account(new BigDecimal("1.11")));
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenFindByIdAndExist() throws Exception {
        accountRepository.create(new Account(new BigDecimal("0.15")));
        accountRepository.create(new Account(new BigDecimal("1.15")));
        mockMvc.perform(get("/api/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(1.15));
    }

    @Test
    @DirtiesContext
    public void shouldGetAccountWhenFindAllAndExists() throws Exception {
        accountRepository.create(new Account(new BigDecimal("0.15")));
        accountRepository.create(new Account(new BigDecimal("1.15")));
        mockMvc.perform(get("/api/account"))
                .andExpect(status().isOk());
                //.andExpect(content().json(objectMapper.writeValueAsString(accountRepository.findAll())));
    }
}
