package com.muravskyi.peopledbweb.biz.service;

import com.muravskyi.peopledbweb.biz.model.Person;
import com.muravskyi.peopledbweb.data.FileStorageRepository;
import com.muravskyi.peopledbweb.data.PersonRepository;
import java.io.InputStream;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final FileStorageRepository fileStorageRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, FileStorageRepository fileStorageRepository) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @Transactional
    public Person save(Person person, InputStream photoStream) {
        var savedPerson = personRepository.save(person);
        fileStorageRepository.save(person.getPhotoFilename(), photoStream);
        return savedPerson;
    }

    public <S extends Person> Iterable<S> saveAll(Iterable<S> entities) {
        return personRepository.saveAll(entities);
    }

    public Optional<Person> findById(Long aLong) {
        return personRepository.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return personRepository.existsById(aLong);
    }

    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    public Iterable<Person> findAllById(Iterable<Long> longs) {
        return personRepository.findAllById(longs);
    }

    public long count() {
        return personRepository.count();
    }

    public void deleteById(Long aLong) {
        personRepository.deleteById(aLong);
    }

    public void delete(Person entity) {
        personRepository.delete(entity);
    }

    public void deleteAllById(Iterable<? extends Long> longs) {
        personRepository.deleteAllById(longs);
    }

    public void deleteAll(Iterable<? extends Person> entities) {
        personRepository.deleteAll(entities);
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

}
