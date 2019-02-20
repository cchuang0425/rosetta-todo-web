'use strict';

// Declaration
const NOT_YET = -1;
const NEW = 0;
const DONE = 1;

var Todo = Todo || {};

Todo.Task = class Task {
    constructor(_id, _task, _status) {
        this.id = _id || NOT_YET;
        this.task = _task || "";
        this.status = _status || NOT_YET;
    }
};

// Initialize
Todo.init = () => {
    Auth.checkAuth();
    Todo.clean();
    Todo.fetch();
};

// Task Events
Todo.add = (_event) => {
    if (Util.isEnterPressed(_event)) {
        let $newTask = document.querySelector("#newTask");
        Todo.sendNewTask($newTask.value.trim());
    }
};

Todo.sendNewTask = (_newTask) => {
    if (!Util.isEmptyStr(_newTask)) {
        let _task = new Todo.Task(null, _newTask, null);
        fetch(RESOURCE_TODO, {
                body: JSON.stringify(_task),
                cache: 'no-cache',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'POST'
            }).then(Util.parseJson)
            .then(Todo.checkResult);
    }
};

Todo.checkResult = (result) => {
    if (result.succeed) {
        Todo.fetch();
        Todo.clean();
    }
};

Todo.edit = (_event) => {
    let $input = _event.target;
    $input.readOnly = false;
    $input.onkeypress = Todo.update;
};

Todo.update = (_event) => {
    if (Util.isEnterPressed(_event)) {
        let _id = _event.target.dataset.id;
        let _text = _event.target.value.trim();
        let _task = new Todo.Task(_id, _text, NEW);

        fetch(Todo.buildURL(_id), {
                body: JSON.stringify(_task),
                cache: 'no-cache',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'PUT'
            }).then(Util.parseJson)
            .then(Todo.checkResult);
    }
};

Todo.buildURL = (_id) => RESOURCE_TODO + "/" + _id;

Todo.done = (_event) => {
    let _id = _event.target.value;

    fetch(Todo.buildURL(_id), {
            headers: {
                'Accept': 'application/json'
            },
            method: 'DELETE'
        }).then(Util.parseJson)
        .then(Todo.checkResult);
};

// DOM Process
Todo.clean = () => {
    let $newTask = document.querySelector("#newTask");
    $newTask.value = '';
};

Todo.fetch = () => {
    fetch(RESOURCE_TODO, {
            headers: {
                'Accept': 'application/json'
            }
        }).then(response => response.json())
        .then(_taskList => Todo.refresh(_taskList));
};

Todo.refresh = function (_taskList) {
    let $taskList = document.querySelector("#taskList");
    let $oldList = $taskList.querySelector("tbody");
    let $newList = Todo.build$TaskList(_taskList);

    $taskList.replaceChild($newList, $oldList);
};


Todo.build$TaskList = function (_taskList) {
    let $taskList = document.createElement("tbody");

    _taskList.forEach((_task) => {
        $taskList.appendChild(Todo.build$Row(_task));
    })

    return $taskList;
};

Todo.build$Row = function (_task) {
    let $row = document.createElement("tr");

    $row.appendChild(Todo.build$ColId(_task));
    $row.appendChild(Todo.build$ColTask(_task));
    $row.appendChild(Todo.build$ColStatus(_task));

    return $row;
};

Todo.build$ColId = (_task) => {
    let $colId = document.createElement("td");
    $colId.innerHTML = _task.id;
    return $colId;
};

Todo.build$ColTask = (_task) => {
    let $colTask = document.createElement("td");
    let $task = Todo.create$EditableText(Todo.build$IdEdit(_task.id), _task.task, {
        id: _task.id
    });

    $colTask.appendChild($task);

    return $colTask;
};

Todo.build$ColStatus = (_task) => {
    let $colStatus = document.createElement("td");

    $colStatus.appendChild(Todo.create$Checkbox(Todo.build$IdStatus(_task), _task.id, null, (_task.status === DONE)));

    return $colStatus;
};

Todo.build$IdStatus = (_task) => "done-" + _task.id;

Todo.build$IdEdit = (_id) => "editor-" + _id;

Todo.create$Checkbox = (_id, _value, _label, _checked, _data) => {
    let $checkbox = document.createElement("input");

    $checkbox.type = "checkbox";
    $checkbox.id = _id;
    $checkbox.value = _value;
    $checkbox.checked = _checked;
    $checkbox.onchange = Todo.done;

    return Todo.create$Label(_label, $checkbox)
};

Todo.create$Textfield = (_id, _value, _keypress) => {
    let $textfield = document.createElement("input");

    $textfield.type = "text";
    $textfield.id = _id;
    $textfield.value = _value;
    $textfield.onkeypress = _keypress;

    return $textfield;
};

Todo.create$Label = (_label, $input) => {
    let $label = document.createElement("label");

    $label.innerHTML = (Util.isEmptyStr(_label)) ? "" : _label;
    $label.appendChild($input);

    return $label;
};

Todo.create$EditableText = (_id, _text, _data) => {
    let $input = document.createElement("input");

    $input.type = "text";
    $input.value = _text;
    $input.id = _id;
    $input.name = _id;
    $input.readOnly = true;
    $input.ondblclick = Todo.edit;

    $input = Todo.set$Data($input, _data);

    return $input;
};

Todo.set$Data = ($dom, _data) => {
    for (let _prop in _data) {
        if (_data.hasOwnProperty(_prop)) {
            $dom.dataset[_prop] = _data[_prop];
        }
    }

    return $dom;
};
