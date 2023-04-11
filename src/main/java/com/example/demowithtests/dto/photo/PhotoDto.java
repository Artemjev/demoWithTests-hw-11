package com.example.demowithtests.dto.photo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PhotoDto {

    @Schema(description = "The time when photo was added. The value is assigned automatically when adding an address")
    public LocalDateTime addDate = LocalDateTime.now();

    @Schema(description = "Photo description.", example = "My worst photo ever", required = false)
    public String description;

    @Schema(description = "Name of file.", example = "my-photo", required = true)
    public String fileName;

    @Schema(description = "Type of file.", example = "jpeg", required = true)
    public String fileType;

    @Schema(description = "Binary data.", required = true)
    public byte[] data;

//    @Schema(description = "Type or model of the camera.", example = "Nokia#123", required = false)
//    private String cameraType;

//    @Schema(description = "Link to the photo in the repository.", example = "https://prnt.sc/swoMD5th8Q-x",
//            required = false)
//    private String photoUrl;


}
