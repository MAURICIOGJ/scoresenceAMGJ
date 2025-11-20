package scoresense.app.controller;

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

    // 1. ENDPOINT PRINCIPAL: Obtener todos con paginación
    @GetMapping
    @Operation(summary = "Get all players (paginated)", description = "Return a page of players. Use ?page=X&size=Y&sort=name,asc to paginate.")
    public ResponseEntity<Page<PlayerResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(playerService.getAllPaged(pageable));
    }

    // Obtener jugador por ID
    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID")
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getById(id));
    }

    // Crear nuevo jugador
    @PostMapping
    @Operation(summary = "Create a new player", description = "A player must be associated with an existing team ID.")
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest req) {
        PlayerResponse created = playerService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar jugador existente
    @PutMapping("/{id}")
    @Operation(summary = "Update player information")
    public ResponseEntity<PlayerResponse> update(@PathVariable Long id, @Valid @RequestBody PlayerRequest req) {
        PlayerResponse updated = playerService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    // Eliminar jugador por ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- CONSULTAS PERSONALIZADAS ---
    // Buscar por nacionalidad (paginado)
    @GetMapping("/by-nationality")
    @Operation(summary = "Search players by nationality", description = "Returns a paginated list of players filtered by their nationality.")
    public ResponseEntity<Page<PlayerResponse>> getByNationality(
            @RequestParam String nationality,
            Pageable pageable) {
        return ResponseEntity.ok(playerService.findByNationalityPaged(nationality, pageable));
    }

    // Buscar por ID de equipo (paginado)
    @GetMapping("/by-team/{teamId}")
    @Operation(summary = "Search players by Team ID", description = "Returns a paginated list of players belonging to a specific team.")
    public ResponseEntity<Page<PlayerResponse>> getByTeamId(
            @PathVariable Long teamId,
            Pageable pageable) {
        return ResponseEntity.ok(playerService.findByTeamIdPaged(teamId, pageable));
    }

    // Búsqueda por nombre y posición (paginado)
    @GetMapping("/search")
    @Operation(summary = "Search players by name and position", description = "Returns a paginated list of players matching the name (partial) and position.")
    public ResponseEntity<Page<PlayerResponse>> searchByNameAndPosition(
            @RequestParam String name,
            @RequestParam String position,
            Pageable pageable) {
        return ResponseEntity.ok(playerService.searchByNameAndPositionPaged(name, position, pageable));
    }
}
