package scoresense.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scoresense.app.dto.FavoriteRequest;
import scoresense.app.dto.FavoriteResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.mapper.FavoriteMapper;
import scoresense.app.model.Favorite;
import scoresense.app.model.User;
import scoresense.app.repository.FavoriteRepository;
import scoresense.app.repository.UserRepository; // Asumiendo que existe para validar usuarios

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    // --- CRUD BÁSICO ---
    // Obtener todos los favoritos sin paginación
    public List<FavoriteResponse> getAll() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteMapper::toResponse)
                .toList();
    }

    // Obtener por ID
    public FavoriteResponse getById(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "id", id));
        return FavoriteMapper.toResponse(favorite);
    }

    // Crear nuevo favorito
    public FavoriteResponse create(FavoriteRequest req) {
        // Validar que el usuario existe
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getUserId()));

        // Validar que la entidad referenciada existe (opcional pero recomendado)
        // Aquí podrías inyectar TeamRepository o PlayerRepository según req.getEntityType()
        // para verificar si el equipo o jugador realmente existe antes de guardar.
        Favorite favorite = FavoriteMapper.toEntity(req);
        favorite.setUser(user);

        Favorite saved = favoriteRepository.save(favorite);
        return FavoriteMapper.toResponse(saved);
    }

    // Actualizar favorito (aunque raramente se actualiza, se provee por completitud)
    public FavoriteResponse update(Long id, FavoriteRequest req) {
        Favorite existingFavorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "id", id));

        FavoriteMapper.copyToEntity(req, existingFavorite);

        if (!req.getUserId().equals(existingFavorite.getUser().getUserId())) {
            User newUser = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", req.getUserId()));
            existingFavorite.setUser(newUser);
        }

        Favorite updated = favoriteRepository.save(existingFavorite);
        return FavoriteMapper.toResponse(updated);
    }

    // Eliminar favorito
    public void delete(Long id) {
        if (!favoriteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Favorite", "id", id);
        }
        favoriteRepository.deleteById(id);
    }

    // --- CONSULTAS ESPECIALIZADAS ---
    // Obtener todos paginado
    public Page<FavoriteResponse> getAllPaged(Pageable pageable) {
        return favoriteRepository.findAll(pageable)
                .map(FavoriteMapper::toResponse);
    }

    // Obtener favoritos por usuario (Sin paginación)
    public List<FavoriteResponse> findByUser(Long userId) {
        // Validar si el usuario existe primero es opcional, pero buena práctica
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return favoriteRepository.findByUserUserId(userId).stream()
                .map(FavoriteMapper::toResponse)
                .toList();
    }

    // Obtener favoritos que son de un equipo específico (team_id)
    // Esta lógica filtra en memoria o podrías agregar un método en el repositorio: findByEntityTypeAndEntityId
    public List<FavoriteResponse> findUsersWhoFavoritedTeam(Long teamId) {
        // Opción A: Filtrar en base de datos (más eficiente) -> Requiere método en Repo
        // Opción B: Filtrar aquí (menos eficiente si hay muchos datos)
        // Usaremos Opción A asumiendo que agregas al repo: List<Favorite> findByEntityTypeAndEntityId(String type, Long id);
        // Por ahora, simulamos con stream si no quieres modificar el repo:
        return favoriteRepository.findAll().stream()
                .filter(f -> "team".equalsIgnoreCase(f.getEntityType()) && f.getEntityId().equals(teamId))
                .map(FavoriteMapper::toResponse)
                .toList();
    }
}
