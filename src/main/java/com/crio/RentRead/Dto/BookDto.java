package com.crio.RentRead.Dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class BookDto {
    @NotEmpty(message = "Please Specify book title")
    private String title;

    @NotEmpty(message = "Please Specify the author name")
    @NotNull(message="Please Specify the author name")
    private String authorName;

    @NotEmpty(message = "Please Specify genre")
    private String genre;
}
