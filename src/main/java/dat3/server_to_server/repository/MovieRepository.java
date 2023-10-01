package dat3.server_to_server.repository;

import dat3.server_to_server.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
  Optional<Movie> findByImdbID(String imdbId);
}
