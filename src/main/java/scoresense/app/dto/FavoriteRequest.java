package scoresense.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FavoriteRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El tipo de entidad es obligatorio")
    @Pattern(regexp = "^(team|player)$", message = "El tipo de entidad debe ser 'team' o 'player'")
    private String entityType;

    @NotNull(message = "El ID de la entidad es obligatorio")
    private Long entityId;
}
