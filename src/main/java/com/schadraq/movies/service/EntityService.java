package com.schadraq.movies.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.schadraq.movies.dto.Movie;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import jakarta.validation.metadata.ConstraintDescriptor;

public abstract class EntityService<T,R extends CrudRepository<T,Long>> {

	private static Logger log = LogManager.getLogger(EntityService.class);

    @Autowired
    private Validator validator;

    @Autowired
    private R dao;

	protected R getDao() {
		return dao;
	}
    
	public abstract List<T> get(T o);

	protected List<T> get(T o, Function<T, Optional<T>> f) {

		validate(o);
    	List<T> list = new ArrayList<>();
    	Optional<T> opt = f.apply(o);
    	if (opt.isPresent()) {
    		list.add(opt.get());
    	}
    	return list;
	}

	public List<T> list() {

		List<T> list = new ArrayList<>();
    	dao.findAll().forEach(o -> list.add((T)o));
    	return list;
	}

	public String add(T o) {

//		List<T> list = get(o);
//		if (list.size() == 0) {
			validate(o);
			dao.save(o);
	        return o.getClass().getSimpleName() + " added!";
//		}
//        return o.getClass().getSimpleName() + " already exists!";
	}

	public String delete(T o) {

		List<T> list = get(o);
		if (list.size() > 0) {
			dao.delete(o);
	        return o.getClass().getSimpleName() + " deleted!";
		}
        return o.getClass().getSimpleName() + " does not exist!";
	}

	protected void validate(T o) {

		Set<ConstraintViolation<T>> violations = validator.validate(o);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
	}

	protected <V> void validate(V value, Function<V, Boolean> f, String message) {
		if (f.apply(value)) {
			Set<ConstraintViolation<Movie>> violations = new HashSet<>();
			violations.add(new ConstraintViolation<Movie>() {

				@Override
				public String getMessage() {
					return message;
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
				}
			});
	        StringBuilder sb = new StringBuilder();
            sb.append("The title must contain content");
	        throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
		}
	}
}
