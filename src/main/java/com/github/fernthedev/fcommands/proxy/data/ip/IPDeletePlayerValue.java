package com.github.fernthedev.fcommands.proxy.data.ip;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.Date;
import java.util.UUID;

@Getter
@Data
public class IPDeletePlayerValue {

    private UUID requester;

    @NonNull
    private Date hideDeleteDate;

    @NonNull
    private Date deleteDate;

    public boolean isHidden() {
        return hideDeleteDate.before(new Date());
    }

}
