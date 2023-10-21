package com.play.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EntityScan("com.play.movies")
public class MovieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Repository
interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByMovieYear(@Param("movieYear") Long movieYear);
}

@Service
class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    public Movie getById(Long id) {
        return movieRepository.getReferenceById(id);
    }

    public List<Movie> getByYear(Long movieYear) {
        return movieRepository.findByMovieYear(movieYear);
    }

    public List<Movie> getByGenres(String genre) {
        return getAll().stream().filter(movie -> movie.getMovieGenres().contains(genre)).collect(Collectors.toList());
    }

    public List<Movie> getByRating(List<Rating> ratingList) {
        System.out.println("Number of Ratings: "+ratingList.size());
        Set<Long> movieIds = ratingList.stream().map(rating -> rating.movieId()).collect(Collectors.toSet());
        System.out.println("Number of Unique Movie IDs: "+movieIds.size());
        return movieIds.stream().map(id -> getById(id)).collect(Collectors.toList());
    }
}

@RestController
@RequestMapping("/movies")
class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAll() {
        return ResponseEntity.ok().body(movieService.getAll());
    }

    @GetMapping("/byId/{movieId}")
    public ResponseEntity<Movie> getById(@PathVariable Long movieId) {
        return ResponseEntity.ok().body(movieService.getById(movieId));
    }

    @GetMapping("/byYear/{movieYear}")
    public ResponseEntity<List<Movie>> getByYear(@PathVariable Long movieYear) {
        return ResponseEntity.ok().body(movieService.getByYear(movieYear));
    }

    @GetMapping("/byGenre/{movieGenre}")
    public ResponseEntity<List<Movie>> getByGenre(@PathVariable String movieGenre) {
        return ResponseEntity.ok().body(movieService.getByGenres(movieGenre));
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/byRating/{rating}")
    public ResponseEntity<List<Movie>> getByRating(@PathVariable Double rating) {
        System.out.println("Controller: " + rating);
        List<Movie> movies = new ArrayList<>();
        ResponseEntity<List<Rating>> forEntity = restTemplate.exchange(
                "http://localhost:7002/ratings/byRating/" + rating,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Rating>>() {
                }
        );
        if (forEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Web-service call OK");
            movies = movieService.getByRating(forEntity.getBody());
        } else {
            System.out.println("Web-service call KO");
            // Handle the case where the HTTP request was not successful (e.g., 404, 500, etc.).
            // You might want to log an error or throw an exception.
        }
        movies.forEach(System.out::println);
        return ResponseEntity.ok().body(movies);
    }

}

