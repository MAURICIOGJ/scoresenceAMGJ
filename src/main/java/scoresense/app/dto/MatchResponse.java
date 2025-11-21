package scoresense.app.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {

    @JsonProperty("match_id")
    Long matchId;

    @JsonProperty("match_date")
    LocalDate matchDate;

    @JsonProperty("home_score")
    Integer homeScore;

    @JsonProperty("away_score")
    Integer awayScore;

    @JsonProperty("home_team_id")
    Long homeTeamId;

    @JsonProperty("away_team_id")
    Long awayTeamId;

    @JsonProperty("referee_id")
    Long refereeId;
}
