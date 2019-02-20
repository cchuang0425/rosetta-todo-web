"use strict";

/////////////////////  CONST  ///////////////////////////////

const RESOURCE_LOGIN = "/todo/login";
const RESOURCE_LOGOUT = "/todo/logout";
const RESOURCE_INFO = "/todo/info";
const RESOURCE_TODO = "/todo/tasks";
const RESOURCE_VERIFY = "/todo/verify";

const CLIENT_ID = "client-id";
const SERVER_TOKEN = "server-token";

/////////////////////  UTIL  ////////////////////////////////

var Util = Util || {};

Util.parseJson = (response) => response.json();

Util.isEmptyStr = (_str) => (Util.isNull(_str) || 0 === _str.trim().length);

Util.isNull = (_obj) => (_obj === null || _obj === undefined);

Util.isEnterPressed = (_event) => (_event.keyCode == 13);

/* from: [cythilya](https://cythilya.github.io/2017/03/12/uuid/) */
Util.uuid = () => {
    var d = Date.now();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function') {
        d += performance.now(); //use high-precision timer if available
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

Util.sha256 = (str) => sha256(str);

/////////////////////  AUTH  ////////////////////////////////

var Auth = Auth || {};

Auth.Session = class Session {
    constructor(_clientId, _token) {
        this.clientId = _clientId;
        this.token = _token;
    }
};

Auth.Login = class Login {
    constructor(_clientId, _password) {
        this.clientId = _clientId;
        this.password = _password;
    }
};

Auth.login = (_password, _succeed, _failed) => {
    let _hashPwd = Util.sha256(_password);
    let _hashCid = sessionStorage.getItem(CLIENT_ID);

    fetch(RESOURCE_LOGIN, {
            body: JSON.stringify(new Auth.Login(_hashCid, _hashPwd)),
            cache: 'no-cache',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST'
        }).then(Util.parseJson)
        .then((_result) => (_result.succeed) ? _succeed(_result.message) : _failed(_result.message));
};

Auth.logout = () => {
    fetch(RESOURCE_LOGOUT, {
            body: JSON.stringify(Auth.session()),
            cache: 'no-cache',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST'
        }).then(Util.parseJson)
        .then(Auth.checkLogout);
};

Auth.checkLogout = (_result) => {
    if (_result.succeed) {
        Auth.returnLogin();
    }
};

Auth.returnLogin = () => {
    sessionStorage.removeItem(SERVER_TOKEN);
    sessionStorage.removeItem(CLIENT_ID);
    window.location.href = "/login.html";
};

Auth.session = () => {
    let _clientId = sessionStorage.getItem(CLIENT_ID);
    let _token = sessionStorage.getItem(SERVER_TOKEN);

    return new Auth.Session(_clientId, _token);
};

Auth.checkAuth = () => {
    if (sessionStorage.getItem(CLIENT_ID) === null ||
        sessionStorage.getItem(SERVER_TOKEN) === null) {
        Auth.forwardLogin();
    } else {
        Auth.verify();
    }
};

Auth.forwardLogin = () => {
    window.location.href = '/login.html';
};

Auth.verify = () => {
    fetch(RESOURCE_VERIFY, {
            body: JSON.stringify(Auth.session()),
            cache: 'no-cache',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST'
        }).then(Util.parseJson)
        .then((_result) => {
            if (!_result.succeed) {
                Auth.forwardLogin();
            }
        });
};
