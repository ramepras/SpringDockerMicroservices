package com.play.movies;

import java.sql.Timestamp;

public record Rating(Long id, Long userId, Long movieId, Timestamp timestamp) {
}
