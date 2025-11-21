package scoresense.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scoresense.app.model.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // 1. Obtener todos paginados (Ya incluido en JpaRepository, pero bueno explicitarlo)
    Page<Match> findAll(Pageable pageable);

    // 2. Obtener todos en casa por team id (Sin paginar)
    // JPA infiere: WHERE homeTeam.teamId = ?
    List<Match> findByHomeTeamTeamId(Long teamId);

    // 3. Obtener todos de visitante por team id (Sin paginar)
    // JPA infiere: WHERE awayTeam.teamId = ?
    List<Match> findByAwayTeamTeamId(Long teamId);
}
