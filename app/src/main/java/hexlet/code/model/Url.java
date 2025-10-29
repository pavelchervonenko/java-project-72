package hexlet.code.model;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(String name) {
        this.name = name;
    }
}
