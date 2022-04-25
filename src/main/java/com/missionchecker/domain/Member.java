package com.missionchecker.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    @OneToMany(mappedBy = "member")
    private Set<Administration> administrations;
    @OneToMany(mappedBy = "member")
    private Set<Participation> participations;

    protected Member() {
    }

    public Member(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.participations = new HashSet<>();
        this.administrations = new HashSet<>();
        this.password = password;
    }

    public int getNumberOfMissionsAsParticipant() {
        return participations.size();
    }

    public int getNumberOfMissionsAsAdministrator() {
        return administrations.size();
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public void addParticipation(Participation participation) {
        participations.add(participation);
    }

    public void addAdministration(Administration administration) {
        administrations.add(administration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(name, member.name) && Objects.equals(email, member.email) && Objects.equals(phone, member.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, phone);
    }
}
