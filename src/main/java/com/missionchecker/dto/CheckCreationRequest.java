package com.missionchecker.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckCreationRequest {

    private String missionExecutionDate;

    public LocalDate toMissionExecutionDate() {
        return LocalDate.parse(missionExecutionDate);
    }
}
