package com.example.demowithtests.domain;

import lombok.Data;
import lombok.ToString;

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
    public Boolean isActive = Boolean.TRUE;

    @NotNull
    @Column(name = "add_date")
    private LocalDateTime addDate = LocalDateTime.now();

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "file_type")
    private String fileType;

    @NotNull
    //    @Lob
    @ToString.Exclude
    @Column(name = "data")
    private byte[] data; //    bytea в постгресе должно быть!

}
