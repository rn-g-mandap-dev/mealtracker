package com.example.meal2.thoughtrecord.controller;

import com.example.meal2.thoughtrecord.dto.ThoughtRecordDTO;
import com.example.meal2.thoughtrecord.service.ThoughtRecordService;
import com.example.meal2.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Tag(
        name="ThoughtRecord controller",
        description = "provides api for ThoughtRecord"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v2")
public class ThoughtRecordControllerV2 {

    private final String PATH_HEADER = "/thought-records";
    private final ThoughtRecordService thoughtRecordService;

    public ThoughtRecordControllerV2(ThoughtRecordService thoughtRecordService) {
        this.thoughtRecordService = thoughtRecordService;
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="list of ThoughtRecordDTOs",
                    content={@Content(
                            mediaType="application/json",
                            array=@ArraySchema(schema=@Schema(implementation= ThoughtRecordDTO.class))
                    )}
            )
    })
    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<?> getAllThoughtRecords(
            @AuthenticationPrincipal User user,

            @Parameter(description="search situation", schema=@Schema(type="string"))

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
                thoughtRecordService.getThoughtRecordsPage(user, search, page, size, startDate, endDate, startTime, endTime),
                HttpStatus.OK
        );
    }

}
