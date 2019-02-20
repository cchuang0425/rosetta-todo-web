package org.rosetta.todoweb.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rosetta.todoweb.domain.Response;
import org.rosetta.todoweb.domain.Todo;
import org.rosetta.todoweb.persistence.TodoDAO;
import org.rosetta.todoweb.service.TodoService;
import org.rosetta.todoweb.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;
import static org.rosetta.todoweb.web.TodoAPI.RESOURCE_TASKS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoAPITest {

    private static final String TEST_TASK = "test task";

    @Autowired
    private MockMvc mockMvc;

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
    public void test$getNewTasks() throws Exception {
        this.mockMvc.perform(
                get(String.format("%s%s", RESOURCE_TODO, RESOURCE_TASKS))
                        .accept(MediaType.APPLICATION_JSON))
                    .andDo(result -> assertThat(result.getResponse().getContentAsString()).contains("[]"));
    }

    @Test
    public void test$saveNewTask() throws Exception {
        var task = new Todo(TEST_TASK);
        this.mockMvc.perform(
                post(String.format("%s%s", RESOURCE_TODO, RESOURCE_TASKS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.getJSONFromObject(task))
                        .accept(MediaType.APPLICATION_JSON))
                    .andDo(result ->
                            assertThat(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                          .getContentAsString())
                                                .get(Response.KEY_SUCCEED)
                                                .asBoolean())
                                    .isTrue());
    }

    @Test
    public void test$updateTask() throws Exception {
        do$saveTask();
        var task = getFirstTask();
        task.setTask(TEST_TASK + "@" + System.currentTimeMillis());

        this.mockMvc.perform(
                put(String.format("%s%s/%d", RESOURCE_TODO, RESOURCE_TASKS, task.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONUtils.getJSONFromObject(task))
                        .accept(MediaType.APPLICATION_JSON))
                    .andDo(result ->
                            assertThat(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                          .getContentAsString())
                                                .get(Response.KEY_SUCCEED)
                                                .asBoolean())
                                    .isTrue());
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
    public void test$updateDone() throws Exception {
        do$saveTask();
        var task = getFirstTask();

        this.mockMvc.perform(
                delete(String.format("%s%s/%d", RESOURCE_TODO, RESOURCE_TASKS, task.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                    .andDo(result ->
                            assertThat(JSONUtils.getJSONTreeFromRaw(result.getResponse()
                                                                          .getContentAsString())
                                                .get(Response.KEY_SUCCEED)
                                                .asBoolean())
                                    .isTrue());
    }
}
