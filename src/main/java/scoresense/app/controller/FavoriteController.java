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
import scoresense.app.dto.FavoriteRequest;
import scoresense.app.dto.FavoriteResponse;
import scoresense.app.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Favorite API Endpoints")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // --- LISTADO GENERAL ---
    @GetMapping
    @Operation(summary = "List all favorites", description = "Returns a list of all favorites (without pagination)")
    public ResponseEntity<List<FavoriteResponse>> getAll() {
        return ResponseEntity.ok(favoriteService.getAll());
    }

    @GetMapping("/paged")
    @Operation(summary = "Page favorites", description = "Get a paginated list of favorites")
    public ResponseEntity<Page<FavoriteResponse>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(favoriteService.getAllPaged(pageable));
    }

    // --- CRUD BÁSICO ---
    @GetMapping("/{id}")
    @Operation(summary = "Get favorite by ID", description = "Return a favorite by using ID")
    public ResponseEntity<FavoriteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new favorite", description = "Create a new favorite for a user (team or player)")
    public ResponseEntity<FavoriteResponse> create(@Valid @RequestBody FavoriteRequest req) {
        FavoriteResponse created = favoriteService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update favorite", description = "Update favorite information by ID")
    public ResponseEntity<FavoriteResponse> update(@PathVariable Long id, @Valid @RequestBody FavoriteRequest req) {
        FavoriteResponse updated = favoriteService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a favorite", description = "Delete a favorite by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- CONSULTAS ESPECIALIZADAS ---
    @GetMapping("/by-user")
    @Operation(summary = "Search favorites by user", description = "Returns a list of favorites for a specific user")
    public ResponseEntity<List<FavoriteResponse>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(favoriteService.findByUser(userId));
    }

    @GetMapping("/users-favoriting-team")
    @Operation(summary = "Get users favoriting a team", description = "Returns favorites where the entity is a specific team")
    public ResponseEntity<List<FavoriteResponse>> getUsersFavoritingTeam(@RequestParam Long teamId) {
        return ResponseEntity.ok(favoriteService.findUsersWhoFavoritedTeam(teamId));
    }
}
