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

import java.util.Arrays;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomersPOSTRouteBuilderTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CustomersPOSTRouteBuilder();
    }

    @Test
    void testCustomerPOSTRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("createCustomers-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("createCustomerBackend").replace().to("mock:backend");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");

        CreateCustomerDTO createCustomer = getCreateCustomer();
        PostResponseDTO postResponseDTO = PostResponseDTO.builder().id(1L).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(createCustomer);
        String jsonResponse = objectMapper.writeValueAsString(postResponseDTO);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            exchange.getMessage().setBody(jsonResponse);
        });

        template.sendBody("direct:TO_createCustomers", createCustomer);

//      backend tests
        String backendRequest = mockEndpoint.getExchanges().get(0).getMessage().getBody(String.class);
        String httpMethod = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
        String path = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_PATH, String.class);

        assertEquals(jsonRequest, backendRequest, "Should be equal");
        assertEquals("POST", httpMethod, "Should be equal");
        assertEquals("/customers", path, "Should be equal");

//      result of route tests
        PostResponseDTO resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(PostResponseDTO.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(postResponseDTO, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.CREATED.value(), responseCode, "Should be equal");

    }

    private CreateCustomerDTO getCreateCustomer() {
        return CreateCustomerDTO.builder()
                .name("teste da silva")
                .document("123456789")
                .documentType("PF")
                .address("rua do teste")
                .password("123456")
                .build();
    }
}