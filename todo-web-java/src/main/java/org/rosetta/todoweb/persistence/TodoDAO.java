package org.rosetta.todoweb.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rosetta.todoweb.domain.Todo;
import org.rosetta.todoweb.exception.SystemException;
import org.rosetta.todoweb.util.SQLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class TodoDAO {
    private static final String FILE_TODO = "todo";

    private static final String SQL_INSERT = "insert";
    private static final String SQL_UPDATE_TASK = "updateTask";
    private static final String SQL_UPDATE_STATUS = "updateStatus";
    private static final String SQL_SELECT_STATUS = "selectByStatus";
    private static final String SQL_SELECT_ID = "selectById";
    private static final String SQL_SELECT_MAX_ID = "selectMaxId";
    private static final String SQL_DELETE_ALL = "deleteAll";

    private static final String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MAX_ID = "maxId";

    public static final int INIT_ID = 0;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int findMaxId() {
        String sql = loadSQL(SQL_SELECT_MAX_ID);
        return jdbcTemplate.query(sql, rs -> rs.next() ? rs.getInt(KEY_MAX_ID) : INIT_ID);
    }

    public List<Todo> findNew() {
        String sql = loadSQL(SQL_SELECT_STATUS);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, Todo.NEW);

        List<Todo> tasks = new ArrayList<>();

        while (rs.next()) {
            tasks.add(new Todo(
                    rs.getInt(KEY_ID),
                    rs.getString(KEY_TASK),
                    rs.getInt(KEY_STATUS)));
        }

        return tasks;
    }

    public boolean updateTask(Todo task) {
        String sql = loadSQL(SQL_UPDATE_TASK);
        int result = jdbcTemplate.update(sql, task.getTask(), task.getId());
        return result == 1;
    }

    public boolean updateDone(Todo task) {
        String sql = loadSQL(SQL_UPDATE_STATUS);
        int result = jdbcTemplate.update(sql, Todo.DONE, task.getId());
        return result == 1;
    }

    public void removeAll() {
        String sql = loadSQL(SQL_DELETE_ALL);
        jdbcTemplate.update(sql);
    }

    private String loadSQL(String id) {
        try {
            return SQLUtils.loadSQL(FILE_TODO, id);
        } catch (IOException ex) {
            throw new SystemException(ex);
        }
    }

    public boolean save(Todo task) {
        String sql = loadSQL(SQL_INSERT);
        int result = jdbcTemplate.update(sql, task.getId(), task.getTask(), task.getStatus());
        return result == 1;
    }
}
