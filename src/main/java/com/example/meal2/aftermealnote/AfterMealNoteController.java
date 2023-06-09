package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteReq;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.MealItem;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AfterMealNoteController {

    private final String PATH_HEADER = "/after-meal-notes";

    private final AfterMealNoteService afterMealNoteService;

    public AfterMealNoteController(AfterMealNoteService afterMealNoteService) {
        this.afterMealNoteService = afterMealNoteService;
    }

    @PostMapping(value=PATH_HEADER, consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> createAfterMealNote(@RequestBody @Valid AfterMealNoteReq afterMealNoteReq){
        AfterMealNote afterMealNote = new AfterMealNote();
        afterMealNote.setMealItemId(afterMealNoteReq.getMealItemId());
        afterMealNote.setDate(afterMealNoteReq.getDate());
        afterMealNote.setTime(afterMealNoteReq.getTime());
        afterMealNote.setNote(afterMealNoteReq.getNote());

        afterMealNoteService.createAfterMealNote(afterMealNote);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<?> getAllAfterMealNotes(){
        return new ResponseEntity<>(
                afterMealNoteService.getAllAfterMealNotes(),
                HttpStatus.OK
        );
    }

    @GetMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<?> getAfterMealNote(@PathVariable("id") Long id){
        return new ResponseEntity<>(
                afterMealNoteService.getAfterMealNote(id).orElseThrow(
                        () -> new ResourceNotFoundException("mealitem id not found: " + id)),
                HttpStatus.OK
        );
    }


}
