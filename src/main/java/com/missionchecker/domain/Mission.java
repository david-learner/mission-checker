package com.missionchecker.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_MISSION")
@Getter
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "missions")
    private List<Member> members;
    @OneToMany(mappedBy = "mission")
    private List<Check> checks;
}
