async function registerAssist() {
    var payload = {
        name: document.getElementById("fullname").value,
        phone_number: document.getElementById("mobile").value,
        address: document.getElementById("address").value,
        state: "Odisha", // This has to be change in html code 
        district: document.getElementById("district").value,
        pin_code: document.getElementById("pin").value,
        city_area: document.getElementById("village").value,
        //landmark: document.getElementById("landmark").value, // Include landmark also
        description: document.getElementById("description").value,
        assist_types: ["agriculture", "farm_land_maintenance", "wood_cutting", "tractor_power_tiller", "animal_selling"] // this will replce one UI change to checkbox
    };

    const response = await fetch("/api/assist/agriculture/register", { // make this generic as per category
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

    const data = await response.json();

    if(data.state == "error") {
        console.log(data.error)
        alert(data.error)
        throw new Error("Failed to register");
    }

    localStorage.setItem('uuid', data.uuid);
    window.location.assign("/assist/availability");
}

async function getAssistData() {
    const uuid = localStorage.getItem('uuid')
    if(!uuid) {
        console.log("uuid is null");
        window.location.assign("/");
    }

    const response = await fetch(`/api/assist/agriculture/${uuid}`, { // make this generic as per category
        method: "GET",
        headers: {
            "Authorization": getBearerToken()
        }
    })

    if (!response.ok) {
        throw new Error("Failed to register");
    }

    const data = await response.json();
    console.log(data);
    return data;
}


async function fetchAssistInfo() {
    const data = await getAssistData();
    updateAvailabilitySelect(data.active)
}


function updateAvailabilitySelect(active) {
    const availabilitySelect = document.getElementById("availability");
    if (active) {
        availabilitySelect.value = "yes";
    } else {
        availabilitySelect.value = "no";
    }
}


async function updateAvailabilityInfo() {
    const uuid = localStorage.getItem('uuid')
    const availability = document.getElementById("availability").value === "yes";
    const response = await fetch(`/api/assist/agriculture/${uuid}/availability`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": getBearerToken()
        },
        body: JSON.stringify({ availability: availability })
    })

    if (!response.ok) {
        alert("Failed to update Availability");
        throw new Error("Failed to update Availability");
    }

}

async function assistLogin() {
    const mobile = document.getElementById("mobile").value;
    const otp =  document.getElementById("otp").value;

    if (!otp || !mobile) {
        console.log("Please enter a valid moblie/otp");
        return;
    }

    var payload = {
        phoneNumber: mobile,
        otp: otp,
    };

    try {
        const response = await fetch("/api/auth/assist/login", {
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
            localStorage.setItem("token", data.token);
            localStorage.setItem("uuid", data.uuid);
            window.location.assign("/assist/availability");
        } else {
            throw new Error("Failed to validate OTP");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Invalid OTP. Please enter a valid OTP.");
    }
}