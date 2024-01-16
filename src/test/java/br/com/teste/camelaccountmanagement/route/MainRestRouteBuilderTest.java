package br.com.teste.camelaccountmanagement.route;

import br.com.teste.camelaccountmanagement.CamelAccountManagementApplication;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

@CamelSpringBootTest
@SpringBootTest(classes = {CamelAccountManagementApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
@MockEndpointsAndSkip("^direct:.*")
@DirtiesContext
class MainRestRouteBuilderTest {

    @Autowired
    CamelContext camelContext;

    @EndpointInject(value = "mock:direct:TO_getCustomers")
    private MockEndpoint mockCustomersGETRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_createCustomers")
    private MockEndpoint mockCustomersPOSTRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_getAccounts")
    private MockEndpoint mockAccountsGETRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_createAccounts")
    private MockEndpoint mockAccountPOSTRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_getTransactions")
    private MockEndpoint mockTransactionsGETRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_createTransactions")
    private MockEndpoint mockTransactionPOSTRouteBuilder;

    @EndpointInject(value = "mock:direct:TO_cancelTransactions")
    private MockEndpoint mockTransactionPOSTCancelRouteBuilder;

    @Produce
    private ProducerTemplate template;

    @DirtiesContext
    @Test
    public void testMainRouteCustomersGET() throws Exception {
        mockCustomersGETRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "GET");
        template.sendBodyAndHeaders("http://localhost:8081/account_management/v1/customers", null, headers);

        mockCustomersGETRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteCustomersPOST() throws Exception {
        mockCustomersPOSTRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "POST");
        template.sendBodyAndHeaders("http://localhost:8081/account_management/v1/customers", "", headers);

        mockCustomersPOSTRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteAccountsGET() throws Exception {
        mockAccountsGETRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "GET");
        template.sendBodyAndHeaders(
                "http://localhost:8081/account_management/v1/customers/1/accounts",
                null,
                headers
        );

        mockAccountsGETRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteAccountsPOST() throws Exception {
        mockAccountPOSTRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "POST");
        template.sendBodyAndHeaders(
                "http://localhost:8081/account_management/v1/customers/1/accounts",
                "",
                headers
        );

        mockAccountPOSTRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteTransactionsGET() throws Exception {
        mockTransactionsGETRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "GET");
        template.sendBodyAndHeaders(
                "http://localhost:8081/account_management/v1/customers/1/accounts/1/transactions",
                null,
                headers
        );

        mockTransactionsGETRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteTransactionsPOST() throws Exception {
        mockTransactionPOSTRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "POST");
        template.sendBodyAndHeaders(
                "http://localhost:8081/account_management/v1/customers/1/accounts/1/transactions",
                "",
                headers
        );

        mockTransactionPOSTRouteBuilder.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void testMainRouteTransactionsPOSTCancel() throws Exception {
        mockTransactionPOSTCancelRouteBuilder.expectedMessageCount(1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("CamelHttpMethod", "POST");
        template.sendBodyAndHeaders(
                "http://localhost:8081/account_management/v1/customers/1/accounts/1/transactions/1/cancel",
                null,
                headers
        );

        mockTransactionPOSTCancelRouteBuilder.assertIsSatisfied();
    }
}