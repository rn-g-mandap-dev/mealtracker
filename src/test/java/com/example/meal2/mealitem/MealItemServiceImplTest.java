package com.example.meal2.mealitem;

import com.example.meal2.exception.NotResourceOwnerException;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.dto.MealItemCreationDTO;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealItemServiceImplTest {

    @Mock
    private MealItemRepository mealItemRepository;

    @InjectMocks
    private MealItemServiceImpl mealItemService;

    private User user;

    private MealItem mealItem;

    @BeforeEach
    public void setup(){
        user = new User();
        user.setId(9999);
        user.setUsername("test_user");
        user.setPassword("password");
        user.setRole(Role.USER);

        mealItem = new MealItem();
        mealItem.setId(9999L);
        mealItem.setUserId(9999);
        mealItem.setMeal("standard meal");
        mealItem.setDate(LocalDate.parse("2023-06-09"));
        mealItem.setTime(LocalTime.parse("14:35:00"));
        mealItem.setMealSize(MealItem.MealSize.heavy);
        mealItem.setNote("test note");
    }

    @Test
    void normal_createMealItem(){
        MealItemCreationDTO micDTO = new MealItemCreationDTO(
                "standard meal",
                LocalDate.parse("2023-06-09"),
                LocalTime.parse("14:35:00"),
                MealItem.MealSize.heavy,
                "test note"
        );

        /*
        MealItem bmealItem = new MealItem();
        bmealItem.setUserId(9999);
        bmealItem.setMeal("standard meal");
        bmealItem.setDate(LocalDate.parse("2023-06-09"));
        bmealItem.setTime(LocalTime.parse("14:35:00"));
        bmealItem.setMealSize(MealItem.MealSize.heavy);
        bmealItem.setNote("test note");
        */

        //when(mealItemRepository.save(bmealItem)).thenReturn(mealItem);
        when(mealItemRepository.save(any(MealItem.class))).thenReturn(mealItem);

        Long returnId = mealItemService.createMealItem(user, micDTO);

        //verify(mealItemRepository, times(1)).save(mealItem);
        verify(mealItemRepository, times(1)).save(any(MealItem.class));
        assertEquals(mealItem.getId(), returnId);
    }

    @Test
    void normal_deleteMealItemById() {
        when(mealItemRepository.findById(9999L)).thenReturn(Optional.of(mealItem));
        willDoNothing().given(mealItemRepository).deleteById((9999L));

        mealItemService.deleteMealItemById(user, 9999L);

        verify(mealItemRepository, times(1)).deleteById(9999L);
    }

    @Test
    void notFoundId_deleteMealItemById() {
        Long id = 10_000L;

        when(mealItemRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> {mealItemService.deleteMealItemById(user, id);}
        );

        Assertions.assertEquals("mealitem id not found: 10000", thrown.getMessage());
        verify(mealItemRepository, never()).deleteById(id);
    }

    @Test
    void notResourceOwner_deleteMealItemById() {
        mealItem.setUserId(10_000);

        when(mealItemRepository.findById(9999L)).thenReturn(Optional.of(mealItem));

        NotResourceOwnerException thrown = Assertions.assertThrows(
                NotResourceOwnerException.class,
                () -> {mealItemService.deleteMealItemById(user, 9999L);}
        );

        Assertions.assertEquals("does not own this resource", thrown.getMessage());
        verify(mealItemRepository, never()).deleteById(9999L);
    }
}