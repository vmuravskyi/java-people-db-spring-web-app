package com.muravskyi.peopledbweb.web.controller;

import com.muravskyi.peopledbweb.biz.model.Person;
import com.muravskyi.peopledbweb.data.FileStorageRepository;
import com.muravskyi.peopledbweb.data.PersonRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;

    @Autowired
    public PeopleController(PersonRepository personRepository, FileStorageRepository fileStorageRepository) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @ModelAttribute("people")
    public Iterable<Person> getPeople() {
        return personRepository.findAll();
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
        String disposition = " attachment; filename=\"%s\"";
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format(disposition, resource))
            .
    }

    @PostMapping
    public String savePerson(@Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile)
        throws IOException {
        log.info(person);
        log.info("Filename:" + photoFile.getOriginalFilename());
        log.info("File size:" + photoFile.getSize());
        log.error("Errors: " + errors);
        if (!errors.hasErrors()) {
            fileStorageRepository.save(photoFile.getOriginalFilename(), photoFile.getInputStream());
            personRepository.save(person);
            return "redirect:people";
        }
        return "people";
    }

    @PostMapping(params = "delete=true")
    public String deletePerson(@RequestParam Optional<List<Long>> selections) {
        selections.ifPresent(ids -> personRepository.deleteAllById(selections.get()));
        return "redirect:people";
    }

    @PostMapping(params = "edit=true")
    public String editPerson(@RequestParam Long selections, Model model) {
        if (selections != null) {
            Optional<Person> foundPerson = personRepository.findById(selections);
            model.addAttribute("person", foundPerson);
        }
        return "people";
    }

}
