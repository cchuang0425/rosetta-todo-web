'use strict';

var Info = Info || {};

Info.NewPassword = class NewPassword {
    constructor(_password, _session) {
        this.password = _password;
        this.session = _session;
    }
};

Info.initPassword = (_event) => {
    let $setPassword = _event.target;
    Info.enableEdit($setPassword);
};

Info.enableEdit = ($input) => {
    $input.className = "during-input";
    $input.removeAttribute("readonly");
};

Info.setPassword = (_event) => {
    if (Util.isEnterPressed(_event)) {
        let $password = _event.target;
        let _password = $password.value;
        let _hashed = Util.sha256(_password);

        fetch(RESOURCE_INFO, {
                body: JSON.stringify(new Info.NewPassword(_hashed, Auth.session())),
                cache: 'no-cache',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'POST'
            }).then(Util.parseJson)
            .then(Info.checkUpdate);
    }
};

Info.checkUpdate = (_result) => {
    if (_result.succeed) {
        Info.recoverInput();
    }
};

Info.recoverInput = () => {
    let $password = document.querySelector("#setPassword");
    $password.value = "";
    $password.className = "before-input";
    $password.setAttribute("readonly", "readonly");
};
