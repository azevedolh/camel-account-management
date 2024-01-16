package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.model.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionsPOSTRouteBuilderTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new TransactionsPOSTRouteBuilder();
    }

    @Test
    void testTransactionPOSTRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("createTransactions-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("createTransactionBackend").replace().to("mock:backend");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");

        CreateTransactionDTO createTransactionDTO = getCreateTransaction();
        NewTransactionResponseDTO newTransactionResponseDTO = getNewTransactionResponseDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(createTransactionDTO);
        String jsonResponse = objectMapper.writeValueAsString(newTransactionResponseDTO);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            exchange.getMessage().setBody(jsonResponse);
        });

        Map<String, Object> headers = new HashMap<>();
        headers.put("customerId", 1);
        headers.put("accountId", 1);
        template.sendBodyAndHeaders("direct:TO_createTransactions", createTransactionDTO, headers);

//      backend tests
        String backendRequest = mockEndpoint.getExchanges().get(0).getMessage().getBody(String.class);
        String httpMethod = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
        String path = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_PATH, String.class);

        assertEquals(jsonRequest, backendRequest, "Should be equal");
        assertEquals("POST", httpMethod, "Should be equal");
        assertEquals("/customers/1/accounts/1/transactions", path, "Should be equal");

//      result of route tests
        NewTransactionResponseDTO resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(NewTransactionResponseDTO.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(newTransactionResponseDTO, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.CREATED.value(), responseCode, "Should be equal");

    }

    private NewTransactionResponseDTO getNewTransactionResponseDTO() {
        NotificationResultDTO notificationResultDTO = NotificationResultDTO.builder()
                .accountType("ORIGIN")
                .notificationStatus("SENT")
                .message("teste")
                .build();


        return NewTransactionResponseDTO.builder()
                .type("DEBITO")
                .originAccount(1L)
                .destinationAccount(2L)
                .amount(new BigDecimal(1000))
                .status("EFETIVADO")
                .notificationResult(Arrays.asList(notificationResultDTO))
                .build();
    }

    private CreateTransactionDTO getCreateTransaction() {
        return CreateTransactionDTO.builder()
                .destinationAccount(2L)
                .amount(new BigDecimal(1000))
                .build();
    }
}