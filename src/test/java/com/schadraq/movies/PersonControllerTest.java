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
import com.schadraq.movies.dto.Person;
import com.schadraq.movies.service.PersonService;

import jakarta.validation.ConstraintViolationException;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    PersonController controller;

    @MockitoBean
    private PersonService service;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void whenPostRequestToAddPersonAndValid_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Person p = new Person("Mary Poppins", LocalDate.of(1965,1,1));
    	String requestBody = objectMapper.writeValueAsString(p);
		given(this.service.add(p))
			.willReturn("Person added!");
		MvcTestResult mtr = tester.post().uri("/person/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatusOk()
			.hasBodyTextEqualTo("Person added!");
    }

    @Test
    public void whenPostRequestToAddPersonAndInvalidName_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Person p = new Person("", LocalDate.of(1965,1,1));
    	String requestBody = objectMapper.writeValueAsString(p);
		given(this.service.add(p))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/person/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"name\":\"Person name cannot be blank\"}");
    }

    @Test
    public void whenPostRequestToAddPersonAndInvalidNullBirthdate_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Person p = new Person("Mickey Mouse", null);
    	String requestBody = objectMapper.writeValueAsString(p);
		given(this.service.add(p))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/person/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"birthDate\":\"Person birthdate cannot be null\"}");
    }

    @Test
    public void whenPostRequestToAddPersonAndInvalidFutureBirthdate_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Person p = new Person("Mickey Mouse", LocalDate.of(2525,1,1));
    	String requestBody = objectMapper.writeValueAsString(p);
		given(this.service.add(p))
			.willThrow(ConstraintViolationException.class);
		MvcTestResult mtr = tester.post().uri("/person/add").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatus4xxClientError()
			.hasBodyTextEqualTo("{\"birthDate\":\"Person birthdate cannot be in the future\"}");
    }
    
    @Test
    @Disabled
    public void whenDeleteRequestToRemoveMovieAndValid_thenCorrectResponse(@Autowired MockMvcTester tester) throws Exception {

    	Person p = new Person("Mary Poppins", LocalDate.of(1965,1,1));
    	String requestBody = objectMapper.writeValueAsString(p);
		given(this.service.delete(p))
			.willReturn("Person deleted!");
		MvcTestResult mtr = tester.delete().uri("/person/delete").contentType(MediaType.APPLICATION_JSON).content(requestBody).exchange();
		assertThat(mtr)
			.hasStatusOk()
			.hasBodyTextEqualTo("Person deleted!");

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
