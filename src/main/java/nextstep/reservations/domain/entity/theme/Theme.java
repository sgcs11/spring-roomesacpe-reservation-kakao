package nextstep.reservations.domain.entity.theme;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Theme {
    private Long id;

    private String name;

    private String desc;

    private Integer price;
}