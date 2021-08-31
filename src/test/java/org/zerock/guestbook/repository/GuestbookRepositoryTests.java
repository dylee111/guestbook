package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

//    @Test
//    public void insertDummies() {
//        IntStream.rangeClosed(1, 300).forEach(i -> {
//            Guestbook guestbook = Guestbook.builder()
//                    .title("Title..." + i)
//                    .content("Content..." + i)
//                    .writer("user" + i % 10)
//                    .build();
//            System.out.println(guestbookRepository.save(guestbook));
//        });
//    } // insertDummies()

//    @Test
//    public void updateTest() {
//
//        Optional<Guestbook> result = guestbookRepository.findById(300L);
//
//        if(result.isPresent()) {
//
//            Guestbook guestbook = result.get();
//
//            guestbook.changeTitle("Changed Title...");
//            guestbook.changeContent("Changed Content...");
//
//            guestbookRepository.save(guestbook);
//        }
//    } // updateTest();

    @Test
    public void testQuery1() {  // 단일 항목 검색
        Pageable pageable = PageRequest.of(1, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //동적 쿼리 처리

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //  Where문에 들어가는 조건을 넣는 컨테이너 역할.

        BooleanExpression expression = qGuestbook.title.contains(keyword); // 원하는 조건은 필드 값과 같이 결합.(ex. Title에 1이 포함되었는지 확인)

        builder.and(expression); // 만들어진 조건을 where문의 and 또는 or 키워드와 결합.

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    } // testQuery1()

    @Test
    public void testQueryMany() { // 다중 항목 검색
        Pageable pageable = PageRequest.of(1,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll =  exTitle.or(exContent); // exTitle 또는 exContent

        booleanBuilder.and(exAll); // exAll을 and와 결합.

        booleanBuilder.and(qGuestbook.gno.gt(0L)); // gt : greater than

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
