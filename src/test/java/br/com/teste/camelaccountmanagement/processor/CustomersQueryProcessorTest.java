package br.com.teste.camelaccountmanagement.processor;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomersQueryProcessorTest {
    private CustomersQueryProcessor processor;
    private Exchange exchange;

    @BeforeEach
    public void beforeEach() {
        this.processor = new CustomersQueryProcessor();
        DefaultCamelContext context = new DefaultCamelContext();
        this.exchange = new DefaultExchange(context);
    }

    @Test
    void testeShouldMountQueryParamsWhenTheyAreInformed() throws Exception {

        exchange.getMessage().setHeader("page", "1");
        exchange.getMessage().setHeader("size", "10");
        exchange.getMessage().setHeader("_sort", "createdBy,desc");
        exchange.getMessage().setHeader("name", "teste");
        exchange.getMessage().setHeader("document", "123456789");

        processor.process(exchange);

        String expectedQueryParams = "page=1&size=10&_sort=createdBy,desc&name=teste&document=123456789";

        assertEquals(expectedQueryParams, exchange.getProperty("customerQuery"), "Should be equal");
    }

    @Test
    void testeShouldReturnEmptyStringWhenQueryParamsNotInformed() throws Exception {

        processor.process(exchange);

        assertEquals("", exchange.getProperty("customerQuery"), "Should be equal");
    }
}