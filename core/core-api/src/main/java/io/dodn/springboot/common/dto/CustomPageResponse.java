package io.dodn.springboot.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPageResponse<T> {
    private final List<T> content;          // 데이터 목록
    private final int pageNumber;           // 현재 페이지 번호 (0부터 시작)
    private final int pageSize;             // 페이지 크기
    private final long totalElements;       // 전체 데이터 개수
    private final int totalPages;           // 전체 페이지 수
    private final boolean isLast;           // 마지막 페이지 여부
    private final boolean isFirst;          // 첫 페이지 여부
    private final boolean hasNext;          // 다음 페이지 존재 여부
    private final boolean hasPrevious;      // 이전 페이지 존재 여부
    private final boolean isEmpty;          // 현재 페이지가 비어있는지 여부

    // Page 객체를 받아서 CustomPageResponse 객체로 변환하는 생성자
    public CustomPageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
        this.isFirst = page.isFirst();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isEmpty = page.isEmpty();
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return isLast;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
