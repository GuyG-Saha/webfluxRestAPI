package com.reactive.webflux2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@Document
public class CreditCard {
    @Id
    private String id;
    @NotBlank(message = "CCN must be present")
    @Size(min = 16, max = 16, message = "CCN length must be 16")
    private String CCN;
    @Pattern(regexp = "^(0?[1-9]|1[012])$", message = "Month must be between 01 and 12")
    @NotBlank(message = "Expiration month must be present")
    private String expMonth;
    @NotNull
    @Min(value = 2023, message = "Earliest expiration year is 2023")
    @Max(value = 2099, message = "Latest expiration year is 2099")
    private Integer expYear;
    private String processStatus;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CCN.length(); i++) {
            if (i < 6 || i > 11)
                sb.append(CCN.charAt(i));
            else
                sb.append('*');
        }
        return sb.toString();
    }

}
