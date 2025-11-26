package com.schadraq.movies.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "movie", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"title", "release_year" })
})
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotBlank(message = "The title must contain content")
	private String title;

	@Column(name = "release_year")
	@Min(value = 1888, message = "The release year must be at least 1888")
	@Max(value = 2025, message = "The release year cannot be in the future")
	private int releaseYear;

	public Movie() {
		
	}

	public Movie(String title, int releaseYear) {
		this.title = title;
		this.releaseYear = releaseYear;
	}
}

