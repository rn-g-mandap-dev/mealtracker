package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteCreationDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteDetailedDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteUpdateDTO;
import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.mealitem.MealItemServiceImpl;
import com.example.meal2.mealitem.dto.MealItemInfoDTO;
import com.example.meal2.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AfterMealNoteServiceImpl implements AfterMealNoteService {

    private final AfterMealNoteRepository afterMealNoteRepository;

    private final MealItemServiceImpl mealItemService;

    public AfterMealNoteServiceImpl(AfterMealNoteRepository afterMealNoteRepository, MealItemServiceImpl mealItemService) {
        this.afterMealNoteRepository = afterMealNoteRepository;
        this.mealItemService = mealItemService;
    }

    @Override
    public Long createAfterMealNote(User user, @Valid AfterMealNoteCreationDTO afterMealNoteCreationDTO) {
        long id = afterMealNoteCreationDTO.mealItemId();
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                var amn = new AfterMealNote();
                amn.setMealItemId(afterMealNoteCreationDTO.mealItemId());
                amn.setDate(afterMealNoteCreationDTO.date());
                amn.setTime(afterMealNoteCreationDTO.time());
                amn.setNote(afterMealNoteCreationDTO.note());
                return afterMealNoteRepository.save(amn).getId();
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Override
    public AfterMealNoteDetailedDTO getAfterMealNote(User user, Long afterMealNoteId) {
        Optional<AfterMealNote> amn = afterMealNoteRepository.findById(afterMealNoteId);
        if(amn.isPresent()){
            if(Objects.equals(amn.get().getMealItem().getUserId(), user.getId())){
                return convertAfterMealNote2DetaieldDTO(amn.get());
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException(String.format("aftermealnote id not found: %d", afterMealNoteId));
    }

    @Override
    public List<AfterMealNoteDetailedDTO> getAfterMealNotes(
            User user,
            String search,
            Integer page,
            Integer size,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
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
                Sort.by("note_date").descending().and(Sort.by("note_time").descending()));

        return afterMealNoteRepository.getAllAfterMealNotes(user.getId(), search, startDate, endDate, startTime, endTime, pageable)
                .stream()
                .map(this::convertAfterMealNote2DetaieldDTO)
                .toList();
    }

    private AfterMealNoteDetailedDTO convertAfterMealNote2DetaieldDTO(AfterMealNote amn){
        return new AfterMealNoteDetailedDTO(
                amn.getId(),
                amn.getDate(),
                amn.getTime(),
                amn.getNote(),
                new MealItemInfoDTO(
                        amn.getMealItem().getId(),
                        amn.getMealItem().getUserId(),
                        amn.getMealItem().getMeal(),
                        amn.getMealItem().getDate(),
                        amn.getMealItem().getTime(),
                        amn.getMealItem().getMealSize(),
                        amn.getMealItem().getNote()
                )
        );
    }

    @Override
    public void updateAfterMealNote(User user, Long afterMealNoteId, @Valid AfterMealNoteUpdateDTO afterMealNoteUpdateDTO) {
        Optional<AfterMealNote> amn = afterMealNoteRepository.findById(afterMealNoteId);
        if(amn.isPresent()){
            if(Objects.equals(amn.get().getMealItem().getUserId(), user.getId())){
                var updatedAMN = amn.get();
                updatedAMN.setDate(afterMealNoteUpdateDTO.date());
                updatedAMN.setTime(afterMealNoteUpdateDTO.time());
                updatedAMN.setNote(afterMealNoteUpdateDTO.note());
                afterMealNoteRepository.save(updatedAMN);
                return;
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("aftermealnote id not found: " + afterMealNoteId);
    }

    @Override
    public void deleteAfterMealNote(User user, Long afterMealNoteId) {
        Optional<AfterMealNote> amn = afterMealNoteRepository.findById(afterMealNoteId);
        if(amn.isPresent()){
            if(Objects.equals(amn.get().getMealItem().getUserId(), user.getId())){
                afterMealNoteRepository.deleteById(afterMealNoteId);
                return;
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("aftermealnote id not found: " + afterMealNoteId);
    }
}
