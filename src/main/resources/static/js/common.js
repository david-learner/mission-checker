function logout() {
    const logoutUrl = "/logout";

    fetch(logoutUrl, {
        method: 'post',
    }).then((response) => {
        if(response.redirected) {
            window.location = response.url;
        }
    }).catch((error) => {
    });
}

// tooltip enabled
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))