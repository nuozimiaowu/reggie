function isValidUsername(str) {
    return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal(path) {
    return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone(val) {
    if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
        return false
    } else {
        return true
    }
}

//校验账号
function checkUserName(rule, value, callback) {
    if (value == "") {
        callback(new Error("请输入账号"))
    } else {
        callback()
    }
}

//校验姓名
function checkName(rule, value, callback) {
    if (value == "") {
        callback(new Error("请输入姓名"))
    } else {
        callback()
    }
}

function checkPhone(rule, value, callback) {
    if (value == "") {
        callback(new Error("请输入手机号"))
    } else {
        callback()
    }
}


function validID(rule, value, callback) {
    if (value == '') {
        callback(new Error('请输入身份证号码'))
    } else {
        callback()
    }
}