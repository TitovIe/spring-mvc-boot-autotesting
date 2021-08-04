package com.acme.dbo.config;

import com.acme.dbo.dao.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

@Configuration
@Import(Config.class)
@Profile("Test")
public class TestConfig {
    private static Logger log = LoggerFactory.getLogger(TestConfig.class);

    @Bean @Primary
    public AccountRepository accountRepositoryStub(@Value("${accounts.repo.init-capacity}") int initCapacity) {
        log.debug("Created accountRepositoryStub with initial capacity {}", initCapacity);
        return mock(AccountRepository.class);
    }
}
