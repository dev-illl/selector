package com.selector.settings;

import com.selector.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max = 35)
    private String introduce;

    @Length(max = 50)
    private String instagramUrl;

    @Length(max = 50)
    private String occupation;

    @Length(max = 50)
    private String interest;

    private String profileImage;

    public Profile(Account account) {
        this.introduce = account.getIntroduce();
        this.occupation = account.getOccupation();
        this.interest = account.getInterest();
        this.instagramUrl = account.getInstagramUrl();
        this.profileImage = account.getProfileImage();
    }
}
