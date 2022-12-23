package com.springmongo.simpleapp.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(
        collection = "person"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    private String personalId;
    private String firstName;
    private String lastName;

    private String email;

    private int age;
    private List<String> hobbies;
    private List<Address> addresses;
}
