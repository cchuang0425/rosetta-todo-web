package org.rosetta.todoweb.persistence;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoDAOTest {

    private static final String TEST_TASK = "test task";

    @Autowired
    private TodoDAO dao;

    @Test
    public void testFindMaxId() {
        dao.removeAll();

        int id = dao.findMaxId();

        Assert.assertEquals(TodoDAO.INIT_ID, id);
    }

    @Test
    public void testCRUD() {
        dao.removeAll();

        int id = dao.findMaxId();
        testSave(id);

        List<Todo> tasks = testFindNew();

        Todo task = tasks.get(0);
        testUpdateTask(task);
        testUpdateDone(task);

        testFindNone();
    }

    private void testFindNone() {
        List<Todo> tasks = dao.findNew();
        Assert.assertTrue(tasks.isEmpty());
    }

    private void testUpdateTask(Todo task) {
        task.setTask(TEST_TASK + System.currentTimeMillis());
        Assert.assertTrue(dao.updateTask(task));
    }

    private void testUpdateDone(Todo task) {
        Assert.assertTrue(dao.updateDone(task));
    }

    private List<Todo> testFindNew() {
        List<Todo> tasks = dao.findNew();
        Assert.assertFalse(tasks.isEmpty());
        return tasks;
    }

    private void testSave(int id) {
        Todo task = new Todo(id + 1, TEST_TASK, Todo.NEW);
        Assert.assertTrue(dao.save(task));
    }
}
