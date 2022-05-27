function logout() {
    const logoutUrl = "/logout";

    fetch(logoutUrl, {
        method: 'post',
    }).then((response) => {
        if (response.redirected) {
            window.location = response.url;
        }
    }).catch((error) => {
    });
}

// tooltip enabled
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

// mission
function initMissionRegistrationForm() {
    document.querySelector("#can-create-check-with-past-date")
        .addEventListener("change", e => toggleMissionCheckStartAndEndTime(e));
}

function toggleMissionCheckStartAndEndTime(e) {
    let missionCheckStartTime = document.querySelector("#mission-check-start-time");
    let missionCheckEndTime = document.querySelector("#mission-check-end-time");

    if (e.target.checked) {
        missionCheckStartTime.disabled = true;
        missionCheckEndTime.disabled = true;
    } else {
        missionCheckStartTime.disabled = false;
        missionCheckEndTime.disabled = false;
    }
}