package br.com.teste.camelaccountmanagement.model.enumerator;

public enum PagingFiltersEnum {
    PAGE("page"),
    SIZE("size"),
    SORT("_sort");

    private final String filter;

    PagingFiltersEnum(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}
