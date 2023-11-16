package com.example.CRM.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        setContent(content);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public PagedResponse<T> returnPagedResponse(Page<T> resource){
        if(resource.getNumberOfElements() == 0){
            return new PagedResponse<>(List.of(), resource.getNumber() + 1, resource.getSize(),
                    resource.getTotalElements(), resource.getTotalPages(), resource.isLast());
        }

        List<T> subscriptionsList = resource.getContent();

        return new PagedResponse<>(subscriptionsList, resource.getNumber() + 1, resource.getSize(), resource.getTotalElements(),
                resource.getTotalPages(), resource.isLast());
    }
}