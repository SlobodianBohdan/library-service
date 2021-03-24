package org.libraryservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {

    private Long id;

    @NotBlank
    private String name;

}
