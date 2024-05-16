package rw.ac.rca.spring_boot_template.dtos.responses;

import rw.ac.rca.spring_boot_template.enumerations.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Response<T> {
    private ResponseType type;
    private T payload;
}
