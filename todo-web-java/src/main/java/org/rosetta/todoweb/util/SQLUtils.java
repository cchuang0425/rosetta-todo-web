package org.rosetta.todoweb.util;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class SQLUtils {
    private static final String FOLDER_SQL = "/sql/";
    private static final String FILE_EXTENSION = ".yml";

    public static String loadSQL(String file, String id) throws IOException {
        var sqlRaw = IOUtils.readTextInClasspath(FOLDER_SQL + file + FILE_EXTENSION);
        var sqlContent = new Yaml(new Constructor(SQL.class));
        var sqlItems = sqlContent.loadAll(sqlRaw);

        var sqlItem = StreamSupport.stream(sqlItems.spliterator(), false)
                                   .filter(sql -> ((SQL) sql).getId().equals(id))
                                   .findFirst()
                                   .orElse(SQL.EMPTY);

        return ((SQL) sqlItem).getSql();
    }

    public static class SQL {
        public static SQL EMPTY = new SQL("", "");

        public SQL() {
        }

        public SQL(String id, String sql) {
            this.id = id;
            this.sql = sql;
        }

        private String id;
        private String sql;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }

}
