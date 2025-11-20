package scoresense.app.mapper;

import scoresense.app.dto.PlayerRequest;
import scoresense.app.dto.PlayerResponse;
import scoresense.app.model.Player;

// Clase utilitaria para la conversión de Player a sus DTOs
public final class PlayerMapper {

    // Convierte PlayerRequest a la entidad Player usando setters
    public static Player toEntity(PlayerRequest req) {
        if (req == null) {
            return null;
        }
        // Usamos constructor y setters (patrón Coach)
        Player player = new Player();
        player.setName(req.getName());
        player.setPosition(req.getPosition());
        player.setAge(req.getAge());
        player.setNationality(req.getNationality());
        player.setHeight(req.getHeight());
        player.setWeight(req.getWeight());
        return player;
    }

    // Convierte la entidad Player a PlayerResponse para la salida de la API
    public static PlayerResponse toResponse(Player player) {
        if (player == null) {
            return null;
        }

        // Extrae solo el ID del equipo (Foreign Key)
        Long teamId = player.getTeam() != null ? player.getTeam().getTeamId() : null;

        // Mapea la entidad al DTO, usando casting directo a (int) para tipos Short
        return PlayerResponse.builder()
                .playerId(player.getPlayerId())
                .name(player.getName())
                .position(player.getPosition())
                .age((int) player.getAge())
                .nationality(player.getNationality())
                .height((int) player.getHeight())
                .weight((int) player.getWeight())
                .teamId(teamId)
                .build();
    }

    // Copia las propiedades de Request a una entidad Player existente (para UPDATE)
    public static void copyToEntity(PlayerRequest req, Player entity) {
        if (req == null || entity == null) {
            return;
        }
        entity.setName(req.getName());
        entity.setPosition(req.getPosition());
        entity.setAge(req.getAge());
        entity.setNationality(req.getNationality());
        entity.setHeight(req.getHeight());
        entity.setWeight(req.getWeight());
    }
}
