package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteRes;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.mealitem.MealItemServiceImpl;
import com.example.meal2.mealitem.dto.MealItemRes;
import com.example.meal2.user.User;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public void createAfterMealNote(@Valid AfterMealNote afterMealNote) {
        long id = afterMealNote.getMealItemId();
        Optional<MealItem> amn = mealItemService.getMealItemById(id);
        if(mealItemService.existsById(id)){
            afterMealNoteRepository.save(afterMealNote);
            return;
            /*
            // di pa authenticated sa spring sec config so it returns anonymous user...
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(amn.get().getUserId() == user.getId()){
                afterMealNoteRepository.save(afterMealNote);
                return;
            }

            throw new ResourceNotFoundException("U R NOT THE OWNER OF THIS RESOURCE");
            */
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Override
    public Optional<AfterMealNote> getAfterMealNote(Long id) {
        Optional<AfterMealNote> amn = afterMealNoteRepository.findById(id);
        if(amn.isPresent()){
            AfterMealNote amn2 = amn.get();
            return amn;
        }
        throw new ResourceNotFoundException("aftermealnote id not found: " + id);
    }

    @Override
    public List<AfterMealNoteRes> getAllAfterMealNotes() {
        List<AfterMealNote> amnl = afterMealNoteRepository.findAll();
        List<AfterMealNoteRes> amnsl = new ArrayList<>();
        for(AfterMealNote amn : amnl){
            amnsl.add(new AfterMealNoteRes(
                    amn.getId(),
                    amn.getMealItemId(),
                    amn.getDate(),
                    amn.getTime(),
                    amn.getNote(),
                    new MealItemRes(
                            amn.getMealItem().getId(),
                            amn.getMealItem().getUserId(),
                            amn.getMealItem().getMeal(),
                            amn.getMealItem().getDate(),
                            amn.getMealItem().getTime(),
                            amn.getMealItem().getMealSize(),
                            amn.getMealItem().getNote()
                    )
            ));
        }
        return amnsl;
    }

    @Override
    public void updateAfterMealNote(AfterMealNote afterMealNote) {

    }

    @Override
    public void deleteAfterMealNote(Long id) {

    }
}
