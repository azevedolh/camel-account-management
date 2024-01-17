package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.model.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountsGETRouteBuilderTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new AccountsGETRouteBuilder();
    }

    @Test
    void testAccountGETRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("getAccounts-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("pagingQueryProcessor").replace().to("mock:queryProcessor");
                weaveById("getAccountsBackend").replace().to("mock:backend");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");
        MockEndpoint mockQueryProcessor = getMockEndpoint("mock:queryProcessor");

        PageableAccount pageableAccount = getPageableAccount();


        mockQueryProcessor.whenAnyExchangeReceived(exchange -> {
            exchange.setProperty("accountQuery", "");
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(pageableAccount);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            exchange.getMessage().setBody(jsonResponse);
        });

        template.sendBodyAndHeader("direct:TO_getAccounts", "teste", "customerId", 1);

//      backend tests
        String httpMethod = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
        String path = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_PATH, String.class);
        assertEquals("GET", httpMethod, "Should be equal");
        assertEquals("/customers/1/accounts", path, "Should be equal");

//      result of route tests
        PageableAccount resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(PageableAccount.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(pageableAccount, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.OK.value(), responseCode, "Should be equal");
    }

    private PageableAccount getPageableAccount() {
        PageableTemplatePageable pageableTemplatePageable = PageableTemplatePageable.builder()
                ._limit(10)
                ._offset(0L)
                ._pageNumber(1)
                ._pageElements(1)
                ._totalPages(1)
                ._totalElements(1L)
                ._moreElements(false)
                .build();

        AccountResponseDTO accountResponseDTO = AccountResponseDTO.builder()
                .id(1L)
                .agency("1234")
                .balance(new BigDecimal(1343.25))
                .createdAt("2024-01-15T23:16:06.164099")
                .updatedAt("2024-01-15T23:16:06.164099")
                .isActive(Boolean.TRUE)
                .build();

        PageableAccount pageableAccount = PageableAccount.builder()
                ._pageable(pageableTemplatePageable)
                ._content(Arrays.asList(accountResponseDTO))
                .build();
        return pageableAccount;
    }

//  Exemplo teste negativo, seria replicado para todos os endpoints:
    @Test
    void testShouldThrowExceptionWhenBackendReturnsError() throws Exception {
        RouteDefinition route = context.getRouteDefinition("getAccounts-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("pagingQueryProcessor").replace().to("mock:queryProcessor");
                weaveById("getAccountsBackend").replace().to("mock:backend");
                weaveByToString(".*stop()").before().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");
        MockEndpoint mockQueryProcessor = getMockEndpoint("mock:queryProcessor");

        ErrorTemplate errorTemplate = getBackendErrorReturn();

        mockQueryProcessor.whenAnyExchangeReceived(exchange -> {
            exchange.setProperty("accountQuery", "");
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorTemplate = objectMapper.writeValueAsString(errorTemplate);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            throw new HttpOperationFailedException(
                    "http://teste",
                    HttpStatus.BAD_REQUEST.value(),
                    "BAD REQUEST",
                    "http://teste",
                    null,
                    jsonErrorTemplate);
        });

        template.sendBodyAndHeader("direct:TO_getAccounts", "teste", "customerId", 1);

//      result of route tests
        String resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(String.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(jsonErrorTemplate, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseCode, "Should be equal");
    }

    private ErrorTemplate getBackendErrorReturn() {
        return ErrorTemplate.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.toString())
                .errorCode(1000)
                .message("Erro no backend")
                .title("ERROR")
                .details("Detalhes do erro no backend")
                .build();
    }
}