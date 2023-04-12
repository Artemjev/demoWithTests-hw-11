package com.example.demowithtests.dto.photo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


//!!!!!!!!!!!!!!!!!!!    Currently not in use! !!!!!!!!!!!!!
@Data
public class PhotoCreateDto {

    @Schema(description = "Photo description.", example = "My worst photo ever", required = false)
    public String description;

}
