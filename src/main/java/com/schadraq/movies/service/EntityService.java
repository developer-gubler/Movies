package com.schadraq.movies.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

public abstract class EntityService<T,R extends CrudRepository<T,Long>> {

	private static Logger log = LogManager.getLogger(EntityService.class);

    @Autowired
    private Validator validator;

    @Autowired
    private R dao;
    
	public abstract List<T> get(T o);

	protected R getDao() {
		return dao;
	}

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

	private void validate(T o) {

		Set<ConstraintViolation<T>> violations = validator.validate(o);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
	}
}
