package com.github.fernthedev.fcommands.universal.mysql.nick;

import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;
import lombok.Getter;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import java.util.UUID;

public class NickDatabaseInfo extends TableInfo<NickDatabaseInfo.NickDatabaseRowInfo> {

    public NickDatabaseInfo() {
        super("fern_nicks", NickDatabaseRowInfo.class, NickDatabaseRowInfo::new);
    }

    @Getter
    public static class NickDatabaseRowInfo extends RowData {
        @PrimaryKey
        @Column("PLAYERUUID")
        private UUID uuid;

        @Column("NICK")
        private String nick;

        public NickDatabaseRowInfo(UUID uuid, String nick) {
            super();
            this.uuid = uuid;
            this.nick = nick;
            initiateRowData();
        }

        public NickDatabaseRowInfo() {
            super();
        }
    }
}
