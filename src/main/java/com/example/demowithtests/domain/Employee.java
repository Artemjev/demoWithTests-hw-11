package com.example.demowithtests.domain;


import com.example.demowithtests.util.validation.annotation.custom.CountryMatchesAddressesVerification;
import com.example.demowithtests.util.validation.annotation.custom.MarkedAsDeleted;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CountryMatchesAddressesVerification
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String country;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @OrderBy("country asc, id desc")
    private Set<Address> addresses = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Set<Photo> photos = new HashSet<>();


    @MarkedAsDeleted(value = false)
    private Boolean isDeleted = Boolean.FALSE;

    private Boolean isPrivate = Boolean.FALSE;

    private Boolean isConfirmed = Boolean.FALSE;

}
