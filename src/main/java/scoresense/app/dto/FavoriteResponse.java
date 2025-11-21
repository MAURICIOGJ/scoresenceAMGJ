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
public class FavoriteResponse {

    @JsonProperty("favorite_id")
    Long favoriteId;

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("entity_type")
    String entityType;

    @JsonProperty("entity_id")
    Long entityId;
}
