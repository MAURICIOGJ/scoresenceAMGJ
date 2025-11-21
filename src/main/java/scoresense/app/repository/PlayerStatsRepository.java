package scoresense.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scoresense.app.model.PlayerStats;

@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    // 1. Consulta especializada: Obtener todos paginados
    // (Heredada de JpaRepository, pero explicitada para claridad)
    Page<PlayerStats> findAll(Pageable pageable);

    // 2. Consulta especializada: Obtener todos los jugadores con al menos una tarjeta roja
    // Spring Data JPA infiere: WHERE redCards > 0
    List<PlayerStats> findByRedCardsGreaterThan(Integer minRedCards);

    // 3. Consulta especializada: Obtener todos los jugadores que anotaron mínimo X goles
    // Spring Data JPA infiere: WHERE goals >= ?
    List<PlayerStats> findByGoalsGreaterThanEqual(Integer minGoals);
}
