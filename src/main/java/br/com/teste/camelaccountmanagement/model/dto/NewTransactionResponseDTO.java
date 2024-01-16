package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewTransactionResponseDTO {
    private Long id;
    private String type;
    private Long originAccount;
    private Long destinationAccount;
    private BigDecimal amount;
    private String status;
    private List<NotificationResultDTO> notificationResult;
}
