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

function showPopupNotification(message) {
    // Create notification div
    var notificationDiv = document.createElement('div');
    notificationDiv.className = 'popup-notification';
    var toastBody = document.createElement('div');
    toastBody.className = 'toast-body bg-secondary text-white';
    toastBody.textContent = message;
    notificationDiv.appendChild(toastBody);

    // Add notification to container
    document.body.appendChild(notificationDiv);

    // Automatically remove notification after 5 seconds
    setTimeout(function () {
        notificationDiv.parentNode.removeChild(notificationDiv);
    }, 1500);
}


function validatePassword(password) {
    if (password != null && (password.length < 6 || password.length > 10)) {
        alert("Password must be between 6 and 10 characters long.");
        return false;
    }

    return true;
}