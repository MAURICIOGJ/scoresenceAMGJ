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
    // 1. get all players
    @QueryMapping
    public List<PlayerResponse> players() {
        return playerService.getAll();
    }

    // 2. GET BY ID
    @QueryMapping
    public PlayerResponse playerById(@Argument Long id) {
        return playerService.getById(id);
    }

    // 3. [CUSTOMIZED] FIND BY NATIONALITY
    @QueryMapping
    public List<PlayerResponse> playersByNationality(@Argument String nationality) {
        return playerService.findByNationality(nationality);
    }

    // 4. [CUSTOMIZED] FIND BY POSITION AND TEAM ID
    @QueryMapping
    public List<PlayerResponse> playersByPositionAndTeam(@Argument String position, @Argument Long teamId) {
        return playerService.findByPositionAndTeam(position, teamId);
    }

    // 5. [CUSTOMIZED] FIND BY NATIONALITY AND MAX AGE
    @QueryMapping
    public List<PlayerResponse> playersByNationalityAndMaxAge(
            @Argument String nationality,
            @Argument Integer maxAge
    ) {

        return playerService.findByNationalityAndMaxAge(nationality, maxAge.shortValue());
    }

    // ==========================================
    //            MUTATIONS
    // ==========================================
    // 1. Create player
    @MutationMapping
    public PlayerResponse createPlayer(@Argument("request") PlayerRequest req) {
        return playerService.create(req);
    }

    // 2. Update player
    @MutationMapping
    public PlayerResponse updatePlayer(@Argument Long id, @Argument("request") PlayerRequest req) {
        return playerService.update(id, req);
    }

    // 3. Delete player
    @MutationMapping
    public String deletePlayer(@Argument Long id) {
        playerService.delete(id);
        return "Player deleted successfully";
    }
}
