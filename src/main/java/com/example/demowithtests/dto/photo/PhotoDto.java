package com.example.demowithtests.dto.photo;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PhotoDto {

    private LocalDateTime addDate = LocalDateTime.now();

    private String description;

    private String cameraType;

    private String photoUrl;
}
