package me.druwa.be.domain.test;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Customer {

    @Size(min = 2, max = 30, message = "cannot be null")
    private String name;

    @Email
    @NotBlank(message = "cannot be blank")
    private String email;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @NotNull
    private Gender gender;

    @Past
    @NotNull
    private LocalDate birthday;

    @Phone
    private String phone;

    public enum Gender {
        MALE, FEMALE
    }
}