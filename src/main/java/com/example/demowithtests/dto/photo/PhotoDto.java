package com.example.demowithtests.dto.photo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PhotoDto {

    @Schema(description = "The time when photo was added. The value is assigned automatically when adding an address")
    private LocalDateTime addDate = LocalDateTime.now();

    @Schema(description = "Photo description.", example = "My worst photo ever", required = false)
    private String description;

    @Schema(description = "Type or model of the camera.", example = "Nokia#123", required = false)
    private String cameraType;

    @Schema(description = "Link to the photo in the repository.", example = "https://prnt.sc/swoMD5th8Q-x",
            required = false)
    private String photoUrl;
}
