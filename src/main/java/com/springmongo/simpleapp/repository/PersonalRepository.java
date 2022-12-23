package com.springmongo.simpleapp.repository;

import com.springmongo.simpleapp.collections.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalRepository extends MongoRepository<Person, String> {
    List<Person> findByFirstName(String name);

    void deleteByPersonalId(String id);

    // List<Person> findByAgeBetween(Integer min, Integer max);

    @Query(value = "{'age' : {$gt : ?0, $lt: ?1}}",
    fields = "{addresses: 0}")
    List<Person> findPersonByAgeBetween(Integer min, Integer max);
}
