'use strict';

var Login = Login || {};

/* Initialize */
Login.init = () => {
    let _clientId = Util.uuid();
    let _hashCid = Util.sha256(_clientId);
    sessionStorage.setItem(CLIENT_ID, _hashCid);

    let $message = document.querySelector("#message");
    $message.innerHTML = '&nbsp;';

    let $password = document.querySelector("#password");
    $password.value = '';
};

/* Event Definition */
Login.enterAuth = (_event) => {
    if (Util.isEnterPressed(_event)) {
        Login.login();
    }
};

Login.login = () => {
    let $password = document.querySelector('#password');
    let _password = $password.value;
    
    Auth.login(_password, Login.forwardTodo, Login.showMessage);
};

/* DOM Process */
Login.forwardTodo = (token) => {
    sessionStorage.setItem(SERVER_TOKEN, token);
    window.location.href = '/todo.html';
};

Login.showMessage = (message) => {
    let $password = document.querySelector("#password");
    $password.value = '';

    let $message = document.querySelector("#message");
    $message.innerHTML = message;
};