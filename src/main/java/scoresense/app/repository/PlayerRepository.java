package scoresense.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scoresense.app.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Page<Player> findAll(Pageable pageable);

    List<Player> findByNationality(String nationality);

    List<Player> findByPositionAndTeamTeamId(String position, Long teamId);

    List<Player> findByNationalityAndAgeLessThanEqual(String nationality, Short maxAge);
}
