package scoresense.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import scoresense.app.model.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 1. Consulta general con paginación
    // (Heredada de JpaRepository, pero explicitada para claridad)
    Page<Favorite> findAll(Pageable pageable);

    // 2. Consulta general SIN paginación (Nuevo requerimiento para listas completas)
    // JpaRepository.findAll() ya retorna List<Favorite>, pero lo declaramos explícitamente
    List<Favorite> findAll();

    // 3. Consulta especializada: Obtener favoritos por ID de usuario (Sin paginación)
    // Spring Data JPA infiere: WHERE user.userId = ?
    // (Asumiendo que la entidad User tiene el campo 'userId')
    List<Favorite> findByUserUserId(Long userId);

    // Opcional: Si quisieras buscar por tipo de entidad (ej: todos los favoritos que son 'team')
    List<Favorite> findByEntityType(String entityType);
}
