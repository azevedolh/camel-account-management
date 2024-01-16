package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String agency;
    private BigDecimal balance;
    private String createdAt;
    private String updatedAt;
    private Boolean isActive;
}
