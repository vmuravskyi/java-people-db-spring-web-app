package com.muravskyi.peopledbweb.biz.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth must be specified")
    private LocalDate dob;

    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @DecimalMin(value = "1000.00", message = "Salary must be at least 1000.00")
    @NotNull(message = "Salary cannot be empty")
    private BigDecimal salary;

    private String photoFilename;

    public static Person parse(String csvLine) {
        var fields = csvLine.split(",");
        var trimmed = Arrays.stream(fields).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
        var dob = LocalDate.parse(trimmed.get(10), DateTimeFormatter.ofPattern("M/d/yyyy"));
        return new Person(null, trimmed.get(2), trimmed.get(4), dob, trimmed.get(6), new BigDecimal(trimmed.get(25)),
            null);
    }

}
