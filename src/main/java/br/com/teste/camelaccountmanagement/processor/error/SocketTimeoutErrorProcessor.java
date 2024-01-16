package br.com.teste.camelaccountmanagement.processor.error;

import org.apache.camel.Exchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class SocketTimeoutErrorProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody("");
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.REQUEST_TIMEOUT.value());
    }
}
