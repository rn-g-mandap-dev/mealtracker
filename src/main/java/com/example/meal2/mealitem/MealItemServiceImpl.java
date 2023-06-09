package com.example.meal2.mealitem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MealItemServiceImpl implements MealItemService{

    private MealItemRepository mealItemRepository;

    @Autowired
    public void setMealItemRepository(MealItemRepository mealItemRepository){
        this.mealItemRepository = mealItemRepository;
    }

    @Override
    public List<MealItem> getAllMealItems(
            Integer userId,
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
        return mealItemRepository.getAllMealItems(userId, search, mealSize, startDate, endDate, startTime, endTime, pageable);
    }

    @Override
    public void saveMealItem(MealItem mealItem) {
        mealItemRepository.save(mealItem);
    }

    @Override
    public void updateMealItem(MealItem mealItem) {
        mealItemRepository.save(mealItem);
    }

    @Override
    public Optional<MealItem> getMealItemById(Long id) {
        return mealItemRepository.findById(id);
    }

    @Override
    public void deleteMealItemById(@AuthenticationPrincipal User user, Long id) {
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
