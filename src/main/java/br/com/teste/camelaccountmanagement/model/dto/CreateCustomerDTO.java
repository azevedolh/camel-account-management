package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDTO {
    private String name;
    private String document;
    private String documentType;
    private String address;
    private String password;
}
