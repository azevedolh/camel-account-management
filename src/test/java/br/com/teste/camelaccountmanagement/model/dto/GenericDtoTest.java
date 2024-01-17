package br.com.teste.camelaccountmanagement.model.dto;

import br.com.teste.camelaccountmanagement.util.DTOTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GenericDtoTest {

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassAccountResponseDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(AccountResponseDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassCancelTransactionDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(CancelTransactionDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassCreateAccountDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(CreateAccountDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassCreateCustomerDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(CreateCustomerDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassCreateTransactionDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(CreateTransactionDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassCustomerResponseDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(CustomerResponseDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassErrorTemplate() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(ErrorTemplate.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassNewTransactionResponseDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(NewTransactionResponseDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassNewNotificationResultDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(NotificationResultDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassPageableTemplatePageable() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(PageableTemplatePageable.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassPostResponseDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(PostResponseDTO.class),
                "cannot throw exception");
    }

    @Test
    public void shouldExecuteGetterAndSetterConstructorsWithClassTransactionResponseDTO() {
        assertDoesNotThrow(() -> DTOTester.executeGetterSetterConstructor(TransactionResponseDTO.class),
                "cannot throw exception");
    }
}
