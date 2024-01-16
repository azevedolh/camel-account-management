package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.model.dto.CustomerResponseDTO;
import br.com.teste.camelaccountmanagement.model.dto.PageableCustomer;
import br.com.teste.camelaccountmanagement.model.dto.PageableTemplatePageable;
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

class CustomersGETRouteBuilderTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CustomersGETRouteBuilder();
    }

    @Test
    void testCustomerGETRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("getCustomers-service");

        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("customerQueryProcessor").replace().to("mock:queryProcessor");
                weaveById("getCustomerBackend").replace().to("mock:backend");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:backend");
        MockEndpoint mockResult = getMockEndpoint("mock:result");
        MockEndpoint mockQueryProcessor = getMockEndpoint("mock:queryProcessor");

        PageableCustomer pageableCustomer = getPageableCustomer();

        mockQueryProcessor.whenAnyExchangeReceived(exchange -> {
            exchange.setProperty("customerQuery", "");
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(pageableCustomer);

        mockEndpoint.whenAnyExchangeReceived(exchange -> {
            exchange.getMessage().setBody(jsonResponse);
        });

        template.sendBody("direct:TO_getCustomers", "teste");

//      backend tests
        String httpMethod = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
        String path = mockEndpoint.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_PATH, String.class);

//      result of route tests
        PageableCustomer resultResponse = mockResult.getExchanges().get(0).getMessage().getBody(PageableCustomer.class);
        String contentType = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.CONTENT_TYPE, String.class);
        Integer responseCode = mockResult.getExchanges().get(0).getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);

        assertEquals(pageableCustomer, resultResponse, "Objects should be equal");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType, "Should be equal");
        assertEquals(HttpStatus.OK.value(), responseCode, "Should be equal");
        assertEquals("GET", httpMethod, "Should be equal");
        assertEquals("/customers", path, "Should be equal");
    }

    private PageableCustomer getPageableCustomer() {
        PageableTemplatePageable pageableTemplatePageable = PageableTemplatePageable.builder()
                ._limit(10)
                ._offset(0L)
                ._pageNumber(1)
                ._pageElements(1)
                ._totalPages(1)
                ._totalElements(1L)
                ._moreElements(false)
                .build();

        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .document("123456789")
                .documentType("PF")
                .name("teste da silva")
                .address("rua do teste")
                .createdAt("2024-01-15T23:16:06.164099")
                .updatedAt("2024-01-15T23:16:06.164099")
                .build();

        PageableCustomer pageableCustomer = PageableCustomer.builder()
                ._pageable(pageableTemplatePageable)
                ._content(Arrays.asList(customerResponseDTO))
                .build();
        return pageableCustomer;
    }
}