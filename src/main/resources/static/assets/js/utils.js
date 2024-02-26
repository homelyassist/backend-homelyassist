function getBearerToken() {
    var token = localStorage.getItem('token');
    return 'Bearer ' + token;
}
