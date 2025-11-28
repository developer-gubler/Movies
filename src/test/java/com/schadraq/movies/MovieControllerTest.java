package com.schadraq.movies;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schadraq.movies.dto.Movie;
import com.schadraq.movies.service.MovieService;

import jakarta.validation.ConstraintViolationException;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    MovieController controller;

    @MockitoBean
    private MovieService service;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void whenPostRequestToAddMovieAndValid_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("Mary Poppins", 1980);
    	String requestBody = objectMapper.writeValueAsString(m);
		given(this.service.add(m))
			.willReturn(m.getTitle() + " added!");
		MvcTestResult mtr = tester.post().uri("/movie/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatusOk()
			.hasBodyTextEqualTo(m.getTitle() + " added!");

//		MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
//        String movie = "{\"title\": \"Mary Poppins\", \"productionyear\" : \"1980\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          .andExpect(MockMvcResultMatchers.content()
//            .contentType(textPlainUtf8));
    }

    @Test
    public void whenPostRequestToAddMovieAndInvalidMovieName_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("", 1980);
    	String requestBody = objectMapper.writeValueAsString(m);
		given(this.service.add(m))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/movie/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"title\":\"The title must contain content\"}");

//		String movie = "{\"title\": \"\", \"productionyear\" : \"1980\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(MockMvcResultMatchers.status().isBadRequest())
//        .andExpect(MockMvcResultMatchers.jsonPath("$.title", Is.is("The title must contain content")))
//        .andExpect(MockMvcResultMatchers.content()
//          .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToAddMovieAndInvalidMinYear_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("Dumbo", 1886);
    	String requestBody = objectMapper.writeValueAsString(m);
		given(this.service.add(m))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/movie/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"releaseYear\":\"The release year must be at least 1888\"}");

//		String movie = "{\"title\": \"Dumbo\", \"productionyear\" : \"1926\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.status().isBadRequest())
//          .andExpect(MockMvcResultMatchers.jsonPath("$.productionyear", Is.is("The production year must be at least 1932")))
//          .andExpect(MockMvcResultMatchers.content()
//            .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenPostRequestToAddMovieAndInvalidMaxYear_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("Dumbo", 2222);
    	String requestBody = objectMapper.writeValueAsString(m);
		given(this.service.add(m))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/movie/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"releaseYear\":\"The release year cannot be in the future\"}");

//        String movie = "{\"title\": \"Dumbo\", \"productionyear\" : \"2222\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.status().isBadRequest())
//          .andExpect(MockMvcResultMatchers.jsonPath("$.productionyear", Is.is("The production year cannot be in the future")))
//          .andExpect(MockMvcResultMatchers.content()
//            .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenGetRequestToRetrieveMovieAndValid_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("Mary Poppins", 1965);
    	this.service.add(m);

    	List<Movie> list = new ArrayList<>();
    	list.add(m);
		given(this.service.get(m))
			.willReturn(list);

		MvcTestResult mtr = tester.get().uri("/movie/getByTitleAndReleaseYear").param("title", m.getTitle()).param("releaseYear", String.valueOf(m.getReleaseYear())).exchange();
		assertThat(mtr)
			.hasStatusOk()
			.hasBodyTextEqualTo(objectMapper.writeValueAsString(list));

//		MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
//        String movie = "{\"title\": \"Mary Poppins\", \"productionyear\" : \"1965\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          .andExpect(MockMvcResultMatchers.content()
//            .contentType(textPlainUtf8));
    }

    @Test
    public void whenDeleteRequestToRemoveMovieAndValid_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Movie m = new Movie("Mary Poppins", 1965);
    	this.service.add(m);

    	String requestBody = objectMapper.writeValueAsString(m);
		given(this.service.delete(m))
			.willReturn(m.getTitle() + " deleted!");
		MvcTestResult mtr = tester.delete().uri("/movie/delete").param("title", m.getTitle()).param("releaseYear", String.valueOf(m.getReleaseYear())).exchange();

		assertThat(mtr)
			.hasStatusOk()
			.hasBodyTextEqualTo(m.getTitle() + " deleted!");

//		MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
//        String movie = "{\"title\": \"Mary Poppins\", \"productionyear\" : \"1980\"}";
//        mockMvc.perform(MockMvcRequestBuilders.post("/addmovie")
//          .content(movie)
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.status().isOk())
//          .andExpect(MockMvcResultMatchers.content()
//            .contentType(textPlainUtf8));
    }

}
