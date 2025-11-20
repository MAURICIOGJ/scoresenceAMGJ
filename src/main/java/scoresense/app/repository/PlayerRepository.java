package scoresense.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import scoresense.app.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Page<Player> findByNationality(String nationality, Pageable pageable);

    Page<Player> findByTeamTeamId(Long teamId, Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.name LIKE %:name% AND p.position = :position")
    Page<Player> searchByNameAndPosition(String name, String position, Pageable pageable);
}
