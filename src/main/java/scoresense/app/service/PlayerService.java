package scoresense.app.service;

import java.util.List;
import java.util.stream.Collectors;

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

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // --- MÉTODOS CRUD BÁSICOS ---
    public List<PlayerResponse> getAll() {
        return playerRepository.findAll()
                .stream()
                .map(PlayerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PlayerResponse getById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
        return PlayerMapper.toResponse(player);
    }

    public PlayerResponse create(PlayerRequest req) {
        Player player = PlayerMapper.toEntity(req);

        Team team = teamRepository.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getTeamId()));
        player.setTeam(team);

        Player saved = playerRepository.save(player);
        return PlayerMapper.toResponse(saved);
    }

    public PlayerResponse update(Long id, PlayerRequest req) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        PlayerMapper.copyToEntity(req, player);

        Team team = teamRepository.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getTeamId()));
        player.setTeam(team);

        Player updated = playerRepository.save(player);
        return PlayerMapper.toResponse(updated);
    }

    public void delete(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player", "id", id);
        }
        playerRepository.deleteById(id);
    }

    // --- PAGINACIÓN GENERAL ---
    public Page<PlayerResponse> getAllPaged(Pageable pageable) {
        return playerRepository.findAll(pageable)
                .map(PlayerMapper::toResponse);
    }

    // --- CONSULTAS ESPECIALIZADAS (SIN PAGINACIÓN) ---
    public List<PlayerResponse> findByNationality(String nationality) {
        return playerRepository.findByNationality(nationality)
                .stream()
                .map(PlayerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> findByPositionAndTeam(String position, Long teamId) {
        return playerRepository.findByPositionAndTeamTeamId(position, teamId)
                .stream()
                .map(PlayerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> findByNationalityAndMaxAge(String nationality, Short maxAge) {
        return playerRepository.findByNationalityAndAgeLessThanEqual(nationality, maxAge)
                .stream()
                .map(PlayerMapper::toResponse)
                .collect(Collectors.toList());
    }
}
