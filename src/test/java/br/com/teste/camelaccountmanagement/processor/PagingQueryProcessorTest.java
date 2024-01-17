package br.com.teste.camelaccountmanagement.processor;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagingQueryProcessorTest {
    private PagingQueryProcessor processor;
    private Exchange exchange;

    @BeforeEach
    public void beforeEach() {
        this.processor = new PagingQueryProcessor();
        DefaultCamelContext context = new DefaultCamelContext();
        this.exchange = new DefaultExchange(context);
    }

    @Test
    void testeShouldMountQueryParamsWhenTheyAreInformed() throws Exception {

        exchange.setProperty("propertyName", "testeQuery");
        exchange.getMessage().setHeader("page", "1");
        exchange.getMessage().setHeader("size", "10");
        exchange.getMessage().setHeader("_sort", "createdBy,desc");

        processor.process(exchange);

        String expectedQueryParams = "page=1&size=10&_sort=createdBy,desc";

        assertEquals(expectedQueryParams, exchange.getProperty("testeQuery"), "Should be equal");
    }

    @Test
    void testeShouldReturnEmptyStringWhenQueryParamsNotInformed() throws Exception {

        exchange.setProperty("propertyName", "testeQuery");

        processor.process(exchange);

        assertEquals("", exchange.getProperty("testeQuery"), "Should be equal");
    }
}