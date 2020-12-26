package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniversalMysql {

    @Setter
    @Getter
    private static DatabaseListener databaseManager;

}
