package scoresense.app.mapper;

import scoresense.app.dto.MatchRequest;
import scoresense.app.dto.MatchResponse;
import scoresense.app.model.Match;

public final class MatchMapper {

    public static Match toEntity(MatchRequest req) {
        if (req == null) {
            return null;
        }

        Match match = new Match();
        match.setMatchDate(req.getMatchDate());
        match.setHomeScore(req.getHomeScore());
        match.setAwayScore(req.getAwayScore());
        // Las relaciones (Teams, Referee) se establecen en el Service usando los IDs del Request
        return match;
    }

    public static MatchResponse toResponse(Match match) {
        if (match == null) {
            return null;
        }

        Long homeTeamId = match.getHomeTeam() != null ? match.getHomeTeam().getTeamId() : null;
        Long awayTeamId = match.getAwayTeam() != null ? match.getAwayTeam().getTeamId() : null;

        // CORRECCIÓN: Ahora getReferee() existirá gracias a la corrección en el modelo Match
        Long refereeId = match.getReferee() != null ? match.getReferee().getRefereeId() : null;

        return MatchResponse.builder()
                .matchId(match.getMatchId())
                .matchDate(match.getMatchDate())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .homeTeamId(homeTeamId)
                .awayTeamId(awayTeamId)
                .refereeId(refereeId)
                .build();
    }

    public static void copyToEntity(MatchRequest req, Match entity) {
        if (req == null || entity == null) {
            return;
        }

        entity.setMatchDate(req.getMatchDate());
        entity.setHomeScore(req.getHomeScore());
        entity.setAwayScore(req.getAwayScore());
    }
}
