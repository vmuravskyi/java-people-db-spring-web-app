package com.muravskyi.peopledbweb.web.controller;

import com.muravskyi.peopledbweb.biz.model.Person;
import com.muravskyi.peopledbweb.biz.service.PersonService;
import com.muravskyi.peopledbweb.data.FileStorageRepository;
import com.muravskyi.peopledbweb.data.PersonRepository;
import com.muravskyi.peopledbweb.exception.StorageException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    public static final String DISPOSITION = " attachment; filename=\"%s\"";
    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;
    private PersonService personService;

    @Autowired
    public PeopleController(PersonRepository personRepository, FileStorageRepository fileStorageRepository,
        PersonService personService) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.personService = personService;
    }

    @ModelAttribute("people")
    public Page<Person> getPeople(@PageableDefault(size = 10) Pageable page) {
        return personService.findAll(page);
    }

    @ModelAttribute
    public Person getPerson() {
        return new Person();
    }

    @GetMapping
    public String showPeoplePage() {
        return "people";
    }

    @GetMapping("/images/{resource}")
    public ResponseEntity<Resource> getResource(@PathVariable String resource) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format(DISPOSITION, resource))
            .body(fileStorageRepository.findByName(resource));
    }

    @PostMapping
    public String savePerson(Model model, @Valid Person person, Errors errors,
        @RequestParam("photoFilename") MultipartFile photoFile)
        throws IOException {
        log.info(person);
        log.info("Filename:" + photoFile.getOriginalFilename());
        log.info("File size:" + photoFile.getSize());
        log.error("Errors: " + errors);
        if (errors.hasErrors()) {
            return "people";
        }
        try {
            personService.save(person, photoFile.getInputStream());
            return "redirect:people";
        } catch (StorageException e) {
            model.addAttribute("errorMsg", "System is currently unable to accept photo files at this time");
            return "people";
        }
    }

    @PostMapping(params = "action=delete")
    public String deletePerson(@RequestParam Optional<List<Long>> selections) {
//        selections.ifPresent(ids -> personRepository.deleteAllById(selections.get()));
        selections.ifPresent(ids -> personService.deleteAllById(ids));
        return "redirect:people";
    }

    @PostMapping(params = "action=edit")
    public String editPerson(@RequestParam Long selections, Model model) {
        if (selections != null) {
            Optional<Person> foundPerson = personRepository.findById(selections);
            model.addAttribute("person", foundPerson);
        }
        return "people";
    }

    @PostMapping(params = "action=import")
    public String importCSV(@RequestParam MultipartFile csvFile) throws IOException {
        log.info("File name: " + csvFile.getOriginalFilename());
        log.info("File size: " + csvFile.getSize());
        personService.importCSV(csvFile.getInputStream());
        return "redirect:people";
    }

}
