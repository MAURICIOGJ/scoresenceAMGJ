package scoresense.app.mapper;

import scoresense.app.dto.FavoriteRequest;
import scoresense.app.dto.FavoriteResponse;
import scoresense.app.model.Favorite;

public final class FavoriteMapper {

    // Convierte FavoriteRequest a la entidad Favorite
    public static Favorite toEntity(FavoriteRequest req) {
        if (req == null) {
            return null;
        }
        Favorite favorite = new Favorite();
        favorite.setEntityType(req.getEntityType());
        favorite.setEntityId(req.getEntityId());
        // La relación con User se establece en el Service
        return favorite;
    }

    // Convierte la entidad Favorite a FavoriteResponse
    public static FavoriteResponse toResponse(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        Long userId = favorite.getUser() != null ? favorite.getUser().getUserId() : null;

        return FavoriteResponse.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(userId)
                .entityType(favorite.getEntityType())
                .entityId(favorite.getEntityId())
                .build();
    }

    // Copia propiedades para actualizaciones (aunque favoritos raramente se actualizan, es buena práctica tenerlo)
    public static void copyToEntity(FavoriteRequest req, Favorite entity) {
        if (req == null || entity == null) {
            return;
        }
        entity.setEntityType(req.getEntityType());
        entity.setEntityId(req.getEntityId());
    }
}
