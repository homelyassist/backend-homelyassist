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

async function getInfoForPinCode(pincode) {
    const response = await fetch(`https://api.postalpincode.in/pincode/${pincode}`);

    if (!response.ok) {
        throw new Error("Unable to fetch details with pincode: " + pincode);
    }

    const data = await response.json();

    if (data && data.length > 0 && data[0].PostOffice && data[0].PostOffice.length > 0) {
        const firstPostOffice = data[0].PostOffice[0];
        const country = firstPostOffice.Country;
        const pincode = firstPostOffice.Pincode;
        const state = firstPostOffice.State;
        const district = firstPostOffice.District;
    
        console.log("Country:", country);
        console.log("Pincode:", pincode);
        console.log("State:", state);
        console.log("District:", district);

        return {
            country: country,
            pincode: pincode,
            state: state,
            district: district
        }
      } else {
        console.error("No data found with pincode: " + pincode);
        throw new Error("Unable to fetch details with pincode: " + pincode);
      }
}
