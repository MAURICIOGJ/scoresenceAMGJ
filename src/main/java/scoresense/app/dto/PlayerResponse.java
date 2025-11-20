package scoresense.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {

    @JsonProperty("player_id")
    Long playerId;

    @JsonProperty("name")
    String name;

    @JsonProperty("position")
    String position;

    @JsonProperty("age")
    Integer age;

    @JsonProperty("nationality")
    String nationality;

    @JsonProperty("height")
    Integer height;

    @JsonProperty("weight")
    Integer weight;

    @JsonProperty("team_id")
    Long teamId;
}
