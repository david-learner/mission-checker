package com.missionchecker.repository;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findAllByOwnerAndIsDeleted(Member creator, Boolean isDeleted);
}
