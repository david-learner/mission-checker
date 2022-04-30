function applyMission(clickedElement) {
    const missionId = clickedElement.dataset.missionId;
    const applyingMissionRequestUrl = "/missions/" + missionId +"/apply";

    fetch(applyingMissionRequestUrl, {
        method: 'post',
    }).then((response) => {
        if (response.ok) {
            alert("미션 신청이 완료되었습니다.");
            location.reload();
            // todo button 비활성화
        }
    }).catch((error) => {
        alert(error);
    });
}

function acceptApplicant(clickedElement) {
    const dataHolder = clickedElement.parentElement;
    const missionId =  dataHolder.dataset.missionId;
    const applicantId = dataHolder.dataset.applicantId;
    const acceptApplyingRequestUrl = "/missions/" + missionId + "/applicants/" + applicantId + "/accept";

    fetch(acceptApplyingRequestUrl, {
        method: 'post',
    }).then((response) => {
        if (response.ok) {
            alert("참여신청을 수락했습니다.");
            location.reload();
        }
    }).catch((error) => {
        alert(error);
    });
}