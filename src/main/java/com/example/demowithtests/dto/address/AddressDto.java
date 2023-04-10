package com.example.demowithtests.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AddressDto {

    public Long id;

    @Schema(description = "The field indicates whether the address is active.", example = "true", required = true)
    public Boolean isActive = Boolean.TRUE;

    @Schema(description = "Name of the country.", example = "England", required = true)
    public String country;

    @Schema(description = "City name.", example = "London", required = true)
    public String city;

    @Schema(description = "Street name.", example = "Oxford Street", required = true)
    public String street;

    @Schema(description = "The time when address was added. The value is assigned automatically when adding an address")
    public LocalDateTime datetime = LocalDateTime.now();
}