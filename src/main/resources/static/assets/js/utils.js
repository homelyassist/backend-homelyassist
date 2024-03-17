function getBearerToken() {
    var token = localStorage.getItem('token');
    return 'Bearer ' + token;
}

function getMemberBearerToken() {
    var token = localStorage.getItem('m_token');
    return 'Bearer ' + token;
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("uuid");
    window.location.assign("/");
}