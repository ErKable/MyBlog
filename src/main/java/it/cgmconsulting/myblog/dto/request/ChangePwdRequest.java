package it.cgmconsulting.myblog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePwdRequest(
        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
                message="La password può contenere solo caratteri maiuscoli e minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri")
        String oldPwd,
        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
                message="La password può contenere solo caratteri maiuscoli e minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri")
        String newPwd,
        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
                message="La password può contenere solo caratteri maiuscoli e minuscoli e numeri. La lunghezza deve essere compresa tra 6 e 10 caratteri")
        String newPwd2
) {
}
