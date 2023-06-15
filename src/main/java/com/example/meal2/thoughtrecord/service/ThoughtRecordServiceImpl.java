package com.example.meal2.thoughtrecord.service;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.thoughtrecord.dto.*;
import com.example.meal2.thoughtrecord.entity.Mood;
import com.example.meal2.thoughtrecord.entity.Thought;
import com.example.meal2.thoughtrecord.entity.ThoughtRecord;
import com.example.meal2.thoughtrecord.repository.ThoughtRecordRepository;
import com.example.meal2.user.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ThoughtRecordServiceImpl implements ThoughtRecordService {

    private final ThoughtRecordRepository thoughtRecordRepository;

    private final Validator validator;

    public ThoughtRecordServiceImpl(ThoughtRecordRepository thoughtRecordRepository, Validator validator) {
        this.thoughtRecordRepository = thoughtRecordRepository;
        this.validator = validator;
    }

    @Override
    public Long createThoughtRecord(User user, ThoughtRecordCreationDTO thoughtRecordCreationDTO) {
        Set<ConstraintViolation<ThoughtRecordCreationDTO>> violations = validator.validate(thoughtRecordCreationDTO);
        if(!violations.isEmpty()) throw new ConstraintViolationException(violations);

        List<Mood> moods = thoughtRecordCreationDTO.moods().stream()
                .map(m -> new Mood(m.mood(), m.level()))
                .toList();
        List<Thought> thoughts = thoughtRecordCreationDTO.thoughts().stream()
                .map(t -> new Thought(t.thought(), t.level()))
                .toList();
        return thoughtRecordRepository.save(new ThoughtRecord(
                user.getId(),
                thoughtRecordCreationDTO.date(),
                thoughtRecordCreationDTO.time(),
                thoughtRecordCreationDTO.situation(),
                moods,
                thoughts
        )).getId();
    }

    @Override
    public ThoughtRecordDTO getThoughtRecord(User user, Long thoughtRecordId) {
        return thoughtRecordRepository.findById(thoughtRecordId).map(
                tr -> {
                    if(Objects.equals(tr.getUserId(), user.getId())){
                        return convertThoughtRecord2DTO(tr);
                    }
                    throw new NotResourceOwnerException("does not own this resource");
                }).orElseThrow(
                    () -> {
                        throw new ResourceNotFoundException(String.format("thoughtrecord id not found: %d", thoughtRecordId));
                });
    }

    @Override
    public List<ThoughtRecordDTO> getThoughtRecords(User user, String search, Integer page, Integer size, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        int DEFAULT_SIZE = 32;
        int MAX_SIZE = 50;
        LocalDate DEFAULT_START_DATE = LocalDate.parse("0000-01-01");
        LocalDate DEFAULT_END_DATE = LocalDate.parse("9998-12-31");
        LocalTime DEFAULT_START_TIME = LocalTime.parse("00:00:00");
        LocalTime DEFAULT_END_TIME = LocalTime.parse("23:59:59");
        if (size <= 0) size = DEFAULT_SIZE;
        if (size > MAX_SIZE) size = MAX_SIZE;
        if (startDate == null) startDate = DEFAULT_START_DATE;
        if (endDate == null) endDate = DEFAULT_END_DATE;
        if (startTime == null) startTime = DEFAULT_START_TIME;
        if (endTime == null) endTime = DEFAULT_END_TIME;
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("tr_date").descending().and(Sort.by("tr_time").descending()));
        return thoughtRecordRepository.getAllThoughtRecords(user.getId(), search, startDate, endDate, startTime, endTime, pageable)
                .stream()
                .map(this::convertThoughtRecord2DTO)
                .toList();
    }


    @Override
    public void updateThoughtRecord(User user, Long thoughtRecordId, ThoughtRecordUpdateDTO thoughtRecordDTO) {
        Set<ConstraintViolation<ThoughtRecordUpdateDTO>> violations = validator.validate(thoughtRecordDTO);
        if(!violations.isEmpty()) throw new ConstraintViolationException(violations);

        thoughtRecordRepository.findById(thoughtRecordId).ifPresentOrElse(
                tr -> {
                    if(Objects.equals(tr.getUserId(), user.getId())){
                        // prevent using mood, thought of other thoughtrecords
                        // by checking if entities are already in thoughtrecord before the update
                        thoughtRecordDTO.moods().forEach(mDTO -> {
                            if(mDTO.id() != null){
                                var wrapper = new Object(){ boolean containsId = false; };
                                tr.getMoods().forEach(m -> {
                                    if(Objects.equals(m.getId(), mDTO.id())) wrapper.containsId = true;
                                });
                                if(!wrapper.containsId){
                                    throw new NotResourceOwnerException("does not own this resource: Mood " + mDTO.id());
                                }
                            }
                        });
                        thoughtRecordDTO.thoughts().forEach(tDTO -> {
                            if(tDTO.id() != null){
                                var wrapper = new Object(){ boolean containsId = false; };
                                tr.getThoughts().forEach(t -> {
                                    if(Objects.equals(t.getId(), tDTO.id())) wrapper.containsId = true;
                                });
                                if(!wrapper.containsId){
                                    throw new NotResourceOwnerException("does not own this resource: Thought " + tDTO.id());
                                }
                            }
                        });

                        tr.setDate(thoughtRecordDTO.date());
                        tr.setTime(thoughtRecordDTO.time());
                        tr.setSituation(thoughtRecordDTO.situation());
                        tr.setMoods(thoughtRecordDTO.moods().stream()
                                .map(m -> new Mood(m.id(), m.mood(), m.level()))
                                .collect(Collectors.toList()));
                        tr.setThoughts(thoughtRecordDTO.thoughts().stream()
                                .map(t -> new Thought(t.id(), t.thought(), t.level()))
                                .collect(Collectors.toList()));
                        thoughtRecordRepository.save(tr);
                        return;
                    }
                    throw new NotResourceOwnerException("does not own this resource");
                },
                () -> {
                    throw new ResourceNotFoundException("thoughtrecord id not found: " + thoughtRecordId);
                }
        );
    }

    private ThoughtRecordDTO convertThoughtRecord2DTO(ThoughtRecord tr){
        return new ThoughtRecordDTO(
                tr.getId(),
                tr.getUserId(),
                tr.getDate(),
                tr.getTime(),
                tr.getSituation(),
                tr.getMoods().stream()
                        .map(m -> new MoodDTO(m.getId(),m.getMood(),m.getLevel()))
                        .toList(),
                tr.getThoughts().stream()
                        .map(t -> new ThoughtDTO(t.getId(),t.getThought(),t.getLevel()))
                        .toList()
        );
    }



    @Override
    public void deleteThoughtRecord(User user, Long id) {
        thoughtRecordRepository.findById(id).ifPresentOrElse(
                tr -> {
                    if(Objects.equals(tr.getUserId(), user.getId())){
                        thoughtRecordRepository.deleteById(id);
                        return;
                    }
                    throw new NotResourceOwnerException("does not own this resource");
                },
                () -> {
                    throw new ResourceNotFoundException("thoughtrecord id not found: " + id);
                }
        );
    }


}
