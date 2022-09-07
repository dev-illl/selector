package com.selector.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickName;

    private String password;

    private boolean emailVerified;

    private boolean emailCheckToken;

    private LocalDateTime joinedAt;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean updatedMyQuestionsByEmail;

    private boolean updatedMyQuestionsByWeb;

    private boolean commentedMyQuestionsByEmail;

    private boolean commentedMyQuestionsByWeb;

    private String test3;
}
