package dat3.server_to_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dat3.server_to_server.api_facade.AzureTranslate;
import dat3.server_to_server.api_facade.OmdbFacade;
import dat3.server_to_server.dto.MovieOmdbResponse;
import dat3.server_to_server.entity.Movie;
import dat3.server_to_server.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MovieService {

  MovieRepository movieRepository;

  @Autowired
  AzureTranslate translator;

  @Autowired
  OmdbFacade omdbFacade;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Movie getMovieByImdbId(String imdbId) {
    return movieRepository.findByImdbID(imdbId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
  }


  public Movie addMovie(String imdbId) throws JsonProcessingException {
    MovieOmdbResponse dto = omdbFacade.getMovie(imdbId);
    //String dkPlot = translator.translate(dto.getPlot());

    Movie movie = Movie.builder()
            .title(dto.getTitle())
            .year(dto.getYear())
            .rated(dto.getRated())
            .released(dto.getReleased())
            .runtime(dto.getRuntime())
            .genre(dto.getGenre())
            .director(dto.getDirector())
            .writer(dto.getWriter())
            .actors(dto.getActors())
            .metascore(dto.getMetascore())
            .imdbRating(dto.getImdbRating())
            .imdbVotes(dto.getImdbVotes())
            .website(dto.getWebsite())
            .response(dto.getResponse())
            .plot(dto.getPlot())
            //.plotDK(dkPlot)
            .poster(dto.getPoster())
            .imdbID(dto.getImdbID())
            .build();
    try {
      movieRepository.save(movie);
      return movie;
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getRootCause().getMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not add movie");
    }
  }
}
