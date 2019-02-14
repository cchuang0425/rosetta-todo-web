package org.rosetta.todoweb.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBInitConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test$initialize(){
        try (Connection conn = dataSource.getConnection();
                Statement stat = conn.createStatement()) {
            stat.executeUpdate("insert into info (password) values ('');");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
