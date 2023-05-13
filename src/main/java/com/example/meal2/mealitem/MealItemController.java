package com.example.meal2.mealitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class MealItemController {

    private MealItemService mealItemService;

    @Autowired
    public void setMealItemService(MealItemService mealItemService){
        this.mealItemService = mealItemService;
    }

    @GetMapping("/meals")
    public List<MealItem> getAllMealItems(){
        return mealItemService.getAllMealItems();
    }

    @GetMapping("/meals/{id}")
    public MealItem getMealItem(@PathVariable("id") Long id){
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        return mi.orElse(null);
    }

    @PostMapping("/meals")
    public void addMealItem(@RequestBody MealItem mealItem){
        mealItemService.saveMealItem(mealItem);
    }

    @PutMapping("/meals/{id}")
    public void updateMealItem(
            @PathVariable("id") Long id,
            @RequestBody MealItem mealItem){
        mealItem.setId(id);
        mealItemService.saveMealItem(mealItem);
    }

    @DeleteMapping("/meals/{id}")
    public void deleteMealItem(@PathVariable("id") Long id){
        mealItemService.deleteMealItemById(id);
    }

}
