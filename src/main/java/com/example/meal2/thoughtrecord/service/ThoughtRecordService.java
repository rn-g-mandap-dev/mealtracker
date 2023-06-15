package com.example.meal2.thoughtrecord.service;

import com.example.meal2.thoughtrecord.dto.ThoughtRecordCreationDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordUpdateDTO;
import com.example.meal2.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ThoughtRecordService {
    Long createThoughtRecord(User user, ThoughtRecordCreationDTO thoughtRecordCreationDTO);
    ThoughtRecordDTO getThoughtRecord(User user, Long thoughtRecordId);
    List<ThoughtRecordDTO> getThoughtRecords(
            User user,
            String search,
            Integer page,
            Integer size,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    );
    void updateThoughtRecord(User user, Long thoughtRecordId, ThoughtRecordUpdateDTO thoughtRecordDTO);
    void deleteThoughtRecord(User user, Long id);
}
