package com.azercell.banking.bffweb.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int totalPages;
    private int totalElements;
    private int number;
    private int size;
    private boolean first;
    private boolean last;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageResponse)) {
            return false;
        }
        PageResponse<?> that = (PageResponse<?>) o;
        return getTotalPages() == that.getTotalPages() && getTotalElements() == that.getTotalElements()
                && getNumber() == that.getNumber() && getSize() == that.getSize() && isFirst() == that.isFirst()
                && isLast() == that.isLast() && getContent().equals(that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent(), getTotalPages(), getTotalElements(), getNumber(), getSize(), isFirst(),
                isLast());
    }

}
