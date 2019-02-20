package org.rosetta.todoweb.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBInitConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test$initialize() {
        try (Connection conn = dataSource.getConnection();
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery("select password from info;")) {
            Assert.assertTrue(rs.next());
            Assert.assertFalse(StringUtils.isEmpty(rs.getString("password")));
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
