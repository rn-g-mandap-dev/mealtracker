package com.example.meal2.mealitem;

import com.example.meal2.aftermealnote.dto.AfterMealNoteInfoDTO;
import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.dto.MealItemCreationDTO;
import com.example.meal2.mealitem.dto.MealItemDetailedDTO;
import com.example.meal2.mealitem.dto.MealItemUpdateDTO;
import com.example.meal2.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealItemServiceImpl implements MealItemService{

    private MealItemRepository mealItemRepository;

    @Autowired
    public void setMealItemRepository(MealItemRepository mealItemRepository){
        this.mealItemRepository = mealItemRepository;
    }

    @Value("${limits.mealitemsperuser}")
    private Integer maxMealItems;

    // todo research if refactor into builder pattern needed
    @Override
    public List<MealItemDetailedDTO> getAllMealItems(
            User user,
            String search,
            Integer page,
            Integer size,
            MealItem.MealSize mealSize,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime) {

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
                Sort.by("meal_date").descending().and(Sort.by("meal_time").descending()));

        return mealItemRepository.getAllMealItems(user.getId(), search, mealSize, startDate, endDate, startTime, endTime, pageable)
                .stream()
                .map(this::convertMealItem2DetailedDTO)
                .toList();

    }

    @Override
    public Page<MealItemDetailedDTO> getAllMealItemsPage(
            User user,
            String search,
            Integer page,
            Integer size,
            MealItem.MealSize mealSize,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime) {

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
                size);
        //        Sort.by("meal_date").descending().and(Sort.by("meal_time").descending()));

        Long count = mealItemRepository.getAllMealItemsCount(user.getId(), search, mealSize, startDate, endDate, startTime, endTime);
        return new PageImpl<>(mealItemRepository.getAllMealItems(user.getId(), search, mealSize, startDate, endDate, startTime, endTime, pageable)
                .stream()
                .map(this::convertMealItem2DetailedDTO)
                .toList(), pageable, count);

    }

    @Override
    public Long createMealItem(User user, @Valid MealItemCreationDTO mealItemCreationDTO) {
        int userMealItems = mealItemRepository.countUserMealItems(user.getId());
        if(userMealItems >= maxMealItems){
            throw new ResourceLimitException(String.format("max %d mealitems reached", maxMealItems));
        }

        MealItem mi = new MealItem();
        mi.setUserId(user.getId());
        mi.setMeal(mealItemCreationDTO.meal());
        mi.setDate(mealItemCreationDTO.date());
        mi.setTime(mealItemCreationDTO.time());
        mi.setMealSize(mealItemCreationDTO.mealSize());
        mi.setNote(mealItemCreationDTO.note());

        return mealItemRepository.save(mi).getId();
    }

    @Override
    public void saveMealItem(MealItem mealItem) {
        mealItemRepository.save(mealItem);
    }

    @Override
    public void updateMealItem(User user, Long mealItemId, @Valid MealItemUpdateDTO mealItemUpdateDTO) {
        Optional<MealItem> mi = getMealItemById(mealItemId);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                mi.get().setMeal(mealItemUpdateDTO.meal());
                mi.get().setDate(mealItemUpdateDTO.date());
                mi.get().setTime(mealItemUpdateDTO.time());
                mi.get().setMealSize(mealItemUpdateDTO.mealSize());
                mi.get().setNote(mealItemUpdateDTO.note());
                saveMealItem(mi.get());
                return;
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("mealitem id not found: " + mealItemId);
    }

    @Override
    public Optional<MealItem> getMealItemById(Long id) {
        return mealItemRepository.findById(id);
    }

    private MealItemDetailedDTO convertMealItem2DetailedDTO(MealItem mi){
        List<AfterMealNoteInfoDTO> amniDTO = mi.getAfterMealNotes()
                .stream()
                .map(amn -> new AfterMealNoteInfoDTO(
                        amn.getId(),
                        amn.getDate(),
                        amn.getTime(),
                        amn.getNote()
                ))
                .toList();
        return new MealItemDetailedDTO(
                mi.getId(),
                mi.getUserId(),
                mi.getMeal(),
                mi.getDate(),
                mi.getTime(),
                mi.getMealSize(),
                mi.getNote(),
                amniDTO
        );
    }

    @Override
    public MealItemDetailedDTO getMealItem(User user, Long id) {
        Optional<MealItem> mi = getMealItemById(id);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                return convertMealItem2DetailedDTO(mi.get());
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Override
    public void deleteMealItemById(User user, Long id) {
        Optional<MealItem> mi = mealItemRepository.findById(id);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                mealItemRepository.deleteById(id);
                return;
            }
            throw new NotResourceOwnerException("does not own this resource");
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Override
    public boolean existsById(Long id) {
        return mealItemRepository.existsById(id);
    }


}
