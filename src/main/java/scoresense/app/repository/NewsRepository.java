package scoresense.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scoresense.app.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    // 1. Consulta especializada: Obtener todas con paginación
    // (Heredada de JpaRepository, pero explicitada para claridad y uso en el servicio)
    Page<News> findAll(Pageable pageable);

    // 2. Consulta especializada: Obtener noticias por ID de equipo (Sin paginación)
    // Spring Data JPA infiere: WHERE team.teamId = ?
    List<News> findByTeamTeamId(Long teamId);
}
