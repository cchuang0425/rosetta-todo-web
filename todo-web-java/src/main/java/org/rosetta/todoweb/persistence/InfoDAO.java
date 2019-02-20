package org.rosetta.todoweb.persistence;

import java.io.IOException;

import org.rosetta.todoweb.exception.SystemException;
import org.rosetta.todoweb.util.SQLUtils;
import org.rosetta.todoweb.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InfoDAO {
    public static final String FILE_INFO = "info";

    public static final String SQL_INSERT = "insert";
    public static final String SQL_DELETE_ALL = "deleteAll";
    private static final String SQL_SELECT_PASSWORD = "selectPassword";

    private static final String KEY_PASSWORD = "password";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean updatePassword(String password) {
        String deleteSQL = loadSQL(SQL_DELETE_ALL);
        jdbcTemplate.update(deleteSQL);

        String insertSQL = loadSQL(SQL_INSERT);
        int result = jdbcTemplate.update(insertSQL, password);

        return result == 1;
    }

    public boolean checkPassword(String actual) {
        String sql = loadSQL(SQL_SELECT_PASSWORD);
        try {
            String expect = jdbcTemplate.queryForObject(sql, String.class);
            return expect.equals(actual);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    private String loadSQL(String id) {
        try {
            return SQLUtils.loadSQL(FILE_INFO, id);
        } catch (IOException ex) {
            throw new SystemException(ex);
        }
    }

}
