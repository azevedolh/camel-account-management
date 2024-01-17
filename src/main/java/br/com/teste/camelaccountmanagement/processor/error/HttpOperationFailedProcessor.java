package br.com.teste.camelaccountmanagement.processor.error;

import br.com.teste.camelaccountmanagement.config.ObjectMapperConfig;
import br.com.teste.camelaccountmanagement.model.dto.ErrorTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.camel.Exchange;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Log4j2
public class HttpOperationFailedProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            HttpOperationFailedException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);

            ObjectMapper objectMapper = ObjectMapperConfig.getInstance();
            ErrorTemplate error = objectMapper.readValue(exception.getResponseBody(), ErrorTemplate.class);
            exchange.getMessage().setBody(error);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, exception.getStatusCode());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing HttpOperationFailedException object");
            ErrorTemplate error = ErrorTemplate.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .errorCode(9000)
                    .title("ERROR")
                    .message("Error deserializing HttpOperationFailedException object")
                    .details(e.getLocalizedMessage())
                    .build();
            exchange.getMessage().setBody(error);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
    }
}
