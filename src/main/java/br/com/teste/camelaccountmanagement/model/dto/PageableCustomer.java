package br.com.teste.camelaccountmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageableCustomer extends PageableTemplate {
    private List<CustomerResponseDTO> _content;
}
