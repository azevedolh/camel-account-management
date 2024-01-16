package br.com.teste.camelaccountmanagement.processor;

import br.com.teste.camelaccountmanagement.model.enumerator.PagingFiltersEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class PagingQueryProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String propertyName = exchange.getProperty("propertyName", String.class);

        StringBuilder queryString = new StringBuilder();

        for (PagingFiltersEnum pagingFilters : PagingFiltersEnum.values()) {
            String filterTitle = pagingFilters.getFilter();
            String filterValue = exchange.getIn().getHeader(filterTitle, String.class);

            if (filterValue != null) {
                queryString.append(filterTitle).append('=').append(filterValue).append('&');
            }
        }

        // deletar ultimo caracter "&" caso a string não esteja vazia
        if (!queryString.toString().isEmpty()) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        exchange.setProperty(propertyName, queryString.toString());
    }
}
