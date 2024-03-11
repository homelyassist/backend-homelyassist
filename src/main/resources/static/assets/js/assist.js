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
        assist_types: getSelectedAssistTypes()
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


async function searchAssist() {
    var payload = {
        pin_code: document.getElementById("pin").value,
        city_area: document.getElementById("village").value,
        assist_types: getSelectedAssistTypes()
    }

    const response = await fetch("/api/assist/agriculture/search", { // make this generic as per category
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": getBearerToken()
        },
        body: JSON.stringify(payload),
    })

    if (!response.ok) {
        throw new Error("Unable to search for an Assist");
    }

    const data = await response.json();
    console.log(data);
    const container = document.getElementById('assistContainer');
    container.innerHTML = '';
    data.assist?.forEach(item => {
        // Create a new div element for the assist card
        const assistCard = document.createElement('div');
        assistCard.classList.add('col-lg-4', 'col-md-6', 'd-flex', 'align-items-stretch');
        assistCard.setAttribute('data-aos', 'fade-up');
        assistCard.setAttribute('data-aos-delay', '100');
  
        // Create HTML structure for the assist card
        assistCard.innerHTML = `
          <div class="assist-card">
            <img src="/assets/img/testimonials/testimonials-5.jpg" alt="${item.name}">
            <h3>${item.name}</h3>
            <p>${item.description}</p>
            <button class="btn btn-request" onclick="openPopup()">Request Mobile Number</button>
          </div>
        `;
  
        // Append the assist card to the container
        container.appendChild(assistCard);
      });
}

function getSelectedAssistTypes() {
    var selectedAssistTypes = [];
    // Get all checkbox elements with name 'assist_type'
    var checkboxes = document.querySelectorAll('input[name="assist_type"]');
    // Iterate over each checkbox
    checkboxes.forEach(function(checkbox) {
        // Check if checkbox is checked
        if (checkbox.checked) {
            // Add the value of the checkbox to the selectedAssistTypes array
            selectedAssistTypes.push(checkbox.value);
        }
    });
    return selectedAssistTypes;
}
