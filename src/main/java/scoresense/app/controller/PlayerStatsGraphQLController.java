package scoresense.app.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import scoresense.app.dto.PlayerStatsRequest;
import scoresense.app.dto.PlayerStatsResponse;
import scoresense.app.service.PlayerStatsService;

@Controller
public class PlayerStatsGraphQLController {

    private final PlayerStatsService playerStatsService;

    public PlayerStatsGraphQLController(PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
    }

    // --- QUERIES ---
    // 1. get all player stats
    @QueryMapping
    public List<PlayerStatsResponse> allPlayerStats() {
        return playerStatsService.getAll();
    }

    // 2. get player stats by ID
    @QueryMapping
    public PlayerStatsResponse playerStatsById(@Argument Long id) {
        return playerStatsService.getById(id);
    }

    // 3. [CUSTOMIZED] players with red cards
    @QueryMapping
    public List<PlayerStatsResponse> playerStatsWithRedCards() {
        return playerStatsService.findPlayersWithRedCard();
    }

    // 4. [CUSTOMIZED] players with minimum goals
    @QueryMapping
    public List<PlayerStatsResponse> playerStatsWithMinGoals(@Argument Integer minGoals) {
        return playerStatsService.findPlayersWithMinGoals(minGoals);
    }

    // --- MUTATIONS ---
    // 1. Create stats
    @MutationMapping
    public PlayerStatsResponse createPlayerStats(@Argument("request") PlayerStatsRequest req) {
        return playerStatsService.create(req);
    }
}
