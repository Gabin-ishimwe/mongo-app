package com.springmongo.simpleapp.controller;

import com.springmongo.simpleapp.collections.Person;
import com.springmongo.simpleapp.service.PersonalService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class Personcontroller {
    @Autowired
    private PersonalService personalService;

    @PostMapping
    public String savePerson(@RequestBody Person person) {
        return personalService.save(person);
    }

//    @GetMapping
//    public List<Person> findByName(@RequestParam("name") String name) {
//        return personalService.getPersonByFirstName(name);
//    }

    @GetMapping
    public List<Person> findAllPerson() {
        return personalService.getAllPerson();
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable String id) {
        personalService.deletePerson(id);
    }

    @GetMapping("/age")
    public List<Person> getPersonByAge(@RequestParam Integer min, @RequestParam Integer max) {
        return personalService.getPersonByAge(min, max);
    }

    @GetMapping("/search")
    public Page<Person> searchPerson(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        return personalService.search(name, minAge, maxAge, city, pageable);
    }
}
