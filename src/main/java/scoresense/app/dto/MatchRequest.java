package scoresense.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchRequest {

    @NotNull
    private LocalDate matchDate;

    @NotNull
    private Integer homeScore; // SmallInt en BD -> Integer en Java

    @NotNull
    private Integer awayScore; // SmallInt en BD -> Integer en Java

    @NotNull
    private Long homeTeamId;

    @NotNull
    private Long awayTeamId;

    private Long refereeId;
}
