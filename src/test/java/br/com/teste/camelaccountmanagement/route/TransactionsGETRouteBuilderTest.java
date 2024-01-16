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

class TransactionsGETRouteBuilderTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new TransactionsGETRouteBuilder();
    }

    @Test
    void testTransactionGETRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("getTransactions-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("transactionQueryProcessor").replace().to("mock:queryProcessor");
                weaveById("getTransactionBackend").replace().to("mock:backend");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");
        MockEndpoint mockQueryProcessor = getMockEndpoint("mock:queryProcessor");

        PageableTransaction pageableTransaction = getPageableTransaction();


        mockQueryProcessor.whenAnyExchangeReceived(exchange -> {
            exchange.setProperty("transactionQuery", "");
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(pageableTransaction);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            exchange.getMessage().setBody(jsonResponse);
        });

        Map<String, Object> headers = new HashMap<>();
        headers.put("customerId", 1);
        headers.put("accountId", 1);
        template.sendBodyAndHeaders("direct:TO_getTransactions", "teste", headers);

//      backend tests
        String httpMethod = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
        String path = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_PATH, String.class);

//      result of route tests
        PageableTransaction resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(PageableTransaction.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(pageableTransaction, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.OK.value(), responseCode, "Should be equal");
        assertEquals("GET", httpMethod, "Should be equal");
        assertEquals("/customers/1/accounts/1/transactions", path, "Should be equal");
    }

    private PageableTransaction getPageableTransaction() {
        PageableTemplatePageable pageableTemplatePageable = PageableTemplatePageable.builder()
                ._limit(10)
                ._offset(0L)
                ._pageNumber(1)
                ._pageElements(1)
                ._totalPages(1)
                ._totalElements(1L)
                ._moreElements(false)
                .build();

        TransactionResponseDTO transactionResponseDTO = TransactionResponseDTO.builder()
                .id(1L)
                .type("DEBITO")
                .originAccount(1L)
                .destinationAccount(2L)
                .amount(new BigDecimal(1000))
                .status("EFETIVADO")
                .createdAt("2024-01-15T23:16:06.164099")
                .updatedAt("2024-01-15T23:16:06.164099")
                .build();

        PageableTransaction pageableTransaction = PageableTransaction.builder()
                ._pageable(pageableTemplatePageable)
                ._content(Arrays.asList(transactionResponseDTO))
                .build();
        return pageableTransaction;
    }
}