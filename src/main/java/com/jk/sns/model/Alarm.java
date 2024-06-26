package com.jk.sns.model;

import com.jk.sns.model.entity.AlarmEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Alarm {

    private Integer id = null;

    private User user;

    private AlarmType alarmType;

    private AlarmArgs args;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public String getAlarmText() {
        return alarmType.getAlarmText();
    }

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
            entity.getId(),
            User.fromEntity(entity.getUser()),
            entity.getAlarmType(),
            entity.getArgs(),
            entity.getRegisteredAt(),
            entity.getUpdatedAt(),
            entity.getRemovedAt()
        );
    }
}
