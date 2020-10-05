package ru.vs4.okornilov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private String userName;
    private String password;
    private String email;
    private boolean inBlackList;
    private boolean validPassword;
}
