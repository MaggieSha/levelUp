package com.makingscience.levelupproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class WaitinListNotification {

    private Long waitingListId;
    private String branchName;
    private LocalDate preferredTime;
    private String targetEmail;


}
