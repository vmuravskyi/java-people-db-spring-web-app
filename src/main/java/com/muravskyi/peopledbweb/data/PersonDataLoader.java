package com.muravskyi.peopledbweb.data;

import com.muravskyi.peopledbweb.biz.model.Person;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PersonDataLoader implements ApplicationRunner {

    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (personRepository.count() == 0) {
            List<Person> people = List.of(
                new Person(10L, "Pete", "Snake", LocalDate.of(1950, 1, 1), new BigDecimal("70000")),
                new Person(20L, "Jennifer", "Snake", LocalDate.of(1960, 2, 1), new BigDecimal("15000")),
                new Person(30L, "Mark", "Smith", LocalDate.of(1970, 3, 1), new BigDecimal("120000")),
                new Person(40L, "Vishnu", "McGuire", LocalDate.of(1980, 4, 1), new BigDecimal("25000")),
                new Person(40L, "Alice", "Smith", LocalDate.of(1997, 5, 1), new BigDecimal("25000")),
                new Person(40L, "Akira", "Jackson", LocalDate.of(1980, 6, 1), new BigDecimal("25000")),
                new Person(50L, "Bobby", "Kim", LocalDate.of(1990, 11, 1), new BigDecimal("35000"))
            );
            personRepository.saveAll(people);
        }
    }

}
