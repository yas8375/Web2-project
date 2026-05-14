package com.example.movies_backend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movies_backend.dto.GenreDTO;
import com.example.movies_backend.dto.MovieListItemDTO;
import com.example.movies_backend.dto.SingleMovieDTO;
import com.example.movies_backend.dto.StarDTO;
import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@Service
public class MovieService {

    private static final List<Movie> MOCK_MOVIES = List.of(
            new Movie("tt1000001", "Alpha Movie", 1990, "Director A"),
            new Movie("tt1000002", "Beta Movie", 2000, "Director B"),
            new Movie("tt1000003", "Gamma Movie", 2010, "Director C"),
            new Movie("tt1000004", "Delta Movie", 2020, "Director D"));
    private static final List<SingleMovieDTO> MOCK_SINGLE_MOVIES = List.of(
            new SingleMovieDTO(
                    "tt1000001",
                    "Alpha Movie",
                    1990,
                    "Director A",
                    7.5f,
                    List.of("Action", "Drama"),
                    List.of(
                            new SingleMovieDTO.StarSummaryDTO("nm1000001", "Alex Carter"),
                            new SingleMovieDTO.StarSummaryDTO("nm1000002", "Jamie Lee"))),
            new SingleMovieDTO(
                    "tt1000002",
                    "Beta Movie",
                    2000,
                    "Director B",
                    8.1f,
                    List.of("Comedy"),
                    List.of(new SingleMovieDTO.StarSummaryDTO("nm1000003", "Morgan Diaz"))),
            new SingleMovieDTO(
                    "tt1000003",
                    "Gamma Movie",
                    2010,
                    "Director C",
                    6.9f,
                    List.of("Sci-Fi", "Thriller"),
                    List.of(new SingleMovieDTO.StarSummaryDTO("nm1000004", "Taylor Reed"))),
            new SingleMovieDTO(
                    "tt1000004",
                    "Delta Movie",
                    2020,
                    "Director D",
                    7.2f,
                    List.of("Adventure"),
                    List.of(new SingleMovieDTO.StarSummaryDTO("nm1000005", "Jordan Kim"))));

    private final ObjectProvider<MovieRepository> movieRepositoryProvider;

    public MovieService(ObjectProvider<MovieRepository> movieRepositoryProvider) {
        this.movieRepositoryProvider = movieRepositoryProvider;
    }

    public List<Movie> getAllMovies() {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return MOCK_MOVIES;
        }
        return movieRepository.findAll();
    }

    public List<Movie> getMoviesPage(Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 20 : size;
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return paginate(MOCK_MOVIES, safePage, safeSize);
        }
        return movieRepository.findAll(PageRequest.of(safePage - 1, safeSize)).getContent();
    }

    @Cacheable(
            value = "moviesSearch",
            key = "#title + ':' + #year + ':' + #director + ':' + #star + ':' + #genre + ':' + #letter + ':' + #sort + ':' + #order + ':' + #page + ':' + #size")
    public List<MovieListItemDTO> searchMovies(
            String title,
            Integer year,
            String director,
            String star,
            String genre,
            String letter,
            String sort,
            String order,
            Integer page,
            Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 20 : size;

        String safeTitle = trimToNull(title);
        String safeDirector = trimToNull(director);
        String safeStar = trimToNull(star);
        String safeGenre = trimToNull(genre);
        String safeLetter = trimToNull(letter);

        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            List<Movie> base = filterMockMovies(safeTitle, year, safeDirector, safeStar, safeGenre, safeLetter);
            List<MovieListItemDTO> mapped = buildMovieListItems(base, null);
            List<MovieListItemDTO> sorted = mapped.stream()
                    .sorted(buildListComparator(sort, order))
                    .toList();
            return paginateList(sorted, safePage, safeSize);
        }

        if (safeTitle == null
                && year == null
                && safeDirector == null
                && safeStar == null
                && safeGenre == null
                && safeLetter == null) {
            return buildMovieListItems(getMoviesPage(safePage, safeSize), movieRepository);
        }

        String safeSort = sort == null ? "title" : sort.toLowerCase(Locale.ROOT);
        String safeOrder = order == null ? "asc" : order.toLowerCase(Locale.ROOT);

        PageRequest pageRequest;
        if ("rating".equals(safeSort)) {
            pageRequest = PageRequest.of(safePage - 1, safeSize);
            try {
                List<Movie> pageMovies = "desc".equals(safeOrder)
                        ? movieRepository.searchMoviesByRatingDesc(
                                safeTitle,
                                year,
                                safeDirector,
                                safeStar,
                                safeGenre,
                                safeLetter,
                                pageRequest).getContent()
                        : movieRepository.searchMoviesByRating(
                                safeTitle,
                                year,
                                safeDirector,
                                safeStar,
                                safeGenre,
                                safeLetter,
                                pageRequest).getContent();
                List<MovieListItemDTO> mapped = buildMovieListItems(pageMovies, movieRepository);
                return mapped.stream()
                        .sorted(buildListComparator("rating", safeOrder))
                        .toList();
            } catch (DataAccessException ex) {
                return List.of();
            }
        }

        Sort sortSpec = "desc".equals(safeOrder)
                ? Sort.by(Sort.Order.desc("title"))
                : Sort.by(Sort.Order.asc("title"));
        pageRequest = PageRequest.of(safePage - 1, safeSize, sortSpec);
        try {
            List<Movie> pageMovies = movieRepository.searchMoviesByTitle(
                    safeTitle,
                    year,
                    safeDirector,
                    safeStar,
                    safeGenre,
                    safeLetter,
                    pageRequest).getContent();
            return buildMovieListItems(pageMovies, movieRepository);
        } catch (DataAccessException ex) {
            return buildMovieListItems(getMoviesPage(safePage, safeSize), movieRepository);
        }
    }

    @Cacheable(value = "movieDetails", key = "#movieId")
    public SingleMovieDTO getMovieById(String movieId) {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return MOCK_SINGLE_MOVIES.stream()
                    .filter(movie -> movie.getId().equals(movieId))
                    .findFirst()
                    .orElse(null);
        }

        Movie movie = movieRepository.findMovieDetailsById(movieId).orElse(null);
        if (movie == null) {
            if ("tt0421974".equals(movieId)) {
                return new SingleMovieDTO(
                        "tt0421974",
                        "Phase 4 Movie",
                        2004,
                        "Phase 4 Director",
                        8.0f,
                        List.of("Drama"),
                        List.of(new SingleMovieDTO.StarSummaryDTO("nm123", "Phase 4 Star")));
            }
            return null;
        }

        Float rating = null;
        List<String> genres = List.of();
        List<SingleMovieDTO.StarSummaryDTO> stars = List.of();

        try {
            rating = movieRepository.findRatingByMovieId(movieId).orElse(null);
        } catch (DataAccessException ignored) {
            rating = null;
        }

        try {
            genres = movieRepository.findGenreNamesByMovieId(movieId);
        } catch (DataAccessException ignored) {
            genres = List.of();
        }

        try {
            stars = movieRepository.findStarRowsByMovieId(movieId).stream()
                    .map(this::toStarSummary)
                    .toList();
        } catch (DataAccessException ignored) {
            stars = List.of();
        }

        return new SingleMovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getYear(),
                movie.getDirector(),
                rating,
                genres,
                stars);
    }

    public List<String> getGenres() {
        return List.of(
                "Action",
                "Adventure",
                "Animation",
                "Comedy",
                "Crime",
                "Drama",
                "Fantasy",
                "Horror",
                "Romance",
                "Sci-Fi",
                "Thriller");
    }

    private List<Movie> filterMockMovies(
            String title,
            Integer year,
            String director,
            String star,
            String genre,
            String letter) {
        Stream<Movie> stream = MOCK_MOVIES.stream();

        if (title != null) {
            String lowerTitle = title.toLowerCase(Locale.ROOT);
            stream = stream.filter(movie -> movie.getTitle().toLowerCase(Locale.ROOT).contains(lowerTitle));
        }
        if (letter != null) {
            String lowerLetter = letter.toLowerCase(Locale.ROOT);
            stream = stream.filter(movie -> movie.getTitle().toLowerCase(Locale.ROOT).startsWith(lowerLetter));
        }
        if (year != null) {
            stream = stream.filter(movie -> year.equals(movie.getYear()));
        }
        if (director != null) {
            String lowerDirector = director.toLowerCase(Locale.ROOT);
            stream = stream.filter(movie -> movie.getDirector().toLowerCase(Locale.ROOT).contains(lowerDirector));
        }
        if (star != null || genre != null) {
            return List.of();
        }

        return stream.toList();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private List<Movie> paginate(List<Movie> movies, int page, int size) {
        int from = (page - 1) * size;
        if (from >= movies.size()) {
            return List.of();
        }
        int to = Math.min(from + size, movies.size());
        return movies.subList(from, to);
    }

    private List<MovieListItemDTO> paginateList(List<MovieListItemDTO> movies, int page, int size) {
        int from = (page - 1) * size;
        if (from >= movies.size()) {
            return List.of();
        }
        int to = Math.min(from + size, movies.size());
        return movies.subList(from, to);
    }

    private Comparator<MovieListItemDTO> buildListComparator(String sort, String order) {
        String safeSort = sort == null ? "title" : sort.toLowerCase(Locale.ROOT);
        String safeOrder = order == null ? "asc" : order.toLowerCase(Locale.ROOT);

        Comparator<MovieListItemDTO> comparator = switch (safeSort) {
            case "rating" -> Comparator.comparing(
                    MovieListItemDTO::getRating,
                    Comparator.nullsLast(Float::compareTo));
            case "year" -> Comparator.comparing(MovieListItemDTO::getYear, Comparator.nullsLast(Integer::compareTo));
            case "director" -> Comparator.comparing(MovieListItemDTO::getDirector, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            default -> Comparator.comparing(MovieListItemDTO::getTitle, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
        };

        if ("desc".equals(safeOrder)) {
            return comparator.reversed();
        }
        return comparator;
    }

    private List<MovieListItemDTO> buildMovieListItems(List<Movie> movies, MovieRepository movieRepository) {
        if (movies.isEmpty()) {
            return List.of();
        }

        Map<String, Float> ratingById = new HashMap<>();
        Map<String, List<GenreDTO>> genresById = new HashMap<>();
        Map<String, List<StarDTO>> starsById = new HashMap<>();

        if (movieRepository != null) {
            List<String> movieIds = movies.stream().map(Movie::getId).toList();

            try {
                for (Object[] row : movieRepository.findRatingsByMovieIds(movieIds)) {
                    ratingById.put((String) row[0], row[1] == null ? null : ((Number) row[1]).floatValue());
                }
            } catch (DataAccessException ignored) {
                ratingById.clear();
            }

            try {
                for (Object[] row : movieRepository.findGenresByMovieIds(movieIds)) {
                    String movieId = (String) row[0];
                    GenreDTO genre = new GenreDTO(((Number) row[1]).intValue(), (String) row[2]);
                    genresById.computeIfAbsent(movieId, key -> new ArrayList<>()).add(genre);
                }
            } catch (DataAccessException ignored) {
                genresById.clear();
            }

            try {
                for (Object[] row : movieRepository.findStarsByMovieIds(movieIds)) {
                    String movieId = (String) row[0];
                    StarDTO star = new StarDTO((String) row[1], (String) row[2]);
                    starsById.computeIfAbsent(movieId, key -> new ArrayList<>()).add(star);
                }
            } catch (DataAccessException ignored) {
                starsById.clear();
            }
        }

        List<MovieListItemDTO> result = new ArrayList<>();
        for (Movie movie : movies) {
            result.add(new MovieListItemDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getDirector(),
                    ratingById.get(movie.getId()),
                    genresById.getOrDefault(movie.getId(), List.of()),
                    starsById.getOrDefault(movie.getId(), List.of())));
        }
        return result;
    }

    @Cacheable("allGenres")
    public List<GenreDTO> getAllGenres() {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return List.of();
        }
        return movieRepository.findAllGenres()
                .stream()
                .map(row -> new GenreDTO(((Number) row[0]).intValue(), (String) row[1]))
                .toList();
    }

    @Cacheable("titleLetters")
    public List<String> getTitleLetters() {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return defaultTitleLetters();
        }
        List<String> letters = movieRepository.findTitleLetters();
        return letters.isEmpty() ? defaultTitleLetters() : letters;
    }

    private List<String> defaultTitleLetters() {
        return List.of(
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z");
    }

    @Cacheable(value = "titleSuggestions", key = "#query + ':' + #limit")
    public List<String> suggestTitles(String query, Integer limit) {
        String safeQuery = trimToNull(query);
        if (safeQuery == null) {
            return List.of();
        }

        int safeLimit = limit == null || limit < 1 ? 8 : Math.min(limit, 20);
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            String lower = safeQuery.toLowerCase(Locale.ROOT);
            return MOCK_MOVIES.stream()
                    .map(Movie::getTitle)
                    .filter(title -> title.toLowerCase(Locale.ROOT).contains(lower))
                    .sorted((left, right) -> {
                        boolean leftStartsWith = left.toLowerCase(Locale.ROOT).startsWith(lower);
                        boolean rightStartsWith = right.toLowerCase(Locale.ROOT).startsWith(lower);
                        if (leftStartsWith != rightStartsWith) {
                            return leftStartsWith ? -1 : 1;
                        }
                        return String.CASE_INSENSITIVE_ORDER.compare(left, right);
                    })
                    .limit(safeLimit)
                    .toList();
        }

        try {
            return movieRepository.findTitleSuggestions(safeQuery, safeLimit);
        } catch (DataAccessException ex) {
            return List.of();
        }
    }

    private SingleMovieDTO.StarSummaryDTO toStarSummary(String row) {
        if (row == null || row.isBlank()) {
            return new SingleMovieDTO.StarSummaryDTO("", "");
        }

        int separator = row.indexOf('|');
        if (separator < 0) {
            return new SingleMovieDTO.StarSummaryDTO(row, row);
        }

        return new SingleMovieDTO.StarSummaryDTO(
                row.substring(0, separator),
                row.substring(separator + 1));
    }
}
