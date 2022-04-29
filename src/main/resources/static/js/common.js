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