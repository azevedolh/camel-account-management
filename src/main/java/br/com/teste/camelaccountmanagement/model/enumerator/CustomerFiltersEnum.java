package br.com.teste.camelaccountmanagement.model.enumerator;

public enum CustomerFiltersEnum {
    PAGE("page"),
    SIZE("size"),
    SORT("_sort"),
    NAME("name"),
    DOCUMENT("document");

    private final String filter;

    CustomerFiltersEnum(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}
