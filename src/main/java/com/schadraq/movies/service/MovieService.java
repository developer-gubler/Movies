package com.schadraq.movies.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.schadraq.movies.dto.Movie;
import com.schadraq.movies.dto.MovieRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;

@Service
public class MovieService extends EntityService<Movie, MovieRepository> {

	@Override
	public List<Movie> get(Movie movie) {

		return get(movie, m -> getDao().findByTitleAndReleaseYear(m.getTitle(), m.getReleaseYear()));
	}

	public List<Movie> get(String title) {

		if (title==null || title.isBlank()) {
			Set<ConstraintViolation<Movie>> violations = new HashSet<>();
			violations.add(new ConstraintViolation<Movie>() {

				@Override
				public String getMessage() {
					return "The title must contain content";
				}

				@Override
				public String getMessageTemplate() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Movie getRootBean() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Class<Movie> getRootBeanClass() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getLeafBean() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object[] getExecutableParameters() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getExecutableReturnValue() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Path getPropertyPath() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getInvalidValue() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ConstraintDescriptor<?> getConstraintDescriptor() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public <U> U unwrap(Class<U> type) {
					// TODO Auto-generated method stub
					return null;
				}});
	        StringBuilder sb = new StringBuilder();
            sb.append("The title must contain content");
	        throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
		}
    	List<Movie> movies = new ArrayList<>();
    	Collection<Movie> collection = getDao().findByTitle(title, Movie.class);
    	collection.forEach(m -> movies.add(m));
    	return movies;
	}
}
