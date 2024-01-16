package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String type;
    private Long originAccount;
    private Long destinationAccount;
    private BigDecimal amount;
    private String status;
    private String createdAt;
    private String updatedAt;
}
