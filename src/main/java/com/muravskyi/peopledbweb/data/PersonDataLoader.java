package com.muravskyi.peopledbweb.data;

import com.muravskyi.peopledbweb.biz.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PersonDataLoader implements ApplicationRunner {

    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Person> people = List.of(
                new Person(10L, "Jake", "Snake", LocalDate.of(1950, 1, 1), new BigDecimal("70000")),
                new Person(20L, "Sarah", "Snake", LocalDate.of(1960, 2, 1), new BigDecimal("15000")),
                new Person(30L, "Jake", "Smith", LocalDate.of(1970, 3, 1), new BigDecimal("120000")),
                new Person(40L, "Johnny", "Jackson", LocalDate.of(1980, 4, 1), new BigDecimal("25000")),
                new Person(40L, "Jane", "Smith", LocalDate.of(1997, 5, 1), new BigDecimal("25000")),
                new Person(40L, "Johnny", "Jackson", LocalDate.of(1980, 6, 1), new BigDecimal("25000")),
                new Person(50L, "Bobby", "Kim", LocalDate.of(1990, 11, 1), new BigDecimal("35000"))
        );
        personRepository.saveAll(people);
    }

}
