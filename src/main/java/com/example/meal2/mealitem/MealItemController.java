package com.example.meal2.mealitem;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<MealItem>> getAllMealItems(
            @RequestParam Optional<String> q,
            @RequestParam Optional<Integer> p,
            @RequestParam Optional<Integer> s
    ){
        Integer DEFAULT_SIZE = 32;
        Integer MAX_SIZE = 50;

        String search = q.orElse("");
        Integer page = p.orElse(0);
        Integer size = s.orElse(DEFAULT_SIZE);
        if (size > MAX_SIZE) size = MAX_SIZE;

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("time").descending()));
        return new ResponseEntity<>(mealItemService.getAllMealItems(search, pageable), HttpStatus.OK);
    }

    @GetMapping("/meals/{id}")
    public ResponseEntity<MealItem> getMealItem(@PathVariable("id") Long id){
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        return new ResponseEntity<>(mi.orElse(null), HttpStatus.OK);
    }

    @PostMapping("/meals")
    public ResponseEntity<?> addMealItem(@RequestBody MealItem mealItem){
        mealItemService.saveMealItem(mealItem);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/meals/{id}")
    public ResponseEntity<?> updateMealItem(
            @PathVariable("id") Long id,
            @RequestBody MealItem mealItem){
        if(mealItemService.existsById(id)){
            mealItem.setId(id);
            mealItemService.saveMealItem(mealItem);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>("id doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/meals/{id}")
    public ResponseEntity<?> deleteMealItem(@PathVariable("id") Long id){
        mealItemService.deleteMealItemById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
