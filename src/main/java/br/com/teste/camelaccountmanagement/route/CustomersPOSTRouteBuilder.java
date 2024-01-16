package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.model.dto.CreateCustomerDTO;
import br.com.teste.camelaccountmanagement.model.dto.ErrorTemplate;
import br.com.teste.camelaccountmanagement.model.dto.PageableCustomer;
import br.com.teste.camelaccountmanagement.model.dto.PostResponseDTO;
import br.com.teste.camelaccountmanagement.processor.CustomersQueryProcessor;
import br.com.teste.camelaccountmanagement.processor.error.ErrorProcessor;
import br.com.teste.camelaccountmanagement.processor.error.HttpOperationFailedProcessor;
import br.com.teste.camelaccountmanagement.processor.error.SocketTimeoutErrorProcessor;
import io.swagger.models.HttpMethod;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Component
public class CustomersPOSTRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:TO_createCustomers")
                .routeId("createCustomers")
                .circuitBreaker()
                    .resilience4jConfiguration().timeoutEnabled(true).timeoutDuration(5000).end()
                    .to("direct:TO_createCustomers-SERVICE")
                .onFallback()
                    .to("direct:timeout")
        .end();

        from("direct:TO_createCustomers-SERVICE")
                .routeId("createCustomers-service")
                .doTry()

                    .removeHeaders("CamelHttp*")
                    .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST.toString()))
                    .setHeader(Exchange.HTTP_PATH, simple("/customers"))

                    .marshal().json(JsonLibrary.Jackson, CreateCustomerDTO.class)

                    .to("{{backend.url}}?socketTimeout=1000")
                    .id("createCustomerBackend")

                    .unmarshal().json(JsonLibrary.Jackson, PostResponseDTO.class)
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
