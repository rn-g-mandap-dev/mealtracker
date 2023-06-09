package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteRes;

import java.util.List;
import java.util.Optional;

public interface AfterMealNoteService {
    void createAfterMealNote(AfterMealNote afterMealNote);
    Optional<AfterMealNote> getAfterMealNote(Long id);
    List<AfterMealNote> getAllAfterMealNotes();
    void updateAfterMealNote(AfterMealNote afterMealNote);
    void deleteAfterMealNote(Long id);
}
