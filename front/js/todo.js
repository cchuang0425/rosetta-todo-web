'use strict';

// Declaration
const NOT_YET = -1;
const NEW = 0;
const DONE = 1;

var Todo = Todo || {};

Todo.frontTest = true;

Todo.Task = class Task {
    constructor(_id, _task, _status) {
        this.id = _id || NOT_YET;
        this.task = _task || "";
        this.status = _status || NOT_YET;
    }
};

// Task Events
Todo.add = (_event) => {
    if (Todo.isEnterPressed(_event)) {
        let $newTask = document.querySelector("#newTask");

        console.log("task: " + $newTask.value.trim());

        Todo.sendNewTask($newTask.value.trim());
    }
};

Todo.sendNewTask = (_newTask) => {
    if (!Todo.isEmptyStr(_newTask)) {
        if (Todo.frontTest) {
            let _taskList = Todo.temp;

            _taskList.unshift(new Todo.Task(Todo.getMaxId(_taskList) + 1, _newTask, NEW));

            console.log(_taskList);

            Todo.refresh(_taskList);

            Todo.temp = _taskList;
        }
    }
};

Todo.getMaxId = (_taskList) => (Todo.isNull(_taskList) || _taskList.length == 0) ? 0 : _taskList[0].id;

Todo.edit = (_event) => {
    let $input = _event.target;
    $input.readOnly = false;
    $input.onkeypress = Todo.update;
};

Todo.update = (_event) => {
    if (Todo.isEnterPressed(_event)) {
        if (Todo.frontTest) {
            let _taskList = Todo.temp;

            let _id = _event.target.dataset.id;
            let _text = _event.target.value.trim();

            if (!Todo.isEmptyStr(_text)) {
                let _index = _taskList.findIndex(_task => _task.id == _id);
                _taskList[_index].task = _text;
            }

            Todo.refresh(_taskList);

            Todo.temp = _taskList;
        }
    }
};

Todo.done = (_event) => {
    let _id = _event.target.value;

    if (Todo.frontTest) {
        let _taskList = Todo.temp;
        let _index = _taskList.findIndex(_task => _task.id == _id);

        if (_index != -1) {
            _taskList.splice(_index, 1);
            Todo.refresh(_taskList);
        }

        Todo.temp = _taskList;
    }
};

// Initialize
Todo.init = () => {
    let _taskList = Todo.fetch();

    if (Todo.frontTest) { Todo.temp = _taskList; }

    Todo.refresh(_taskList);
};

Todo.fetch = function () {
    let _taskList = new Array();

    _taskList.push(new Todo.Task(3, "test 03", NEW));
    _taskList.push(new Todo.Task(2, "test 02", NEW));
    _taskList.push(new Todo.Task(1, "test 01", NEW));

    return _taskList;
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
        $taskList.appendChild(Todo.buildRow(_task));
    })

    return $taskList;
};

Todo.buildRow = function (_task) {
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
    let $task = Todo.create$EditableText(Todo.build$IdEdit(_task.id), _task.task, { id: _task.id });

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

    $label.innerHTML = (Todo.isEmptyStr(_label)) ? "" : _label;
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

Todo.isEmptyStr = (_str) => (Todo.isNull(_str) || 0 === _str.trim().length);

Todo.isNull = (_obj) => (_obj === null || _obj === undefined);

Todo.isEnterPressed = (_event) => (_event.keyCode == 13);
