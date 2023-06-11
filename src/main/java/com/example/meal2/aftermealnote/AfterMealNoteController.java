package com.example.meal2.aftermealnote;

import com.example.meal2.aftermealnote.dto.AfterMealNoteCreationDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteDetailedDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteUpdateDTO;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.mealitem.MealItem;
import com.example.meal2.mealitem.dto.MealItemDetailedDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Tag(
        name="AfterMealNote controller",
        description = "provides api for AfterMealNote"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1")
public class AfterMealNoteController {

    private final String PATH_HEADER = "/after-meal-notes";

    private final AfterMealNoteService afterMealNoteService;

    public AfterMealNoteController(AfterMealNoteService afterMealNoteService) {
        this.afterMealNoteService = afterMealNoteService;
    }

    @PostMapping(value=PATH_HEADER, consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> createAfterMealNote(
            @AuthenticationPrincipal User user,
            @RequestBody AfterMealNoteCreationDTO afterMealNoteCreationDTO)
    {
        return new ResponseEntity<>(
                afterMealNoteService.createAfterMealNote(user, afterMealNoteCreationDTO),
                HttpStatus.CREATED
        );
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="AfterMealNoteDetailedDTO",
                    content={@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation= AfterMealNoteDetailedDTO.class)
                    )}
            )
    })
    @GetMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<?> getAfterMealNote(
            @AuthenticationPrincipal User user,
            @Parameter(description="AfterMealNote id")
            @PathVariable("id") Long id
    ){
        return new ResponseEntity<>(
                afterMealNoteService.getAfterMealNote(user, id),
                HttpStatus.OK
        );
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="list of AfterMealNoteDetailedDTOs",
                    content={@Content(
                            mediaType="application/json",
                            array=@ArraySchema(schema=@Schema(implementation=AfterMealNoteDetailedDTO.class))
                    )}
            )
    })
    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<?> getAllMealItems(
            @AuthenticationPrincipal User user,

            @Parameter(description="search note", schema=@Schema(type="string"))

            @RequestParam Optional<String> q,

            @Parameter(description="page (starts at 0)")
            @RequestParam Optional<Integer> p,

            @Parameter(description="size (default: 32, max: 50)")
            @RequestParam Optional<Integer> s,

            @Parameter(description="start date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> sd,

            @Parameter(description="end date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ed,

            @Parameter(description="start time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> st,

            @Parameter(description="end time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> et
    ){
        String search = q.orElse("");
        Integer page = p.orElse(0);
        Integer size = s.orElse(0);
        LocalDate startDate = sd.orElse(null);
        LocalDate endDate = ed.orElse(null);
        LocalTime startTime = st.orElse(null);
        LocalTime endTime = et.orElse(null);

        return new ResponseEntity<>(
                afterMealNoteService.getAfterMealNotes(user, search, page, size, startDate, endDate, startTime, endTime),
                HttpStatus.OK
        );
    }

    @PutMapping(value=PATH_HEADER+"/{id}", consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> updateAfterMealNote(
            @AuthenticationPrincipal User user,
            @Parameter(description="AfterMealNote id")
            @PathVariable("id") Long id,
            @RequestBody AfterMealNoteUpdateDTO afterMealNoteUpdateDTO)
    {
        afterMealNoteService.updateAfterMealNote(user, id, afterMealNoteUpdateDTO);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<?> deleteAfterMealNote(
            @AuthenticationPrincipal User user,
            @Parameter(description="AfterMealNote id")
            @PathVariable("id") Long id)
    {
        afterMealNoteService.deleteAfterMealNote(user, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }








    /*
    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<?> getAllAfterMealNotes(){
        return new ResponseEntity<>(
                afterMealNoteService.getAllAfterMealNotes(),
                HttpStatus.OK
        );
    }
    */



}
