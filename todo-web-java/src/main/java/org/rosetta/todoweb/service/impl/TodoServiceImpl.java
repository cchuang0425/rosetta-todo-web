package org.rosetta.todoweb.service.impl;

import java.util.List;

import org.rosetta.todoweb.domain.Todo;
import org.rosetta.todoweb.persistence.TodoDAO;
import org.rosetta.todoweb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoDAO dao;

    @Override
    public List<Todo> getNewTasks() {
        return dao.findNew();
    }

    @Override
    public boolean saveTask(Todo task) {
        var id = dao.findMaxId();
        task.setId(id + 1);
        task.setStatus(Todo.NEW);

        return dao.save(task);
    }

    @Override
    public boolean checkId(Integer id) {
        return dao.checkId(id);
    }

    @Override
    public boolean updateTask(Todo task) {
        return dao.updateTask(task);
    }

    @Override
    public boolean updateDone(Integer id) {
        return dao.updateDone(id);
    }
}
