package scoresense.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class NewsRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Size(max = 100)
    private String author;

    @Size(max = 255)
    private String sourceUrl;

    @Size(max = 255)
    private String imageUrl;

    @NotNull
    private Long teamId;
}
