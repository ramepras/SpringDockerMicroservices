package com.play.ratings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class RatingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RatingServiceApplication.class, args);
    }
}

@Repository
interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserId(@Param("userId") Long userId);

    List<Rating> findByMovieId(@Param("movieId") Long movieId);

    List<Rating> findByRating(@Param("rating") Double rating);

}

@Service
class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }
    public Rating getById(Long id) {
        return ratingRepository.getReferenceById(id);
    }
    public List<Rating> getByUserId(Long userId) {
        return ratingRepository.findByUserId(userId);
    }
    public List<Rating> getByMovieId(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }
    public List<Rating> getByRating(Double rating) {
        return ratingRepository.findByRating(rating);
    }
}

@RestController
@RequestMapping("/ratings")
class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/all")
    public ResponseEntity<List<Rating>> getAll() {
        return ResponseEntity.ok().body(ratingService.getAll());
    }
    @GetMapping("/byId/{id}")
    public ResponseEntity<Rating> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ratingService.getById(id));
    }
    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<List<Rating>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok().body(ratingService.getByUserId(userId));
    }
    @GetMapping("/byMovieId/{movieId}")
    public ResponseEntity<List<Rating>> getByMovieId(@PathVariable Long movieId) {
        return ResponseEntity.ok().body(ratingService.getByMovieId(movieId));
    }

    @GetMapping("/byRating/{rating}")
    public ResponseEntity<List<Rating>> getByRating(@PathVariable Double rating) {
        return ResponseEntity.ok().body(ratingService.getByRating(rating));
    }
}
