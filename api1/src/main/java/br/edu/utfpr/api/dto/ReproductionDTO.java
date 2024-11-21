package br.edu.utfpr.api.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReproductionDTO {
    @NotNull(message = "Animal is mandatory")
    private Integer animalId; // Assuming only the animal ID is needed for the DTO

    @NotNull(message = "Insemination date is mandatory")
    private Date inseminationDate;

    private Date expectedBirthDate;

    @Size(max = 255, message = "Observations cannot exceed 255 characters")
    private String observations;

    @NotNull(message = "Pregnancy confirmation is mandatory")
    private boolean pregnancyConfirmed;
}
