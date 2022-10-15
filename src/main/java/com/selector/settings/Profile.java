package com.selector.settings;

import com.selector.domain.Account;
import lombok.Data;

@Data
public class Profile {

    private String introduce;

    private String occupation;

    private String interest;

    private String instagramUrl;

    public Profile(Account account) {
        this.introduce = account.getIntroduce();
        this.occupation = account.getOccupation();
        this.interest = account.getInterest();
        this.instagramUrl = account.getInstagramUrl();
    }
}
