package com.schadraq.movies.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.schadraq.movies.dto.Movie;
import com.schadraq.movies.dto.MovieRepository;

import jakarta.validation.constraints.NotBlank;

@Service
public class MovieService extends EntityService<Movie, MovieRepository> {

	@Override
	public List<Movie> get(Movie movie) {

		return get(movie, m -> getDao().findByTitleAndReleaseYear(m.getTitle(), m.getReleaseYear()));
	}

	/**
	 * Even though the controller uses this method, it is not designed
	 * specifically for the controller method because it (ie the controller
	 * method) is already automatically validated due to the validation
	 * annotations on the input parameter - the way this method is designed is
	 * to handle the other calls that are made internally to this application
	 * so that validation is performed.
	 *  
	 * @param title
	 * @return
	 */
	public List<Movie> getByTitle(String title) {

		validate(new Movie(title, Movie.MIN_RELEASE_YEAR));
    	List<Movie> movies = new ArrayList<>();
    	Collection<Movie> collection = getDao().findByTitle(title, Movie.class);
    	collection.forEach(m -> movies.add(m));
    	return movies;
	}

	public List<Movie> getByReleaseYear(int releaseYear) {

		validate(new Movie("My Fair Lady", releaseYear));
    	List<Movie> movies = new ArrayList<>();
    	Collection<Movie> collection = getDao().findByReleaseYear(releaseYear, Movie.class);
    	collection.forEach(m -> movies.add(m));
    	return movies;
	}
}
