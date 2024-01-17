package br.com.teste.camelaccountmanagement.processor.error;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorProcessorTest {
    private ErrorProcessor processor;
    private Exchange exchange;

    @BeforeEach
    public void beforeEach() {
        this.processor = new ErrorProcessor();
        DefaultCamelContext context = new DefaultCamelContext();
        this.exchange = new DefaultExchange(context);
    }

    @Test
    void testeShouldMountQueryParamsWhenTheyAreInformed() throws Exception {
        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception("teste de erro"));

        processor.process(exchange);

        assertEquals(MediaType.TEXT_PLAIN_VALUE,
                exchange.getMessage().getHeader(Exchange.CONTENT_TYPE, String.class),
                "Should be equal");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class),
                "Should be equal");
        assertEquals("teste de erro", exchange.getMessage().getBody(String.class),
                "Should be equal");
    }
}