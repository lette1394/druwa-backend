package me.druwa.be.domain.test;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Person {
    private final String name;
    private final Integer age;
    private final Boolean isMarried;
    private final List<String> hobbies;
    private final List<Person> kids;
}