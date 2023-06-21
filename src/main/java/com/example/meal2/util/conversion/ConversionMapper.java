package com.example.meal2.util.conversion;

import com.example.meal2.thoughtrecord.dto.MoodDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordDTO;
import com.example.meal2.thoughtrecord.entity.ThoughtRecord;
import org.springframework.stereotype.Component;

@Component
public class ConversionMapper {

    public ThoughtRecordDTO convertThoughtRecord2DTO(ThoughtRecord tr){
        return new ThoughtRecordDTO(
                tr.getId(),
                tr.getUserId(),
                tr.getDate(),
                tr.getTime(),
                tr.getSituation(),
                tr.getMoods().stream()
                        .map(m -> new MoodDTO(m.getId(),m.getMood(),m.getLevel()))
                        .toList(),
                tr.getThoughts().stream()
                        .map(t -> new ThoughtDTO(t.getId(),t.getThought(),t.getLevel()))
                        .toList()
        );
    }

}
