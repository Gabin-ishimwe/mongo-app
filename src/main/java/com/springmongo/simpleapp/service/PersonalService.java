package com.springmongo.simpleapp.service;

import com.springmongo.simpleapp.collections.Person;
import com.springmongo.simpleapp.repository.PersonalRepository;
import io.swagger.models.auth.In;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalService {
    @Autowired
    private PersonalRepository personalRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public String save(Person person) {
        return personalRepository.save(person).getPersonalId();
    }

    public List<Person> getPersonByFirstName(String name) {
        return personalRepository.findByFirstName(name);
    }

    public void deletePerson(String id) {
        personalRepository.deleteByPersonalId(id);
    }

    public List<Person> getAllPerson() {
        return personalRepository.findAll();
    }

    public List<Person> getPersonByAge(Integer min, Integer max) {
        return personalRepository.findPersonByAgeBetween(min, max);
    }

    public Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        if(name != null && !name.isEmpty()) {
            criteria.add(Criteria.where("firstName").regex(name, "i"));
        }
        if(minAge != null && maxAge != null) {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }

        if(city != null && !city.isEmpty()) {
            criteria.add(Criteria.where("addresses.city").is(city));
        }
        if(!criteria.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(criteria.toArray(new Criteria[0]))
            );

        }
        Page<Person> people = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Person.class),
                        pageable,
                        () -> mongoTemplate.count(query.skip(0).limit(0), Person.class));
        return people;
    }

    public List<Document> getOldestPersonByCity() {
        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "age");
        GroupOperation groupOperation = Aggregation.group("addresses.city").first(Aggregation.ROOT).as("oldestPersonal");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, sortOperation, groupOperation);
        List<Document> data = mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
        return data;
    }

    public List<Document> getPopulationByCity() {
        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        GroupOperation groupOperation = Aggregation.group("addresses.city").count().as("populationCount");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "populationCount");
         // projection --> retrieve particular fields we need
        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("_id").as("city")
                .andExpression("populationCount").as("count")
                .andExclude("_id");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, groupOperation, sortOperation, projectionOperation);
        List<Document> data = mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
        return data;
    }
}


