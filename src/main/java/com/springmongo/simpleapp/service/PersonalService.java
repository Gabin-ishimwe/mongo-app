package com.springmongo.simpleapp.service;

import com.springmongo.simpleapp.collections.Person;
import com.springmongo.simpleapp.repository.PersonalRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalService {
    @Autowired
    private PersonalRepository personalRepository;
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
    }
}
