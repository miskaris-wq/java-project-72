package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class BasePage {
    private String flash;
    private String info;
    private String error;

}
