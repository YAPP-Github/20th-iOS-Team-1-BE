package com.yapp.pet.support;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Tag(value = "integrationTest")
@SpringBootTest
@Sql({"/data.sql"})
public abstract class AbstractIntegrationTest {
}
