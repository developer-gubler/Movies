package com.schadraq.movies.dto;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

	<T> Collection<T> findByTitle(String title, Class<T> type);

	Optional<Movie> findByTitleAndReleaseYear(String title, int releaseYear);
}
