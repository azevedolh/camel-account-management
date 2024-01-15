package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {
    private String agency;
    private BigDecimal balance;
}
