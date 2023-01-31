package pro.sbs.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sbs.domain.Activity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ActivityCreateDto {
	
    private String play;
    private String place;
    private String startTime;
    private Integer teamId;
    private String userName;
    private Integer budget;
    
    public Activity toEntity() {
    	
        return Activity.builder()
                .play(play)
                .place(place)
                .teamId(teamId)
                .budget(budget)
                .userName(userName)
                .startTime(LocalDateTime.parse(startTime))
                .build();
    }

    
}











