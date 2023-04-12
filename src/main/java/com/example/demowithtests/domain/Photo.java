package com.example.demowithtests.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "photos")
@Data
//@ToString
//@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "add_date")
    private LocalDateTime addDate = LocalDateTime.now();

    @Column(name = "description")
    private String description;

    //    @NotNull
    @Column(name = "file_name")
    private String fileName;

    //    @NotNull
    @Column(name = "file_type")
    private String fileType;

    //    @NotNull
    //    @Lob
    @ToString.Exclude
    @Column(name = "data")
    private byte[] data;

    //    Employee employee;

    //    public Photo(String fileName, String fileType, @NotNull byte[] data) {
    //        this.fileName = fileName;
    //        this.fileType = fileType;
    //        this.data = data;
    //    }

    //    private String cameraType;
    //    private String photoUrl;
}
