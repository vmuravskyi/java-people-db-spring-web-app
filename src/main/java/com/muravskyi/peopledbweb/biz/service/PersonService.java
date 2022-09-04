package com.muravskyi.peopledbweb.biz.service;

import com.muravskyi.peopledbweb.biz.model.Person;
import com.muravskyi.peopledbweb.data.FileStorageRepository;
import com.muravskyi.peopledbweb.data.PersonRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
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

    @Query(nativeQuery = true, value = "select photo_filename from person where id in :ids")
    public Set<String> findFilenamesByIds(Iterable<Long> ids) {
        return personRepository.findFilenamesByIds(ids);
    }

    public Iterable<Person> findAll(Sort sort) {
        return personRepository.findAll(sort);
    }

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public <S extends Person> S save(S entity) {
        return personRepository.save(entity);
    }

    public void deleteAllById(Iterable<Long> ids) {
//        var allById = personRepository.findAllById(ids);
//        var peopleStream = StreamSupport.stream(allById.spliterator(), false);
//        var fileNames = peopleStream
//            .map(Person::getPhotoFilename)
//            .collect(Collectors.toCollection(HashSet::new));
        Set<String> filenamesByIds = personRepository.findFilenamesByIds(ids);
        personRepository.deleteAllById(ids);
        fileStorageRepository.deleteAllByName(filenamesByIds);
    }

    public void deleteAll(Iterable<? extends Person> entities) {
        personRepository.deleteAll(entities);
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

    public void importCSV(InputStream csvFileInputStream) {
        try {
            var zipInputStream = new ZipInputStream(csvFileInputStream);
            zipInputStream.getNextEntry();
            var inputStreamReader = new InputStreamReader(zipInputStream);
            var bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.lines()
                .skip(1)
                .map(Person::parse)
                .limit(20)
                .forEach(personRepository::save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
