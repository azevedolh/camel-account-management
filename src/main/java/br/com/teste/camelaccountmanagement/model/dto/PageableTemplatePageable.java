package br.com.teste.camelaccountmanagement.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableTemplatePageable {
    private Integer _limit;
    private Long _offset;
    private Integer _pageNumber;
    private Integer _pageElements;
    private Integer _totalPages;
    private Long _totalElements;
    private Boolean _moreElements;
}
