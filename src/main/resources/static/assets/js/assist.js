const category_map = {
    'agriculture_assist': 'agriculture',
    'construction_assist': 'construction'
}

async function registerAssist() {
    const fileInput = document.getElementById('photo');
    const file = fileInput.files[0];

    if (!file) {
        console.error('No file selected.');
        alert("Please select photo");
        return;
    }

    var payload = {
        name: document.getElementById("fullname").value,
        phone_number: document.getElementById("mobile").value,
        state: document.getElementById("state").value,
        district: document.getElementById("district").value,
        block: document.getElementById("block").value,
        village: document.getElementById("village").value,
        experience: document.getElementById("experience").value,
        description: document.getElementById("description").value,
        gender: document.getElementById("gender").value,
        password: document.getElementById("password").value,
        assist_types: getSelectedAssistTypes()
    };

    const category = category_map[document.getElementById("category").value];

    for (const key in payload) {
        if (key == "village") {
            continue;
        }

        if (payload.hasOwnProperty(key)) {
            const value = payload[key];
            if (!value) {
                alert(`Please fill in the ${key.replace('_', ' ')} field.`);
                return false;
            }
        }
    }

    if (payload.state != "Odisha") {
        alert("Only Odisha state registration is allowed");
        return;
    }

    if (payload.password.length < 6) {
        alert("Password should be longer (6+ chars).")
        return
    }

    const experience = parseInt(payload.experience)

    if (Number.isNaN(experience) || experience < 0 || experience > 30) {
        alert("Please provide a valid value for Experience between 0 and 30");
        return;
    }

    if (payload.assist_types.length === 0) {
        alert("Please select at least one Sub-Category.");
        return;
    }

    const validOpt = await validateOtp();

    if (!validOpt) {
        return;
    }

    const response = await fetch(`/api/assist/${category}/register`, { // make this generic as per category
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

    if (data.status == "error") {
        console.log(data.error)
        alert(data.error)
        throw new Error("Failed to register");
    }

    const uuid = data.uuid;
    const formData = new FormData();
    const compressedBlob = await compressImage(file, 50 * 1024);
    formData.append('file', compressedBlob);

    localStorage.setItem("assist_type", category);

    const imageResponse = await fetch(`/api/assist/${category}/${uuid}/image/upload`, { // make this generic as per category
        method: "POST",
        headers: {
            "Authorization": getBearerToken()
        },
        body: formData,
        timeout: 30000
    })

    if (!imageResponse.ok) {
        console.error("Failed to upload photo");
    }

    localStorage.setItem('uuid', data.uuid);
    window.location.assign("/assist/availability");
}

async function getAssistData() {
    const uuid = localStorage.getItem('uuid')
    const category = localStorage.getItem('assist_type')
    if (!uuid) {
        console.log("uuid is null");
        window.location.assign("/");
    }

    const response = await fetch(`/api/assist/${category}/${uuid}`, { // make this generic as per category
        method: "GET",
        headers: {
            "Authorization": getBearerToken()
        }
    })

    if (!response.ok) {
        throw new Error("Failed to register");
    }

    const data = await response.json();
    return data;
}


async function fetchAssistInfo() {
    const data = await getAssistData();
    updateAvailabilitySelect(data.active)
}


function updateAvailabilitySelect(active) {
    const availabilitySelect = $('#availability');
    if (active) {
        availabilitySelect.val('yes').trigger('change');
    } else {
        availabilitySelect.val('no').trigger('change');
    }
}


async function updateAvailabilityInfo() {
    const uuid = localStorage.getItem('uuid')
    const category = localStorage.getItem('assist_type')
    const availability = document.getElementById("availability").value === "yes";
    const response = await fetch(`/api/assist/${category}/${uuid}/availability`, {
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

    showPopupNotification('Your update was successful!');
}



async function assistLogin() {
    const mobile = document.getElementById("mobile").value;
    const password = document.getElementById("password").value;

    if (!password || !mobile) {
        console.log("Please enter a valid moblie/password");
        return;
    }

    var payload = {
        phone_number: mobile,
        password: password,
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
            throw new Error("Login failed");
        }
        const data = await response.json();

        if (data.status === "SUCCESS" && data.token) {
            localStorage.setItem("token", data.token);
            localStorage.setItem("uuid", data.uuid);
            localStorage.setItem("assist_type", data.type);
            window.location.assign("/assist/availability");
        } else {
            console.error("Error login to server")
            alert(data.error);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Login failed. Please check your phone number and password and try again.");
    }
}


async function searchAssist() {

    var payload = {
        state: document.getElementById("state").value,
        district: document.getElementById("district").value,
        block: document.getElementById("block").value,
        village: document.getElementById("village").value,
        assist_types: getSelectedAssistTypes()
    }

    if (payload.assist_types.length === 0) {
        alert("Please select at least one Sub-Category.");
        return;
    }

    if (payload.state == "") {
        alert("Please select a state from the dropdown menu.");
        return;
    }

    const container = document.getElementById('assistContainer');
    container.innerHTML = '';
    container.appendChild(addLoadingIcon());

    try {
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
        if (data.assist && data.assist.length > 0) {
            data.assist.forEach(item => {
                const assistCard = document.createElement('div');
                assistCard.classList.add('col-lg-4', 'col-md-6', 'd-flex', 'align-items-stretch');
                assistCard.setAttribute('data-aos', 'fade-up');
                assistCard.setAttribute('data-aos-delay', '100');
                const imageData = item.image;
                const gender = item.gender ? item.gender : 'male';

                assistCard.innerHTML = `
                    <div class="assist-card">
                        <div class="image-container">
                            <img src="${generateBase64String(imageData, gender)}" alt="${item.name}">
                        </div>
                        <h3>${item.name}</h3>
                        <p>${item.description}</p>
                        <button class="btn btn-request" data-uuid="${item.id}" id="${item.id}_btn" onclick="openPopup(event)">Request Mobile Number</button>
                        <p id="${item.id}"></p>
                    </div>
                `;

                container.appendChild(assistCard);
            });
        } else {
            const noAssistAvailable = document.createElement('div');
            noAssistAvailable.textContent = 'No assist available';
            container.appendChild(noAssistAvailable);
        }
    } catch (error) {
        console.error(error);
    } finally {
        hideLoadingSpinner();
    }
}




function getSelectedAssistTypes() {
    var selectedAssistTypes = [];
    // Get all checkbox elements with name 'assist_type'
    var checkboxes = document.querySelectorAll('input[name="assist_type"]');
    // Iterate over each checkbox
    checkboxes.forEach(function (checkbox) {
        // Check if checkbox is checked
        if (checkbox.checked) {
            // Add the value of the checkbox to the selectedAssistTypes array
            selectedAssistTypes.push(checkbox.value);
        }
    });
    return selectedAssistTypes;
}

function generateBase64String(buffer, gender) {

    // image is not present
    if (!buffer) {
        return "/assets/img/" + gender + ".png"
    }

    const base64String = buffer.slice(buffer.indexOf(',') + 1); // Remove the data URI prefix
    const imageData = 'data:image/jpeg;base64,' + base64String;
    return imageData
}

async function compressImage(file, maxSizeInBytes) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = async function (event) {
            const img = new Image();
            img.src = event.target.result;
            img.onload = function () {
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);
                canvas.toBlob((blob) => {
                    resolve(blob);
                }, 'image/jpeg', 0.7); // Adjust quality here (0.7 means 70% quality)
            };
            img.onerror = function (error) {
                reject(error);
            };
        };
        reader.onerror = function (error) {
            reject(error);
        };
        reader.readAsDataURL(file);
    });
}

async function getAssistDetails(assistId) {
    const m_vid = localStorage.getItem('m_vid');

    var payload = {
        assist_id: assistId,
        vid: m_vid
    };

    const response = await fetch("/member/assist/detail", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": getMemberBearerToken()
        },
        body: JSON.stringify(payload),
    })

    if (!response.ok) {
        throw new Error("Unable to fetch Details for an Assist");
    }

    const data = await response.json();

    return data;
}

function viewAssistPhoneNumber(data, assist_id) {
    const phone_number = data.phone_number;
    document.getElementById(assist_id).innerText = phone_number;
    document.getElementById(assist_id + "_btn").disabled = true;
}

async function requestMobileNumber(event) {
    const assist_uuid = event.target.dataset.uuid;
    await generateAnonymousToken();
    const data = await getAssistDetails(assist_uuid);
    viewAssistPhoneNumber(data, assist_uuid)
    closePopup();
}

function tokenIsPresntAndValid() {
    const m_expiry = localStorage.getItem('m_expiry');
    const expiryTime = new Date(m_expiry);
    const currentTime = new Date();
    return currentTime < expiryTime;
}

async function openPopup(event) {
    const uuid = event.target.dataset.uuid;
    if (tokenIsPresntAndValid()) {
        const data = await getAssistDetails(uuid);
        viewAssistPhoneNumber(data, uuid)
        return;
    }
    const submitButton = document.getElementById('popUpSubmitButton');
    submitButton.setAttribute('data-uuid', uuid);
    document.getElementById("popup").style.display = "block";
}

function closePopup() {
    document.getElementById('otpButton').textContent = 'Generate OTP';
    document.getElementById('otpButton').disabled = false;
    document.getElementById('timer').textContent = '';
    document.getElementById('otpDisplay').innerText = "";
    document.getElementById("mobile").value = "";
    document.getElementById("otp").value = "";
    document.getElementById("popup").style.display = "none";
}


function addLoadingIcon() {
    const loadingSpinner = document.createElement('div');
    loadingSpinner.id = 'loading-spinner';
    loadingSpinner.classList.add('spinner-border', 'text-primary');
    loadingSpinner.setAttribute('role', 'status');
    loadingSpinner.innerHTML = '<span class="sr-only">Loading...</span>';
    return loadingSpinner;
}

function hideLoadingSpinner() {
    // Hide the loading spinner
    document.getElementById('loading-spinner').style.display = 'none';
}

const subcategories = {
    agriculture_assist: [
        { id: 'agriculture', value: 'agriculture', label: 'Farming' },
        { id: 'farm_land_maintenance', value: 'farm_land_maintenance', label: 'Farm Land Maintenance' },
        { id: 'wood_cutting', value: 'wood_cutting', label: 'Wood Cutting' },
        { id: 'tractor_power_tiller', value: 'tractor_power_tiller', label: 'Tractor/Power Tiller rent' },
        { id: 'animal_selling', value: 'animal_selling', label: 'Animal Selling' }
    ],
    construction_assist: [
        { id: 'home_construction', value: 'home_construction', label: 'Home Construction' },
        { id: 'home_repair', value: 'home_repair', label: 'Home Repair' },
        { id: 'carpenter', value: 'carpenter', label: 'Carpenter' },
        { id: 'plumber', value: 'plumber', label: 'Plumber' },
        { id: 'painting', value: 'painting', label: 'Painting' },
        { id: 'fitting', value: 'fitting', label: 'Fitting' }
    ]
};

function updateSubcategories(category) {
    const container = document.getElementById('subcategory-container');
    container.innerHTML = ''; // Clear existing subcategories
    subcategories[category].forEach(subcategory => {
        const checkboxItem = document.createElement('div');
        checkboxItem.className = 'checkbox-item';

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.id = subcategory.id;
        checkbox.name = 'assist_type';
        checkbox.value = subcategory.value;
        checkbox.checked = true;

        const label = document.createElement('label');
        label.htmlFor = subcategory.id;
        label.textContent = subcategory.label;

        checkboxItem.appendChild(checkbox);
        checkboxItem.appendChild(label);
        container.appendChild(checkboxItem);
    });
}
