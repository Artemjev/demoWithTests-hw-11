package com.example.demowithtests.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "photos")
@Data
//@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime addDate = LocalDateTime.now();

    private String description;

    @NotNull
    private String fileName;

    @NotNull
    private String fileType;

    @NotNull
    @Lob
    private byte[] data;

//    public Photo(String fileName, String fileType, @NotNull byte[] data) {
//        this.fileName = fileName;
//        this.fileType = fileType;
//        this.data = data;
//    }

    //    private String cameraType;
    //    private String photoUrl;
}
