package com.foxminded.university.controller.rest;

import com.foxminded.university.controller.SubjectController;
import com.foxminded.university.dao.mapper.SubjectDtoMapper;
import com.foxminded.university.dto.SubjectDto;
import com.foxminded.university.model.Subject;
import com.foxminded.university.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/subjects")
public class SubjectRestController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final SubjectDtoMapper subjectDtoMapper;

    public SubjectRestController(SubjectService subjectService, SubjectDtoMapper subjectDtoMapper) {
        this.subjectService = subjectService;
        this.subjectDtoMapper = subjectDtoMapper;
    }

    @GetMapping
    public Page<SubjectDto> getAllSubjects(Pageable pageable) {
        logger.debug("Show all subjects");

        return subjectService.findAll(pageable).map(subjectDtoMapper::subjectToDto);
    }

    @GetMapping("/{id}")
    public SubjectDto showSubject(@PathVariable int id) {
        logger.debug("Show subject page with id {}", id);

        return subjectDtoMapper.subjectToDto(subjectService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestBody SubjectDto subjectDto) {
        Subject subject = subjectService.save(subjectDtoMapper.dtoToSubject(subjectDto));
        logger.debug("Create new subject. Id {}", subject.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(subject.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody SubjectDto subjectDto) {
        subjectService.save(subjectDtoMapper.dtoToSubject(subjectDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestBody SubjectDto subjectDto) {
        subjectService.delete(subjectDtoMapper.dtoToSubject(subjectDto));
    }
}