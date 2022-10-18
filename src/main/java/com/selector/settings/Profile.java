package com.selector.settings;

import com.selector.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {

    private String introduce;

    private String instagramUrl;

    private String occupation;

    private String interest;

    public Profile(Account account) {
        this.introduce = account.getIntroduce();
        this.occupation = account.getOccupation();
        this.interest = account.getInterest();
        this.instagramUrl = account.getInstagramUrl();
    }
}
