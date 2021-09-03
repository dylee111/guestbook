package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 화면에서 필요한 결과를 담당.
@Data
// 제네릭 DTO, EN - DTO와 ENTITY 타입을 의미, 다양한 곳에서 사용이 가능함.
public class PageResultDTO<DTO, EN> {
    // DTO 리스트
    private List<DTO> dtoList;
    // 총 페이지
    private int totalPage;
    // 현재 페이지
    private int page;
    // 목록 사이즈
    private int size;
    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;
    //이전, 다음
    private boolean prev, next;
    // 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    } // 생성자

    private void makePageList(Pageable pageable) {
        page = pageable.getPageNumber() + 1; // 0부터 시작하기 때문에 1 추가
        size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        // tempEnd가 총 페이지부터 크면 tempEnd를 출력.
        // ex. 31페이지가 마지막일 경우, 31 페이지 이후로는 출력 X.
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        // 페이지 번호를 담는 리스트
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    } // makePageList()
}
