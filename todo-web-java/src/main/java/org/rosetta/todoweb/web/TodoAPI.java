package org.rosetta.todoweb.web;

import java.util.List;

import org.rosetta.todoweb.domain.Response;
import org.rosetta.todoweb.domain.Todo;
import org.rosetta.todoweb.service.TodoService;
import org.rosetta.todoweb.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.rosetta.todoweb.config.SystemConfig.RESOURCE_TODO;

@RestController
@RequestMapping(RESOURCE_TODO)
public class TodoAPI {

    public static final String RESOURCE_TASKS = "/tasks";

    public static final String VAR_ID = "/{id}";
    public static final String KEY_ID = "id";

    @Autowired
    private TodoService service;

    @RequestMapping(value = RESOURCE_TASKS,
            method = RequestMethod.GET)
    public List<Todo> getNewTasks() {
        return service.getNewTasks();
    }

    @RequestMapping(value = RESOURCE_TASKS,
            method = RequestMethod.POST)
    public Response saveNewTask(@RequestBody Todo task) {
        var result = service.saveTask(task);
        return Response.getResponse(result);
    }

    @RequestMapping(value = RESOURCE_TASKS + VAR_ID,
            method = RequestMethod.PUT)
    public Response updateTask(
            @PathVariable(KEY_ID) Integer id,
            @RequestBody Todo task) {
        var check = service.checkId(id);

        if (check) {
            var result = service.updateTask(task);
            return Response.getResponse(result);
        } else {
            return Response.getResponse(check);
        }
    }

    @RequestMapping(value = RESOURCE_TASKS + VAR_ID,
            method = RequestMethod.DELETE)
    public Response updateDone(@PathVariable(KEY_ID) Integer id) {
        var check = service.checkId(id);

        if (check) {
            var result = service.updateDone(id);
            return Response.getResponse(result);
        } else {
            return Response.getResponse(check);
        }
    }
}
