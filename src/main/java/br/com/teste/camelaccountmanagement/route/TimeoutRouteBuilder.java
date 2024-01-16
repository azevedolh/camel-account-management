package br.com.teste.camelaccountmanagement.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TimeoutRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:timeout")
                .routeId("default-timeout-route")
                .setBody(simple(""))
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.TEXT_PLAIN_VALUE))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.GATEWAY_TIMEOUT.value()));
    }
}
