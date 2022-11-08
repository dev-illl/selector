package com.selector.settings;

import com.selector.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Notifications {

    private boolean topicOfInterestByEmail;

    private boolean topicOfInterestByWeb;

    private boolean updatedMyQuestionsByEmail;

    private boolean updatedMyQuestionsByWeb;

    private boolean commentedMyQuestionsByEmail;

    private boolean commentedMyQuestionsByWeb;

}
