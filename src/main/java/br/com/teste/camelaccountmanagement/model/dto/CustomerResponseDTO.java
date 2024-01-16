package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String document;
    private String documentType;
    private String name;
    private String address;
    private String createdAt;
    private String updatedAt;
}
