package org.rosetta.todoweb.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Todo;
import org.rosetta.todoweb.persistence.TodoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.Task;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoServiceTest {

    private static final String TEST_TASK = "test task";

    @Autowired
    private TodoService service;

    @Autowired
    private TodoDAO dao;

    @Before
    public void setUp() {
        dao.removeAll();
    }

    @After
    public void tearDown() {
        dao.removeAll();
    }

    @Test
    public void test$getNewTasks() {
        var tasks = service.getNewTasks();
        Assert.assertNotNull(tasks);
    }

    @Test
    public void test$saveTask() {
        do$saveTask();

        var task = getFirstTask();

        Assert.assertEquals(Todo.NEW, task.getStatus().intValue());
    }

    private Todo getFirstTask() {
        var tasks = service.getNewTasks();
        return tasks.get(0);
    }

    private void do$saveTask() {
        var task = new Todo(TEST_TASK);
        var result = service.saveTask(task);

        Assert.assertTrue(result);
    }

    @Test
    public void test$updateTask() {
        do$saveTask();

        var nTask = getFirstTask();

        String modify = TEST_TASK + "@" + System.currentTimeMillis();
        do$updateTask(nTask, modify);

        var uTask = getFirstTask();
        Assert.assertEquals(modify, uTask.getTask());
    }

    private void do$updateTask(Todo task, String modify) {
        task.setTask(modify);

        var check = service.checkId(task.getId());
        Assert.assertTrue(check);

        var result = service.updateTask(task);
        Assert.assertTrue(result);
    }

    @Test
    public void test$updateDone() {
        do$saveTask();

        var task = getFirstTask();

        var check = service.checkId(task.getId());
        Assert.assertTrue(check);

        var result = service.updateDone(task.getId());
        Assert.assertTrue(result);

        var tasks = service.getNewTasks();
        Assert.assertTrue(tasks.isEmpty());
    }
}
