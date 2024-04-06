package com.jk.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmArgs {

    // user who occur alarm
    private Integer fromUserId;
    private Integer targetId;
}
