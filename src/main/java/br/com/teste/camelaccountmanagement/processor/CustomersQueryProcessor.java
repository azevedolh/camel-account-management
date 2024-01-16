package br.com.teste.camelaccountmanagement.processor;

import br.com.teste.camelaccountmanagement.model.enumerator.CustomerFiltersEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class CustomersQueryProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        StringBuilder queryString = new StringBuilder();

        for (CustomerFiltersEnum customerFilters : CustomerFiltersEnum.values()) {
            String filterTitle = customerFilters.getFilter();
            String filterValue = exchange.getIn().getHeader(filterTitle, String.class);

            if (filterValue != null) {
                queryString.append(filterTitle).append('=').append(filterValue).append('&');
            }
        }

        // deletar ultimo caracter "&" caso a string não esteja vazia
        if (!queryString.toString().isEmpty()) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        exchange.setProperty("customerQuery", queryString.toString());
    }
}
