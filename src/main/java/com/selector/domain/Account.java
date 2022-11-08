package com.selector.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private String introduce;

    private String occupation;

    private String interest;

    private String instagramUrl;

    private boolean topicOfInterestByEmail;

    private boolean topicOfInterestByWeb = true;

    private boolean updatedMyQuestionsByEmail;

    private boolean updatedMyQuestionsByWeb = true;

    private boolean commentedMyQuestionsByEmail;

    private boolean commentedMyQuestionsByWeb = true;

    public void generateEmailCheckToken() {

        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();

    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail(){
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}
