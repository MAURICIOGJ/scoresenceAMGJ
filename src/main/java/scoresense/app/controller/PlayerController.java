package scoresense.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import scoresense.app.dto.PlayerRequest;
import scoresense.app.dto.PlayerResponse;
import scoresense.app.service.PlayerService;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "Operations CRUD and search for players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // --- GENERAL  ---
    @GetMapping
    @Operation(summary = "List all players", description = "Return a list of all players without pagination.")
    public ResponseEntity<List<PlayerResponse>> getAll() {
        return ResponseEntity.ok(playerService.getAll());
    }

    @GetMapping("/paged")
    @Operation(summary = "Get all players (paginated)", description = "Return a page of players. Use ?page=X&size=Y&sort=name,asc to paginate.")
    public ResponseEntity<Page<PlayerResponse>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(playerService.getAllPaged(pageable));
    }

    // --- CRUD ---
    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID")
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new player", description = "A player must be associated with an existing team ID.")
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest req) {
        PlayerResponse created = playerService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update player information")
    public ResponseEntity<PlayerResponse> update(@PathVariable Long id, @Valid @RequestBody PlayerRequest req) {
        PlayerResponse updated = playerService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---  Customized ---
    // FIND BY NATIONALITY
    @GetMapping("/by-nationality")
    @Operation(summary = "Search players by nationality", description = "Returns a list of players filtered by their nationality.")
    public ResponseEntity<List<PlayerResponse>> getByNationality(@RequestParam String nationality) {
        return ResponseEntity.ok(playerService.findByNationality(nationality));
    }

    // FIND BY POSITION AND TEAM ID
    @GetMapping("/by-position-team")
    @Operation(summary = "Search players by position and team", description = "Returns a list of players filtered by position and team ID.")
    public ResponseEntity<List<PlayerResponse>> getByPositionAndTeam(
            @RequestParam String position,
            @RequestParam Long teamId) {
        return ResponseEntity.ok(playerService.findByPositionAndTeam(position, teamId));
    }

    // FIND BY NATIONALITY AND MAX AGE
    @GetMapping("/by-nationality-age")
    @Operation(summary = "Search players by nationality and max age", description = "Returns a list of players filtered by nationality and with age less than or equal to maxAge.")
    public ResponseEntity<List<PlayerResponse>> getByNationalityAndMaxAge(
            @RequestParam String nationality,
            @RequestParam Short maxAge) {
        return ResponseEntity.ok(playerService.findByNationalityAndMaxAge(nationality, maxAge));
    }
}
