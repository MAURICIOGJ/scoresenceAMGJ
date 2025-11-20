package scoresense.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String position;

    @NotNull
    @Min(value = 15)
    @Max(value = 50)
    private Short age;

    @NotBlank
    @Size(max = 30)
    private String nationality;

    @NotNull
    @Min(value = 100)
    @Max(value = 250)
    private Short height; // smallint en BD

    @NotNull
    @Min(value = 40)
    @Max(value = 150)
    private Short weight; // smallint en BD

    @NotNull
    private Long teamId;
}
