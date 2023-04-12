package com.example.demowithtests.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "photos")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_active")
    private Boolean isActive/* = Boolean.TRUE*/;

    @NotNull
    @Column(name = "add_date")
    private LocalDateTime addDate /*= LocalDateTime.now()*/;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @ToString.Exclude
    @Column(name = "data")
    private byte[] data; //    bytea в постгресе должно быть!

}
