package com.missionchecker.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CheckCreationRequest {

    private LocalDate missionExecutionDate;

    public CheckCreationRequest(String missionExecutionDate) {
        this.missionExecutionDate = LocalDate.parse(missionExecutionDate);
    }
}
