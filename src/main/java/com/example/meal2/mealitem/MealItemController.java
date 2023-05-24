package com.example.meal2.mealitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
            @RequestParam Optional<Integer> s,
            @RequestParam Optional<MealItem.MealSize> m,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> sd,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ed,
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> st,
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> et
    ){
        String search = q.orElse("");
        Integer page = p.orElse(0);
        Integer size = s.orElse(0);
        MealItem.MealSize type = m.orElse(null);
        LocalDate startDate = sd.orElse(null);
        LocalDate endDate = ed.orElse(null);
        LocalTime startTime = st.orElse(null);
        LocalTime endTime = et.orElse(null);

        return new ResponseEntity<>(mealItemService.getAllMealItems(search, page, size, type, startDate, endDate, startTime, endTime), HttpStatus.OK);
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
