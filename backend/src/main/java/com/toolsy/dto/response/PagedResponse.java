package com.toolsy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Odpowiedź paginowana")
public class PagedResponse<T> {
    @Schema(description = "Lista elementów na stronie")
    private List<T> content;
    @Schema(description = "Numer bieżącej strony (0-indexed)", example = "0")
    private int page;
    @Schema(description = "Rozmiar strony", example = "10")
    private int size;
    @Schema(description = "Całkowita liczba elementów", example = "100")
    private long totalElements;
    @Schema(description = "Całkowita liczba stron", example = "10")
    private int totalPages;
    @Schema(description = "Czy to pierwsza strona", example = "true")
    private boolean first;
    @Schema(description = "Czy to ostatnia strona", example = "false")
    private boolean last;

    public PagedResponse() {
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}

