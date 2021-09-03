package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTests {

    @Autowired
    private GuestbookService guestbookService;

    @Test
    public void register() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title.....")
                .content("Sample Content.....")
                .writer("user0")
                .build();
        System.out.println("result = " + guestbookService.register(guestbookDTO));
    } // register()

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

    } // testList()

    @Test
    public void testPageNumberList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1).size(10).build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        System.out.println("PREV >> " + resultDTO.isPrev());
        System.out.println("NEXT >> " + resultDTO.isNext());
        System.out.println("TOTAL>> " + resultDTO.getTotalPage());
        System.out.println("=====================================");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
        System.out.println("Page Number =====================================");

        resultDTO.getPageList().forEach(i -> {System.out.println(i);} );
    } // testPageNumberList()

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")     // 검색 조건 : t(title) , c(content), w(writer) .....
                .keyword("한글") // 검색 키워드
                .build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);

        System.out.println("PREV  : " + resultDTO.isPrev());
        System.out.println("NEXT  : " + resultDTO.isNext());
        System.out.println("TOTAL : " + resultDTO.getTotalPage());

        System.out.println("=======================================");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }
        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> {
            System.out.println(i);
        });
    }


}