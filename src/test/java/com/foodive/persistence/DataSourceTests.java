package com.foodive.persistence;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@Log4j
public class DataSourceTests {

    @Setter(onMethod_ = {@Autowired})
    private DataSource dataSource;

    @Test
    public void testConnection() {
        try (Connection con = dataSource.getConnection()) {
            log.info(con);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
