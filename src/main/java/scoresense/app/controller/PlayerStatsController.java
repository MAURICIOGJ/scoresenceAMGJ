package scoresense.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import scoresense.app.dto.PlayerStatsRequest;
import scoresense.app.dto.PlayerStatsResponse;
import scoresense.app.service.PlayerStatsService;

@RestController
@RequestMapping("/api/player-stats")
@Tag(name = "Player Stats", description = "Operations to manage individual player match statistics.")
public class PlayerStatsController {

    private final PlayerStatsService playerStatsService;

    public PlayerStatsController(PlayerStatsService playerStatsService) {
        this.playerStatsService = playerStatsService;
    }

    // ---  GENERAL ---
    @GetMapping
    @Operation(summary = "List all stats", description = "Returns a list of all player statistics without pagination.")
    public ResponseEntity<List<PlayerStatsResponse>> getAll() {
        return ResponseEntity.ok(playerStatsService.getAll());
    }

    @GetMapping("/paged")
    @Operation(summary = "Get all stats (paginated)", description = "Returns a paginated list of all player statistics.")
    public ResponseEntity<Page<PlayerStatsResponse>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(playerStatsService.getAllPaged(pageable));
    }

    // --- Create Read ---
    @GetMapping("/{id}")
    @Operation(summary = "Get player stats by ID", description = "Returns statistics for a specific player in a match.")
    public ResponseEntity<PlayerStatsResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerStatsService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create new player stats", description = "Registers new statistics for a player in a match.")
    public ResponseEntity<PlayerStatsResponse> create(@Valid @RequestBody PlayerStatsRequest req) {
        PlayerStatsResponse created = playerStatsService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // --- Customized ---
    @GetMapping("/red-cards")
    @Operation(summary = "Search players with red cards", description = "Returns a list of statistics entries where the player received at least one red card.")
    public ResponseEntity<List<PlayerStatsResponse>> getPlayersWithRedCard() {
        return ResponseEntity.ok(playerStatsService.findPlayersWithRedCard());
    }

    @GetMapping("/min-goals")
    @Operation(summary = "Search players with minimum goals", description = "Returns a list of statistics entries where the player scored at least the specified number of goals.")
    public ResponseEntity<List<PlayerStatsResponse>> getPlayersWithMinGoals(
            @RequestParam(defaultValue = "2") Integer minGoals) {
        return ResponseEntity.ok(playerStatsService.findPlayersWithMinGoals(minGoals));
    }
}
