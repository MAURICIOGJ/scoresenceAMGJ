package scoresense.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scoresense.app.dto.PlayerRequest;
import scoresense.app.dto.PlayerResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.mapper.PlayerMapper;
import scoresense.app.model.Player;
import scoresense.app.model.Team;
import scoresense.app.repository.PlayerRepository;
import scoresense.app.repository.TeamRepository;

// La anotación @Transactional a nivel de clase aplica transacciones a todos los métodos públicos
@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository; // Necesario para asociar el equipo

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // --- MÉTODOS CRUD BÁSICOS Y PAGINACIÓN ---
    // Obtener todos con paginación (Hereda readOnly de la clase)
    public Page<PlayerResponse> getAllPaged(Pageable pageable) {
        return playerRepository.findAll(pageable)
                .map(PlayerMapper::toResponse);
    }

    // Obtener por ID
    public PlayerResponse getById(Long id) {
        Player player = playerRepository.findById(id)
                // PATRÓN COACH: 3 argumentos
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        return PlayerMapper.toResponse(player);
    }

    // Crear un nuevo Player
    public PlayerResponse create(PlayerRequest req) {
        // 1. Validar y obtener el Team (Recurso dependiente)
        Long teamId = req.getTeamId();
        Team team = teamRepository.findById(teamId)
                // PATRÓN COACH: 3 argumentos
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));

        // 2. Mapear DTO a Entidad
        Player newPlayer = PlayerMapper.toEntity(req);

        // 3. Asignar el equipo
        newPlayer.setTeam(team);

        // 4. Guardar y mapear a Response
        Player savedPlayer = playerRepository.save(newPlayer);
        return PlayerMapper.toResponse(savedPlayer);
    }

    // Actualizar un Player existente
    public PlayerResponse update(Long id, PlayerRequest req) {
        // 1. Encontrar el jugador existente
        Player existingPlayer = playerRepository.findById(id)
                // PATRÓN COACH: 3 argumentos
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        // 2. Mapear las propiedades del Request a la entidad existente
        PlayerMapper.copyToEntity(req, existingPlayer);

        // 3. Reasignar el Team si el ID es diferente
        Long newTeamId = req.getTeamId();
        if (newTeamId != null && !newTeamId.equals(existingPlayer.getTeam().getTeamId())) {
            Team newTeam = teamRepository.findById(newTeamId)
                    // PATRÓN COACH: 3 argumentos
                    .orElseThrow(() -> new ResourceNotFoundException("Team", "id", newTeamId));
            existingPlayer.setTeam(newTeam);
        }

        // 4. Guardar y mapear a Response
        Player updatedPlayer = playerRepository.save(existingPlayer);
        return PlayerMapper.toResponse(updatedPlayer);
    }

    // Eliminar por ID
    public void delete(Long id) {
        Player player = playerRepository.findById(id)
                // PATRÓN COACH: 3 argumentos
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        playerRepository.delete(player);
    }

    // --- MÉTODOS DE CONSULTA PERSONALIZADA (Hereda readOnly de la clase) ---
    // Buscar por nacionalidad (paginado)
    public Page<PlayerResponse> findByNationalityPaged(String nationality, Pageable pageable) {
        return playerRepository.findByNationality(nationality, pageable)
                .map(PlayerMapper::toResponse);
    }

    // Buscar por ID de equipo (paginado)
    public Page<PlayerResponse> findByTeamIdPaged(Long teamId, Pageable pageable) {
        return playerRepository.findByTeamTeamId(teamId, pageable)
                .map(PlayerMapper::toResponse);
    }

    // Buscar por nombre y posición (paginado)
    public Page<PlayerResponse> searchByNameAndPositionPaged(String name, String position, Pageable pageable) {
        return playerRepository.searchByNameAndPosition(name, position, pageable)
                .map(PlayerMapper::toResponse);
    }
}
