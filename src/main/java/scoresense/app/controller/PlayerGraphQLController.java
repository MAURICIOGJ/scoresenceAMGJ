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

    // --- QUERIES (Para obtener datos) ---
    // Mapea a la query 'players' en el esquema
    @QueryMapping
    public List<PlayerResponse> players() {
        // Usa el método getAll() sin paginación que creamos en el servicio
        return playerService.getAll();
    }

    // Mapea a la query 'playerById' en el esquema
    @QueryMapping
    public PlayerResponse playerById(@Argument Long id) {
        return playerService.getById(id);
    }

    // --- MUTATIONS (Para modificar datos) ---
    // Mapea a la mutation 'createPlayer'
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

    // Mapea a la mutation 'updatePlayer'
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

    // Mapea a la mutation 'deletePlayer'
    @MutationMapping
    public String deletePlayer(@Argument Long playerId) {
        playerService.delete(playerId);
        return "Player deleted successfully";
    }
}
