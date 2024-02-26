async function validateToken() {
    const token = localStorage.getItem('token'); 

    const response = await fetch('/api/auth/validate', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    
    if (!response.ok) {
        console.error('Failed to validate token');
        window.location.assign("/");
    }
}