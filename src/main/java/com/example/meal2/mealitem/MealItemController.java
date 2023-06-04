package com.example.meal2.mealitem;

import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Tag(
    name="MealItem controller",
    description = "provides api from MealItem"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1")
public class MealItemController {

    private MealItemService mealItemService;

    @Autowired
    public void setMealItemService(MealItemService mealItemService){
        this.mealItemService = mealItemService;
    }

    @Operation(
        summary="get all MealItem objects",
        description="get all MealItem objects that match parameters"
    )
    @ApiResponses(value={
        @ApiResponse(
            responseCode="200",
            description="list of MealItem objects",
            content={@Content(
                mediaType="application/json",
                array=@ArraySchema(schema=@Schema(implementation=MealItem.class))
            )}
        )
    })
    @PreAuthorize("#u == authentication.principal.username")
    @GetMapping(value="/meals", produces={"application/json"})
    public ResponseEntity<List<MealItem>> getAllMealItems(
            @AuthenticationPrincipal User user,

            @Parameter(description="username")
            @RequestParam(required=true) String u,

            @Parameter(description="search (meal descriptions and notes)", schema=@Schema(type="string"))
            @RequestParam Optional<String> q,

            @Parameter(description="page (starts at 0)")
            @RequestParam Optional<Integer> p,

            @Parameter(description="size (default: 32, max: 50)")
            @RequestParam Optional<Integer> s,

            @Parameter(description="meal size")
            @RequestParam Optional<MealItem.MealSize> m,

            @Parameter(description="start date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> sd,

            @Parameter(description="end date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ed,

            @Parameter(description="start time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> st,

            @Parameter(description="end time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> et
    ){
        Integer userId = user.getId();

        String search = q.orElse("");
        Integer page = p.orElse(0);
        Integer size = s.orElse(0);
        MealItem.MealSize type = m.orElse(null);
        LocalDate startDate = sd.orElse(null);
        LocalDate endDate = ed.orElse(null);
        LocalTime startTime = st.orElse(null);
        LocalTime endTime = et.orElse(null);

        return new ResponseEntity<>(mealItemService.getAllMealItems(userId, search, page, size, type, startDate, endDate, startTime, endTime), HttpStatus.OK);
    }

    @Operation(
            summary="get MealItem object",
            description="get MealItem object that matches id"
    )
    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="MealItem object",
                    content={@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation=MealItem.class)
                    )}
            )
    })
    @PostAuthorize("returnObject.body.userId == authentication.principal.id")
    @GetMapping(value="/meals/{id}", produces={"application/json"})
    public ResponseEntity<MealItem> getMealItem(
            @Parameter(description="MealItem id")
            @PathVariable("id") Long id
    ){
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        if(mi.isPresent()){
            return new ResponseEntity<>(mi.get(), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Operation(
            summary="add new MealItem object",
            description="add new MealItem object"
    )
    @PreAuthorize("#mealItem.getUserId() == authentication.principal.id")
    @PostMapping(value="/meals", consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> addMealItem(@RequestBody @Valid MealItem mealItem){
        mealItemService.saveMealItem(mealItem);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @Operation(
            summary="update MealItem object",
            description="update MealItem object that matches id and has proper owner"
    )
    @PreAuthorize("#mealItem.getUserId() == authentication.principal.id")
    @PutMapping(value="/meals/{id}", consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> updateMealItem(
            @AuthenticationPrincipal User user,
            @Parameter(description="MealItem id")
            @PathVariable("id") Long id,
            @RequestBody @Valid MealItem mealItem){
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                mealItem.setId(id);
                mealItem.setUserId(user.getId());
                mealItemService.saveMealItem(mealItem);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

    @Operation(
            summary="delete MealItem object",
            description="delete MealItem object that matches id and has proper owner"
    )
    @DeleteMapping(value="/meals/{id}", produces={"application/json"})
    public ResponseEntity<?> deleteMealItem(
            @AuthenticationPrincipal User user,
            @Parameter(description="MealItem id")
            @PathVariable("id") Long id){
        Optional<MealItem> mi = mealItemService.getMealItemById(id);
        if(mi.isPresent()){
            if(Objects.equals(mi.get().getUserId(), user.getId())){
                mealItemService.deleteMealItemById(id);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        throw new ResourceNotFoundException("mealitem id not found: " + id);
    }

}
