package org.rosetta.todoweb.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.rosetta.todoweb.util.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBInitConfig {

    private static final String INIT_FILE = "init";
    private static final String[] INIT_SQL = {"createTodo", "createInfo"};

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        var initSql = loadInitSQL();
        initDBTable(initSql);
    }

    private String[] loadInitSQL() {
        try {
            List<String> sqls = new ArrayList<>();

            for (var sql : INIT_SQL) {
                sqls.add(SQLUtils.loadSQL(INIT_FILE, sql));
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
