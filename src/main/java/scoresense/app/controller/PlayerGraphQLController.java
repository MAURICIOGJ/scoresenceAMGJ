package scoresense.app.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import scoresense.app.dto.PlayerRequest;
import scoresense.app.dto.PlayerResponse;
import scoresense.app.service.PlayerService;

@Controller
public class PlayerGraphQLController {

    private final PlayerService playerService;

    public PlayerGraphQLController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // ==========================================
    //              QUERIES (CONSULTAS)
    // ==========================================
    // 1. Obtener todos los jugadores
    @QueryMapping
    public List<PlayerResponse> players() {
        return playerService.getAll();
    }

    // 2. Obtener un jugador por su ID
    @QueryMapping
    public PlayerResponse playerById(@Argument Long id) {
        return playerService.getById(id);
    }

    // 3. [ESPECIALIZADA] Buscar por Nacionalidad
    @QueryMapping
    public List<PlayerResponse> playersByNationality(@Argument String nationality) {
        return playerService.findByNationality(nationality);
    }

    // 4. [ESPECIALIZADA] Buscar por Posición y Equipo
    @QueryMapping
    public List<PlayerResponse> playersByPositionAndTeam(@Argument String position, @Argument Long teamId) {
        return playerService.findByPositionAndTeam(position, teamId);
    }

    // 5. [ESPECIALIZADA] Buscar por Nacionalidad y Edad Máxima
    @QueryMapping
    public List<PlayerResponse> playersByNationalityAndMaxAge(
            @Argument String nationality,
            @Argument Integer maxAge // GraphQL envía Int, Java recibe Integer
    ) {
        // Convertimos Integer a Short porque tu servicio/BD usa Short
        return playerService.findByNationalityAndMaxAge(nationality, maxAge.shortValue());
    }

    // ==========================================
    //            MUTATIONS (CAMBIOS)
    // ==========================================
    // 1. Crear Jugador (Usando Input Type)
    // GraphQL envía un objeto "request", Spring lo mapea automáticamente a PlayerRequest
    @MutationMapping
    public PlayerResponse createPlayer(@Argument("request") PlayerRequest req) {
        return playerService.create(req);
    }

    // 2. Actualizar Jugador (Usando Input Type)
    @MutationMapping
    public PlayerResponse updatePlayer(@Argument Long id, @Argument("request") PlayerRequest req) {
        return playerService.update(id, req);
    }

    // 3. Eliminar Jugador
    @MutationMapping
    public String deletePlayer(@Argument Long id) {
        playerService.delete(id);
        return "Player deleted successfully";
    }
}
