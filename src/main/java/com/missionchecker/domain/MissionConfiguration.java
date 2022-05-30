package com.missionchecker.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class MissionConfiguration {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime checkStartTime;
    private LocalTime checkEndTime;
    private Boolean canCreateCheckWithPastDate;

    public MissionConfiguration() {
    }

    public MissionConfiguration(LocalDate startDate, LocalDate endDate, LocalTime checkStartTime,
                                LocalTime checkEndTime, Boolean canCreateCheckWithPastDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.checkStartTime = checkStartTime;
        this.checkEndTime = checkEndTime;
        this.canCreateCheckWithPastDate = canCreateCheckWithPastDate;
    }

    public boolean canCreateCheckWithPastDate() {
        return true;
    }

    /**
     * 미션 수행 체크 일시가 미션에서 지정한 일자와 시간에 유효한지 확인한다
     *
     * @param executionDatetime
     */
    public void validateExecutionDatetime(LocalDateTime executionDatetime) {
        if (!isValidDate(executionDatetime.toLocalDate())) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다.");
        }
        if (!canCreateCheckWithPastDate && !isValidTime(executionDatetime.toLocalTime())) {
            throw new IllegalArgumentException("유효하지 않은 시간입니다.");
        }
    }

    private boolean isValidDate(LocalDate date) {
        if (date.isEqual(startDate) || date.isEqual(endDate)) {
            return true;
        }
        if (date.isAfter(startDate) && date.isBefore(endDate)) {
            return true;
        }
        return false;
    }

    private boolean isValidTime(LocalTime time) {
        if (time.equals(checkStartTime) || time.equals(checkEndTime)) {
            return true;
        }
        if (time.isAfter(checkStartTime) && time.isBefore(checkEndTime)) {
            return true;
        }
        return false;
    }
}
