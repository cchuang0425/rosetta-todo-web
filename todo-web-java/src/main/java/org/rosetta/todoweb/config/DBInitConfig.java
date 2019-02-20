package org.rosetta.todoweb.config;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.rosetta.todoweb.persistence.InfoDAO;
import org.rosetta.todoweb.util.HashUtils;
import org.rosetta.todoweb.util.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static org.rosetta.todoweb.persistence.InfoDAO.FILE_INFO;
import static org.rosetta.todoweb.persistence.InfoDAO.SQL_DELETE_ALL;
import static org.rosetta.todoweb.persistence.InfoDAO.SQL_INSERT;

@Configuration
public class DBInitConfig {

    private static final String FILE_INIT = "init";
    private static final String[] SQL_INIT = {"createTodo", "createInfo"};

    private static final String DEFAULT_PASSWORD = "0000";

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        var initSql = loadInitSQL();
        initDBTable(initSql);

        var infoSQL = loadInfoSQL();
        initPassword(infoSQL);
    }

    private void initPassword(Map<String, String> infoSQL) {
        if (infoSQL.isEmpty()) { return; }

        try (var conn = dataSource.getConnection();
                var stat = conn.createStatement()) {
            stat.executeUpdate(infoSQL.get(SQL_DELETE_ALL));
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }

        String clientHash = HashUtils.hashToSHA256(DEFAULT_PASSWORD);
        String serverHash = HashUtils.hashToSHA256(clientHash);

        try (var conn = dataSource.getConnection();
                var pstat = conn.prepareStatement(infoSQL.get(SQL_INSERT))) {
            pstat.setString(1, serverHash);
            pstat.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private Map<String, String> loadInfoSQL() {
        Map<String, String> sqlMap = new HashMap<>();

        try {
            sqlMap.put(SQL_INSERT, SQLUtils.loadSQL(FILE_INFO, SQL_INSERT));
            sqlMap.put(SQL_DELETE_ALL, SQLUtils.loadSQL(FILE_INFO, SQL_DELETE_ALL));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            return sqlMap;
        }
    }

    private String[] loadInitSQL() {
        try {
            List<String> sqls = new ArrayList<>();

            for (var sql : SQL_INIT) {
                sqls.add(SQLUtils.loadSQL(FILE_INIT, sql));
            }

            return sqls.toArray(new String[sqls.size()]);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return new String[0];
        }
    }

    private void initDBTable(String[] initSql) {
        if (initSql.length == 0) { return; }

        try (var conn = dataSource.getConnection();
                var stat = conn.createStatement()) {
            for (var sql : initSql) {
                stat.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
