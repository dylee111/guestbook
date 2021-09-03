package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 Annotation
public class GuestbookController {


    private final GuestbookService service;

    @GetMapping({"/",""})
    public String index() {
        return "redirect:/guestbook/list";
    }

    // void : 요청된 url이 리소스로 바로 감.
    @GetMapping({"/list"})
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list......." + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
        log.info("RegisterPost...");
        // 새로 추가된 엔티티 번호.
        Long gno = service.register(dto);
        // addFlashAttribute : 1회성으로 데이터를 전달하는 용도로 사용.
        redirectAttributes.addFlashAttribute("msg",gno);
        redirectAttributes.addFlashAttribute("noti", "등록");

        // redirect 시에 해당 URL로 이동
        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("gno : " + gno);
        // read() : service 인터페이스에서 선언되고 serviceImpl에서 구현된 메서드
        GuestbookDTO dto = service.read(gno);
        // "dto"라는 이름으로 dto에 저장된 값을 넘김.
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, RedirectAttributes redirectAttributes,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO) {

        service.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";
    }

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes) {
        log.info("gno : " + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        redirectAttributes.addFlashAttribute("noti", "삭제");
        return "redirect:/guestbook/list";
    }


}
