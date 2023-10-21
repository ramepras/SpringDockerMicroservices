package com.play.ratings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rating {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "movie_id")
    private Long movieId;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "timestamp")
    private Timestamp timestamp;
}
