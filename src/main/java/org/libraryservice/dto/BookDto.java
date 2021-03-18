package org.libraryservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private boolean isFree;
}
