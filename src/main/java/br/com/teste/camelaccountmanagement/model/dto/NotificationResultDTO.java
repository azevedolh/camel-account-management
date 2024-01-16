package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResultDTO {
    private String accountType;
    private String notificationStatus;
    private String message;
}
