package com.example.demowithtests.dto.photo;

import com.example.demowithtests.util.validation.annotation.constraints.PhotoFileConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
public class PhotoReadDto {

    public Integer id;

    @Schema(description = "The field indicates whether the photo is active.", example = "true", required = true)
    public Boolean isActive = Boolean.TRUE;

    @Schema(description = "The time when photo was added. The value is assigned automatically when adding an photo")
    public LocalDateTime addDate = LocalDateTime.now();

    @Schema(description = "Photo description.", example = "My worst photo ever", required = false)
    public String description;

    @Schema(description = "Name of file.", example = "my-photo", required = true)
    public String fileName;

//    @PhotoFileConstraint(value = "image/jpeg")
    @Schema(description = "Type of file.", example = "jpeg", required = true)
    public String fileType;

    @ToString.Exclude
    @Schema(description = "Binary data.", required = true)
    public byte[] data;

}
