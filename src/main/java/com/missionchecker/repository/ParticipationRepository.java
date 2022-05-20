package com.missionchecker.repository;

import com.missionchecker.domain.Participation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findParticipationByMemberIdAndMissionId(Long memberId, Long missionId);
}
