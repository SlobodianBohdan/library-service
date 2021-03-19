package org.libraryservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private boolean isFree;
}
