package scoresense.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scoresense.app.dto.MatchRequest;
import scoresense.app.dto.MatchResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.mapper.MatchMapper;
import scoresense.app.model.Match;
import scoresense.app.model.Team;
import scoresense.app.repository.MatchRepository;
import scoresense.app.repository.TeamRepository;
// import scoresense.app.repository.RefereeRepository; // Descomenta si usas Referee

@Service
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    // private final RefereeRepository refereeRepository; // Inyectar si se requiere

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    // --- CRUD BÁSICO (SOLO LECTURA Y CREACIÓN) ---
    public MatchResponse getById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
        return MatchMapper.toResponse(match);
    }

    public MatchResponse create(MatchRequest req) {
        Match match = MatchMapper.toEntity(req);

        // Asociar Home Team
        Team homeTeam = teamRepository.findById(req.getHomeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getHomeTeamId()));
        match.setHomeTeam(homeTeam);

        // Asociar Away Team
        Team awayTeam = teamRepository.findById(req.getAwayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getAwayTeamId()));
        match.setAwayTeam(awayTeam);

        /* Lógica para Referee (opcional por ahora para que compile)
        if (req.getRefereeId() != null) {
            Referee referee = refereeRepository.findById(req.getRefereeId())
                .orElseThrow(() -> new ResourceNotFoundException("Referee", "id", req.getRefereeId()));
            match.setReferee(referee);
        }
         */
        Match saved = matchRepository.save(match);
        return MatchMapper.toResponse(saved);
    }

    // --- CONSULTAS (Paginadas y Listas) ---
    // 1. Obtener todos paginados
    public Page<MatchResponse> getAllPaged(Pageable pageable) {
        return matchRepository.findAll(pageable)
                .map(MatchMapper::toResponse);
    }

    // 2. Obtener todos en casa por team id (Sin paginar)
    public List<MatchResponse> findByHomeTeam(Long teamId) {
        return matchRepository.findByHomeTeamTeamId(teamId)
                .stream()
                .map(MatchMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 3. Obtener todos de visitante por team id (Sin paginar)
    public List<MatchResponse> findByAwayTeam(Long teamId) {
        return matchRepository.findByAwayTeamTeamId(teamId)
                .stream()
                .map(MatchMapper::toResponse)
                .collect(Collectors.toList());
    }
}
