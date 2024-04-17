async function generateOtp() {
    var mobileNumber = document.getElementById("mobile").value;
    if (!mobileNumber) {
        alert("Please enter a mobile number");
        return;
    }

    var formData = {
        phone_number: mobileNumber,
    };

    const response = await fetch("/api/auth/otp/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
    });

    if (!response.ok) {
        alert("Failed to generate OTP. Please try again.");
        throw new Error("Failed to generate OTP. Please try again.");
    }

    const data = await response.json();

    document.getElementById('otpButton').textContent = 'Resend OTP';
    document.getElementById('otpButton').disabled = true;
    var otpDisplay = document.getElementById('otpDisplay')
    if (otpDisplay) {
        document.getElementById('otpDisplay').innerText = "Your OTP is: " + data.code;
    }
    resendOptTimer();
}

async function validateOtp() {
    mobile = document.getElementById("mobile").value;
    otp = document.getElementById("otp").value;

    if (!otp || !mobile) {
        console.log("Please enter a valid moblie/otp");
        return;
    }

    var payload = {
        phone_number: mobile,
        otp: otp,
    };

    try {
        const response = await fetch("/api/auth/otp/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            throw new Error("Failed to validate OTP");
        }
        const data = await response.json();

        if (data.status === "SUCCESS" && data.token) {
            // Store token in local storage
            localStorage.setItem("token", data.token);
            return true;
        } else {
            throw new Error("Failed to validate OTP");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Invalid OTP. Please enter a valid OTP.");
        return false;
    }
}

async function generateAnonymousToken() {
    const mobile = document.getElementById("mobile").value;
    const otp = document.getElementById("otp").value;

    if (!otp || !mobile) {
        console.log("Please enter a valid moblie/otp");
        return;
    }

    var payload = {
        phone_number: mobile,
        otp: otp,
    };

    const response = await fetch("/api/auth/token/anonymous/request", { // make this generic as per category
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
    });

    if (!response.ok) {
        throw new Error("Unable to search for an Assist");
    }

    const data = await response.json();

    if (data.status === "SUCCESS" && data.token) {
        localStorage.setItem("m_token", data.token);
        localStorage.setItem("m_vid", data.vid);
        localStorage.setItem("m_expiry", data.expiry);
        localStorage.setItem("m_phone_number", data.phone_number);
        return true;
    } else {
        alert("Failed to validate otp");
        return false;
    }
    // put this in local stroage
}

function resendOptTimer() {
    var countdown = 60;
    var timerElement = document.getElementById('timer');
    timerElement.textContent = countdown + 's';

    var countdownInterval = setInterval(function () {
        countdown--;
        var otpButton = document.getElementById('otpButton')

        if (otpButton.disabled == true) {
            timerElement.textContent = countdown + 's';
        }

        if (countdown <= 0) {
            clearInterval(countdownInterval);
            otpButton.disabled = false;
            timerElement.textContent = '';
        }
    }, 1000);
}