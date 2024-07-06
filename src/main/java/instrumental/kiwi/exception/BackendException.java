package instrumental.kiwi.exception;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BackendException extends RuntimeException{

    private MsgCode msgCode;
}