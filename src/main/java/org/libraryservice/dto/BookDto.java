package org.libraryservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private boolean isFree;
}
