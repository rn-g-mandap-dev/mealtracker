package com.example.meal2.thoughtrecord.service;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.thoughtrecord.dto.*;
import com.example.meal2.thoughtrecord.entity.Mood;
import com.example.meal2.thoughtrecord.entity.MoodType;
import com.example.meal2.thoughtrecord.entity.Thought;
import com.example.meal2.thoughtrecord.entity.ThoughtRecord;
import com.example.meal2.thoughtrecord.repository.ThoughtRecordRepository;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import com.example.meal2.util.conversion.ConversionMapper;
import io.jsonwebtoken.lang.Assert;
import jakarta.validation.*;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThoughtRecordServiceImplTest {

    @Mock
    private ThoughtRecordRepository thoughtRecordRepository;

    @Mock
    private ConversionMapper conversionMapper;


    @Mock
    private Validator validator;

    private Validator realValidator;

    @InjectMocks
    private ThoughtRecordServiceImpl thoughtRecordService;

    private User user;

    private ThoughtRecord thoughtRecord;
    private ThoughtRecord thoughtRecord2;

    private ThoughtRecordCreationDTO thoughtRecordCreationDTO;
    private ThoughtRecordUpdateDTO thoughtRecordUpdateDTO;
    private ThoughtRecordDTO thoughtRecordDTO;
    private ThoughtRecordDTO thoughtRecordDTO2;

    @BeforeEach
    public void setUp(){

        user = new User();
        user.setId(9999);
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(Role.USER);

        thoughtRecord = new ThoughtRecord();
        thoughtRecord.setId(1L);
        thoughtRecord.setUserId(9999);
        thoughtRecord.setDate(LocalDate.parse("2023-06-09"));
        thoughtRecord.setTime(LocalTime.parse("14:35:00"));
        thoughtRecord.setSituation("example situation");
        thoughtRecord.setMoods(List.of(new Mood(1L, MoodType.EAGER, 99)));
        thoughtRecord.setThoughts(List.of(new Thought(1L, "thought example", 99)));

        thoughtRecord2 = new ThoughtRecord();
        thoughtRecord2.setId(2L);
        thoughtRecord2.setUserId(9999);
        thoughtRecord2.setDate(LocalDate.parse("2023-06-09"));
        thoughtRecord2.setTime(LocalTime.parse("14:35:00"));
        thoughtRecord2.setSituation("example situation");
        thoughtRecord2.setMoods(List.of(new Mood(2L, MoodType.EAGER, 99)));
        thoughtRecord2.setThoughts(List.of(new Thought(2L, "thought example", 99)));

        thoughtRecordCreationDTO = new ThoughtRecordCreationDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodCreationDTO(MoodType.EAGER, 99)),
                List.of(new ThoughtCreationDTO("thought example", 99))
        );

        thoughtRecordUpdateDTO = new ThoughtRecordUpdateDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodUpdateDTO(1L, MoodType.EAGER, 99)),
                List.of(new ThoughtUpdateDTO(1L, "thought example", 99))
        );

        thoughtRecordDTO = new ThoughtRecordDTO(
                thoughtRecord.getId(),
                thoughtRecord.getUserId(),
                thoughtRecord.getDate(),
                thoughtRecord.getTime(),
                thoughtRecord.getSituation(),
                List.of(new MoodDTO(1L, MoodType.EAGER, 99)),
                List.of(new ThoughtDTO(1L, "thought example", 99))
        );

        thoughtRecordDTO2 = new ThoughtRecordDTO(
                thoughtRecord2.getId(),
                thoughtRecord2.getUserId(),
                thoughtRecord2.getDate(),
                thoughtRecord2.getTime(),
                thoughtRecord2.getSituation(),
                List.of(new MoodDTO(2L, MoodType.EAGER, 99)),
                List.of(new ThoughtDTO(2L, "thought example", 99))
        );
    }

    @DisplayName("createThoughtRecord: normal")
    @Test
    void createThoughtRecord() {
        when(validator.validate(thoughtRecordCreationDTO)).thenReturn(Collections.emptySet());
        when(conversionMapper.convertThoughtRecord2DTO(any(ThoughtRecord.class))).thenReturn(thoughtRecordDTO);
        when(thoughtRecordRepository.save(any(ThoughtRecord.class))).thenReturn(thoughtRecord);

        ThoughtRecordDTO trDTO = thoughtRecordService.createThoughtRecord(user, thoughtRecordCreationDTO);

        Assert.notNull(trDTO);
        Assert.isInstanceOf(ThoughtRecordDTO.class, trDTO);
        verify(thoughtRecordRepository, times(1)).save(any(ThoughtRecord.class));
    }
    @DisplayName("createThoughtRecord: constraint violation")
    @Test
    void createThoughtRecord1() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        realValidator = factory.getValidator();

        thoughtRecordCreationDTO = new ThoughtRecordCreationDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                null,
                List.of(new MoodCreationDTO(MoodType.EAGER, 99)),
                List.of(new ThoughtCreationDTO("thought example", 99))
        );
        Set<ConstraintViolation<ThoughtRecordCreationDTO>> violations = realValidator.validate(thoughtRecordCreationDTO);

        assertEquals(1, violations.size());
    }
    @DisplayName("createThoughtRecord: multiple same moodType")
    @Test
    void createThoughtRecord2() {
        thoughtRecordCreationDTO = new ThoughtRecordCreationDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodCreationDTO(MoodType.EAGER, 99), new MoodCreationDTO(MoodType.EAGER, 98)),
                List.of(new ThoughtCreationDTO("thought example", 99))
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> thoughtRecordService.createThoughtRecord(user, thoughtRecordCreationDTO)
        );
        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }

    @DisplayName("getThoughtRecord: normal")
    @Test
    void getThoughtRecord() {
        when(conversionMapper.convertThoughtRecord2DTO(thoughtRecord)).thenReturn(thoughtRecordDTO);
        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        ThoughtRecordDTO trDTO = thoughtRecordService.getThoughtRecord(user, 1L);

        Assert.notNull(trDTO);
        Assert.isInstanceOf(ThoughtRecordDTO.class, trDTO);
    }
    @DisplayName("getThoughtRecord: thoughtrecord id not found")
    @Test
    void getThoughtRecord1() {

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> thoughtRecordService.getThoughtRecord(user, 1L)
        );
    }
    @DisplayName("getThoughtRecord: does not own this resource")
    @Test
    void getThoughtRecord2() {
        thoughtRecord.setUserId(9998);

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> thoughtRecordService.getThoughtRecord(user, 1L)
        );
    }

    @DisplayName("getThoughtRecords: normal")
    @Test
    void getThoughtRecords() {
        when(thoughtRecordRepository.getAllThoughtRecords(
                eq(9999),
                eq(""),
                eq(LocalDate.parse("0000-01-01")),
                eq(LocalDate.parse("9998-12-31")),
                eq(LocalTime.parse("00:00:00")),
                eq(LocalTime.parse("23:59:59")),
                any(Pageable.class)
        )).thenReturn(List.of(thoughtRecord, thoughtRecord2));

        List<ThoughtRecordDTO> trDTOList = thoughtRecordService.getThoughtRecords(
          user, "", 0, 32, null, null, null, null
        );

        Assert.notNull(trDTOList);
    }

    @DisplayName("updateThoughtRecord: normal")
    @Test
    void updateThoughtRecord() {
        when(validator.validate(thoughtRecordUpdateDTO)).thenReturn(Collections.emptySet());
        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));
        when(conversionMapper.convertThoughtRecord2DTO(any(ThoughtRecord.class))).thenReturn(thoughtRecordDTO);
        when(thoughtRecordRepository.save(any(ThoughtRecord.class))).thenReturn(thoughtRecord);

        ThoughtRecordDTO trDTO = thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO);

        Assert.notNull(trDTO);
        Assert.isInstanceOf(ThoughtRecordDTO.class, trDTO);
        verify(thoughtRecordRepository, times(1)).save(any(ThoughtRecord.class));

    }
    @DisplayName("updateThoughtRecord: constraint violation")
    @Test
    void updateThoughtRecord1() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        realValidator = factory.getValidator();

        thoughtRecordUpdateDTO = new ThoughtRecordUpdateDTO(
                LocalDate.parse("2023-06-09"),
                null,
                null,
                List.of(new MoodUpdateDTO(1L, MoodType.EAGER, 99), new MoodUpdateDTO(2L, MoodType.EAGER, 98)),
                List.of(new ThoughtUpdateDTO(1L, "thought example", 99))
        );

        Set<ConstraintViolation<ThoughtRecordUpdateDTO>> violations = realValidator.validate(thoughtRecordUpdateDTO);

        assertEquals(2, violations.size());
    }
    @DisplayName("updateThoughtRecord: multiple same moodType")
    @Test
    void updateThoughtRecord2() {

        thoughtRecordUpdateDTO = new ThoughtRecordUpdateDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodUpdateDTO(1L, MoodType.EAGER, 99), new MoodUpdateDTO(2L, MoodType.EAGER, 98)),
                List.of(new ThoughtUpdateDTO(1L, "thought example", 99))
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO)
        );
        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }
    @DisplayName("updateThoughtRecord: using mood of other thoughtrecords")
    @Test
    void updateThoughtRecord3a() {
        // 2nd element of MoodUpdateDTO list has id of 2 which is
        // not in orig thoughtrecord therefore it doesn't own said element
        thoughtRecordUpdateDTO = new ThoughtRecordUpdateDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodUpdateDTO(1L, MoodType.EAGER, 99), new MoodUpdateDTO(2L, MoodType.CONTENT, 98)),
                List.of(new ThoughtUpdateDTO(1L, "thought example", 99))
        );

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO)
        );
        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }
    @DisplayName("updateThoughtRecord: using thought of other thoughtrecords")
    @Test
    void updateThoughtRecord3b() {
        // 2nd element of ThoughtUpdateDTO list has id of 2 which is
        // not in orig thoughtrecord therefore it doesn't own said element
        thoughtRecordUpdateDTO = new ThoughtRecordUpdateDTO(
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                "example situation",
                List.of(new MoodUpdateDTO(1L, MoodType.EAGER, 99)),
                List.of(new ThoughtUpdateDTO(1L, "thought example", 99), new ThoughtUpdateDTO(2L, "thought example", 99))
        );

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO)
        );
        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }
    @DisplayName("updateThoughtRecord: thoughtrecord id not found")
    @Test
    void updateThoughtRecord4() {
        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO)
        );

        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }
    @DisplayName("updateThoughtRecord: does not own this resource")
    @Test
    void updateThoughtRecord5() {
        thoughtRecord.setUserId(9998);

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> thoughtRecordService.updateThoughtRecord(user, 1L, thoughtRecordUpdateDTO)
        );

        verify(thoughtRecordRepository, never()).save(any(ThoughtRecord.class));
    }

    @DisplayName("deleteThoughtRecord: normal")
    @Test
    void deleteThoughtRecord() {
        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        thoughtRecordService.deleteThoughtRecord(user, 1L);

        verify(thoughtRecordRepository, times(1)).deleteById(any());
    }
    @DisplayName("deleteThoughtRecord: does not own this resource")
    @Test
    void deleteThoughtRecord1() {
        thoughtRecord.setUserId(9998);

        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.of(thoughtRecord));

        Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> thoughtRecordService.deleteThoughtRecord(user, 1L)
        );

        verify(thoughtRecordRepository, never()).deleteById(any());
    }
    @DisplayName("deleteThoughtRecord: thoughtrecord id not found")
    @Test
    void deleteThoughtRecord2() {
        when(thoughtRecordRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> thoughtRecordService.deleteThoughtRecord(user, 1L)
        );

        verify(thoughtRecordRepository, never()).deleteById(any());
    }
}