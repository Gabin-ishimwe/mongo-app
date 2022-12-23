package com.springmongo.simpleapp.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(
        collection = "address"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    private String homeAddress;
    private String country;
    private String city;
}
