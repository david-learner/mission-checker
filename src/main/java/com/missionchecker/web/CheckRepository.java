package com.missionchecker.web;

import com.missionchecker.domain.Check;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findAllByMissionIdAndCheckerId(Long missionId, Long checkerId);
}
