package scoresense.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scoresense.app.dto.NewsRequest;
import scoresense.app.dto.NewsResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.mapper.NewsMapper;
import scoresense.app.model.News;
import scoresense.app.model.Team;
import scoresense.app.repository.NewsRepository;
import scoresense.app.repository.TeamRepository;

@Service
@Transactional
public class NewsService {

    private final NewsRepository newsRepository;
    private final TeamRepository teamRepository;

    public NewsService(NewsRepository newsRepository, TeamRepository teamRepository) {
        this.newsRepository = newsRepository;
        this.teamRepository = teamRepository;
    }

    // --- CRUD BÁSICO ---
    // Obtener todas las noticias sin paginación
    public List<NewsResponse> getAll() {
        return newsRepository.findAll()
                .stream()
                .map(NewsMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public NewsResponse getById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));
        return NewsMapper.toResponse(news);
    }

    // Crear nueva noticia
    public NewsResponse create(NewsRequest req) {
        News news = NewsMapper.toEntity(req);

        // Asociar Team
        Team team = teamRepository.findById(req.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getTeamId()));
        news.setTeam(team);

        News saved = newsRepository.save(news);
        return NewsMapper.toResponse(saved);
    }

    // Actualizar noticia existente
    public NewsResponse update(Long id, NewsRequest req) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        NewsMapper.copyToEntity(req, existingNews);

        // Actualizar Team si cambió
        if (!req.getTeamId().equals(existingNews.getTeam().getTeamId())) {
            Team newTeam = teamRepository.findById(req.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team", "id", req.getTeamId()));
            existingNews.setTeam(newTeam);
        }

        News updated = newsRepository.save(existingNews);
        return NewsMapper.toResponse(updated);
    }

    // Eliminar noticia
    public void delete(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException("News", "id", id);
        }
        newsRepository.deleteById(id);
    }

    // --- CONSULTAS ESPECIALIZADAS ---
    // 1. Obtener todas paginado (Consulta especializada paginada)
    public Page<NewsResponse> getAllPaged(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(NewsMapper::toResponse);
    }

    // 2. Obtener noticias por ID de equipo (Sin paginación)
    public List<NewsResponse> findByTeamId(Long teamId) {
        return newsRepository.findByTeamTeamId(teamId)
                .stream()
                .map(NewsMapper::toResponse)
                .collect(Collectors.toList());
    }
}
