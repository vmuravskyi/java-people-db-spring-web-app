package com.muravskyi.peopledbweb.data;

import com.muravskyi.peopledbweb.biz.model.Person;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

    @Query(nativeQuery = true, value = "select photo_filename from person where id in :ids")
    public Set<String> findFilenamesByIds(@Param("ids") Iterable<Long> ids);

}
