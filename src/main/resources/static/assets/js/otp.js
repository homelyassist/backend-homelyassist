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
}

async function validateOtp() {
    mobile = document.getElementById("mobile").value;
    otp =  document.getElementById("otp").value;

    if (!otp || !mobile) {
        console.log("Please enter a valid moblie/otp");
        return;
    }

    var payload = {
        phoneNumber: mobile,
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