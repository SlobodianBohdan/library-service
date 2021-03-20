package org.libraryservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private boolean isFree;
}
