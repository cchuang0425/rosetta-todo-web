'use strict';

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