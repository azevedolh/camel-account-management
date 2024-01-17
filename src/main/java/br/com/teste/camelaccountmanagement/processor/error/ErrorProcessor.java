package br.com.teste.camelaccountmanagement.processor.error;

import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

public class ErrorProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        exchange.getMessage().setBody(e.getMessage());
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
