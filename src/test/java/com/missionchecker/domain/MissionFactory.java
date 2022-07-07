package com.missionchecker.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public class MissionFactory {

    /**
     * 기본 설정 값을 가지는 미션을 생성한다 * 시작일: 오늘 * 종료일: 오늘로부터 30일 후 * 미션 수행 표시 가능 시간: 9:30 ~ 17:30
     */
    public static Mission createDefaultMission(Member creator) {
        int THIRTY_DAYS = 30;
        String missionName = "Speaking sentences as English";
        LocalDate missionStartDate = LocalDate.now();
        LocalDate missionEndDate = LocalDate.now().plus(Period.ofDays(THIRTY_DAYS));
        LocalTime checkableStartTime = LocalTime.of(9, 30);
        LocalTime checkableEndTime = LocalTime.of(17, 30);
        boolean checkableToPastDate = true;

        return createMission(creator, missionName, missionStartDate, missionEndDate,
                checkableStartTime, checkableEndTime, checkableToPastDate);
    }

    /**
     * 미션을 생성한다.
     *
     * @param creator             미션 생성자
     * @param name                미션 이름
     * @param startDate           미션 시작일
     * @param endDate             미션 종료일
     * @param checkableStartTime  미션 수행 완료 표시 가능 시작 시간
     * @param checkableEndTime    미션 수행 완료 표시 가능 종료 시간
     * @param checkableToPastDate 과거 날짜 미션 수행 완료 표시 가능 여부
     * @return
     */
    public static Mission createMission(
            Member creator,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime checkableStartTime,
            LocalTime checkableEndTime,
            boolean checkableToPastDate) {
        MissionConfiguration configuration = new MissionConfiguration(
                startDate,
                endDate,
                checkableStartTime,
                checkableEndTime,
                checkableToPastDate
        );

        return Mission.of(creator, name, configuration);
    }
}
