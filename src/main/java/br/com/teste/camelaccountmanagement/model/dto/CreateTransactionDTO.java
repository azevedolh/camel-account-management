package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {
    private Long destinationAccount;
    private BigDecimal amount;
}
