package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.model.dto.CreateTransactionDTO;
import br.com.teste.camelaccountmanagement.model.dto.ErrorTemplate;
import br.com.teste.camelaccountmanagement.model.dto.NewTransactionResponseDTO;
import br.com.teste.camelaccountmanagement.processor.error.ErrorProcessor;
import br.com.teste.camelaccountmanagement.processor.error.HttpOperationFailedProcessor;
import br.com.teste.camelaccountmanagement.processor.error.SocketTimeoutErrorProcessor;
import io.swagger.models.HttpMethod;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Component
public class TransactionsPOSTRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:TO_createTransactions")
                .routeId("createTransactions")
                .circuitBreaker()
                    .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(5000).end()
                    .to("direct:TO_createTransactions-SERVICE")
                .onFallback()
                    .to("direct:timeout")
        .end();

        from("direct:TO_createTransactions-SERVICE")
                .routeId("createTransactions-service")
                .doTry()

                    .removeHeaders("CamelHttp*")
                    .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST.toString()))
                    .setHeader(Exchange.HTTP_PATH,
                            simple("/customers/${header.customerId}/accounts/${header.accountId}/transactions"))

                    .marshal().json(JsonLibrary.Jackson, CreateTransactionDTO.class)

                    .to("{{backend.url}}?socketTimeout=1000")
                    .id("createTransactionBackend")

                    .unmarshal().json(JsonLibrary.Jackson, NewTransactionResponseDTO.class)
                    .removeHeaders("CamelHttp*")
                    .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON_VALUE))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.CREATED.value()))

                .doCatch(SocketTimeoutException.class)
                    .process(new SocketTimeoutErrorProcessor())
                    .stop()
                .doCatch(HttpOperationFailedException.class)
                    .process(new HttpOperationFailedProcessor())
                    .marshal().json(JsonLibrary.Jackson, ErrorTemplate.class)
                    .stop()
                .doCatch(Exception.class)
                    .process(new ErrorProcessor())
                .endDoTry();
    }
}
