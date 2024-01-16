package br.com.teste.camelaccountmanagement.processor.error;

import br.com.teste.camelaccountmanagement.model.dto.ErrorTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;

public class HttpOperationFailedProcessor implements org.apache.camel.Processor {
    @Autowired
    private ErrorProcessor errorProcessor;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);

            ObjectMapper objectMapper = new ObjectMapper();
            ErrorTemplate error = objectMapper.readValue(exception.getResponseBody(), ErrorTemplate.class);
            exchange.getMessage().setBody(error);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, exception.getStatusCode());
        } catch (JsonProcessingException e) {
            errorProcessor.process(exchange);
        }
    }
}
