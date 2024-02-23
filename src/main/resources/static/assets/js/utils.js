function generateOtp() {
    var mobileNumber = document.getElementById("mobile").value;
    if (!mobileNumber) {
        alert("Please enter a mobile number");
        return;
    }

    var formData = {
        phone_number: mobileNumber,
    };

    fetch("/api/auth/otp/generate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
        })
        .catch((error) => {
            console.error("Error:", error);
            alert("Failed to generate OTP. Please try again.");
        });
}

async function validateOtp() {
    mobile = document.getElementById("mobile").value,
    otp =  document.getElementById("otp").value;

    if (!otp || !mobile) {
        console.log("Please enter a valid moblie/otp");
        return;
    }

    var formData = {
        phoneNumber: mobile,
        otp: otp,
    };

    try {
        const response = await fetch("/api/auth/otp/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
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

async function registerAssist() {
    var payload = {
        name: document.getElementById("fullname").value,
        phone_number: document.getElementById("mobile").value,
        address: document.getElementById("address").value,
        state: "Odisha", // This has to be change in html code 
        district: document.getElementById("district").value,
        pin_code: document.getElementById("pin").value,
        city_area: document.getElementById("village").value,
        landmark: document.getElementById("mobile").value,
        description: document.getElementById("description").value,
        assist_types: ["agriculture"]
    };

    const response = await fetch("/api/member/agriculture/register", { // make this generic as per category
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": getBearerToken()
        },
        body: JSON.stringify(payload),
    })

    if (!response.ok) {
        throw new Error("Failed to register");
    }

    window.location.href="/availability"
}


function getBearerToken() {
    var token = localStorage.getItem('token');
    return 'Bearer ' + token;
}
