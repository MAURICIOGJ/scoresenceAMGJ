package scoresense.app.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import io.swagger.v3.oas.annotations.Operation;
import scoresense.app.dto.PlayerRequest;
import scoresense.app.dto.PlayerResponse;
import scoresense.app.service.PlayerService;
import java.util.List;
import org.springframework.data.domain.PageRequest;

@Controller
public class PlayerGraphQLController {

    private final PlayerService playerService;

    public PlayerGraphQLController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // --- QUERIES (Consultas) ---
    // Obtiene una lista de jugadores (simulando getAll() del CoachService)
    @QueryMapping
    @Operation(summary = "Get all players", description = "Retrieves a list of all players (first page)")
    public List<PlayerResponse> players() {
        // Se llama al método paginado del servicio, pidiendo la primera página (página 0, tamaño 100)
        // ya que el CoachService usa List<CoachResponse> getAll(), que es una lista no paginada.
        return playerService.getAllPaged(PageRequest.of(0, 100)).getContent();
    }

    // Obtiene un jugador por ID
    @QueryMapping
    @Operation(summary = "Get player by ID", description = "Retrieves player details by ID")
    public PlayerResponse playerById(@Argument Long id) {
        return playerService.getById(id);
    }

    // --- MUTATIONS (Modificaciones) ---
    // Crea un nuevo jugador
    @MutationMapping
    public PlayerResponse createPlayer(
            @Argument String name,
            @Argument String position,
            @Argument Short age,
            @Argument String nationality,
            @Argument Short height,
            @Argument Short weight,
            @Argument Long teamId
    ) {
        // Construye el DTO Request a partir de los argumentos de GraphQL
        PlayerRequest req = new PlayerRequest();
        req.setName(name);
        req.setPosition(position);
        req.setAge(age);
        req.setNationality(nationality);
        req.setHeight(height);
        req.setWeight(weight);
        req.setTeamId(teamId);
        return playerService.create(req);
    }

    // Actualiza un jugador existente
    @MutationMapping
    public PlayerResponse updatePlayer(
            @Argument Long playerId,
            @Argument String name,
            @Argument String position,
            @Argument Short age,
            @Argument String nationality,
            @Argument Short height,
            @Argument Short weight,
            @Argument Long teamId
    ) {
        // Construye el DTO Request a partir de los argumentos de GraphQL
        PlayerRequest req = new PlayerRequest();
        req.setName(name);
        req.setPosition(position);
        req.setAge(age);
        req.setNationality(nationality);
        req.setHeight(height);
        req.setWeight(weight);
        req.setTeamId(teamId);
        return playerService.update(playerId, req);
    }

    // Elimina un jugador
    @MutationMapping
    public String deletePlayer(@Argument Long playerId) {
        playerService.delete(playerId);
        return "Player deleted successfully";
    }
}
