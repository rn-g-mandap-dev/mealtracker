package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteCreationDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteDetailedDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteUpdateDTO;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AfterMealNoteService {
    Long createAfterMealNote(User user, AfterMealNoteCreationDTO afterMealNoteCreationDTO);
    AfterMealNoteDetailedDTO getAfterMealNote(User user, Long afterMealNoteId);
    List<AfterMealNoteDetailedDTO> getAfterMealNotes(
            User user,
            String search,
            Integer page,
            Integer size,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    );
    void updateAfterMealNote(User user, Long afterMealNoteId, AfterMealNoteUpdateDTO afterMealNoteUpdateDTO);
    void deleteAfterMealNote(User user, Long afterMealNoteId);
}
