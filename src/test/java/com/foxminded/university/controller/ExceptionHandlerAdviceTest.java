package com.foxminded.university.controller;

import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.service.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerAdviceTest {

    private MockMvc mockMvc;

    @Mock
    private AudienceService audienceService;
    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).setControllerAdvice(new ExceptionHandlerAdvice())
            .build();
    }

    @Test
    void whenFindEntityWithWrongParameter_thenThrowAnyException() throws Exception {
        when(audienceService.findById(10000)).thenThrow(new EntityNotFoundException("test error"));

        mockMvc.perform(get("/audiences/{id}", 10000))
            .andExpect(status().isNotFound())
            .andExpect(view().name("exception"))
            .andExpect(forwardedUrl("exception"))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
            .andExpect(result -> assertEquals("test error", result.getResolvedException().getMessage()));
    }
}
