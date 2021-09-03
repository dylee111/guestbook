package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service // business Logic을 처리하는 부분임을 의미
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {
    // 반드시 final로 선언.
    // 의존성 주입이 필요한 필드를 final로 선언하여 불변하게 사용. 순환참조를 막기 위함.
    // 생성자를 통하여 Spring Bean을 생성할 때, 순환참조가 발생. 그래서 final을 붙임.
    // Setter 즉, Autowired를 사용할 경우, setter을 호출하는 시점이 생성자보다 불분명.
    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        guestbookRepository.save(entity);
        return entity.getGno();
    } // register()

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        // 화면에 페이지 처리와 필요한 값들을 생성.
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        // JPA 처리 결과인 Page<Entity> 객체 생성.
        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable); // Querydsl 사용
        // JPA로 부터 처리된 결과에 Entity로 DTO로 변환하는 처리를 하는 부분.
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        // 위에서 만든 2자리를 PageResultDTO에 넣으면 fn에 정의된 대로 변환해서 결과 반환함.
        return new PageResultDTO<>(result, fn);
    } // getList()

    @Override
    public GuestbookDTO read(Long gno) {
        // repository(저장소)에서 gno를 기준으로 데이터를 찾고 null인지 Optional로 한 번 더 확인 후에 result에 담음.
        Optional<Guestbook> result = guestbookRepository.findById(gno);
        //result에 현재 값이 있다면(true)  Entity객체(result에 담긴 값)를 DTO로 변환. 값이 없다면 null.
        return result.isPresent() ? entityToDto(result.get()) : null;
    } // read()

    @Override
    public void remove(Long gno) {
        guestbookRepository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());

        if(result.isPresent()) {
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            guestbookRepository.save(entity);
        }
    } // modify()

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) { return booleanBuilder; }

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    } // getSearch()
}
