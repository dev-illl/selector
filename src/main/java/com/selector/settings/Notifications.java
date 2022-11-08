package com.selector.settings;

import com.selector.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {

    private boolean topicOfInterestByEmail;

    private boolean topicOfInterestByWeb;

    private boolean updatedMyQuestionsByEmail;

    private boolean updatedMyQuestionsByWeb;

    private boolean commentedMyQuestionsByEmail;

    private boolean commentedMyQuestionsByWeb;

    public Notifications(Account account){
        this.topicOfInterestByEmail = account.isTopicOfInterestByEmail();
        this.topicOfInterestByWeb = account.isTopicOfInterestByWeb();
        this.updatedMyQuestionsByEmail = account.isUpdatedMyQuestionsByEmail();
        this.updatedMyQuestionsByWeb = account.isUpdatedMyQuestionsByWeb();
        this.commentedMyQuestionsByEmail = account.isCommentedMyQuestionsByEmail();
        this.commentedMyQuestionsByWeb = account.isCommentedMyQuestionsByWeb();
    }
}
