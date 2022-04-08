package com.missionchecker.dto;

import com.missionchecker.domain.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SigningUpRequest {

    public String name;
    public String email;
    public String phone;

    public SigningUpRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member toMember() {
        return new Member(name, email, phone);
    }
}
