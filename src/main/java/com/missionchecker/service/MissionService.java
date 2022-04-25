package com.missionchecker.service;

import com.missionchecker.domain.Member;
import com.missionchecker.domain.Mission;
import com.missionchecker.repository.MissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    public List<Mission> findByAllMissionsCreatedBy(Member creator) {
         return missionRepository.findAllByCreatorAndIsDeleted(creator, Boolean.FALSE);
    }
}
