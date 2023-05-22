package com.example.meal2.mealitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class MealItemServiceImpl implements MealItemService{

    private MealItemRepository mealItemRepository;

    @Autowired
    public void setMealItemRepository(MealItemRepository mealItemRepository){
        this.mealItemRepository = mealItemRepository;
    }

    @Override
    public List<MealItem> getAllMealItems(String search, Pageable pageable) {
        return mealItemRepository.getAllMealItems(search, pageable);
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
    public void deleteMealItemById(Long id) {
        mealItemRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return mealItemRepository.existsById(id);
    }


}
