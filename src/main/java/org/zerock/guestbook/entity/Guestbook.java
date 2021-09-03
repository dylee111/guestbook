package org.zerock.guestbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder // @Bulider와 @AllArgsConstructor, NoArgsConstructor는 하나의 세트로 생각할 것.
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity {

    @Id // Primary Key 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성.
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
