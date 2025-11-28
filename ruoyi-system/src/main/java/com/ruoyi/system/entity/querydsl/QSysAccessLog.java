package com.ruoyi.system.entity.querydsl;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.ruoyi.system.entity.SysAccessLog;

import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QSysLogininfor is a Querydsl query type for QSysLogininfor
 */
@SuppressWarnings({"this-escape", "unused"})
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSysAccessLog extends com.querydsl.sql.RelationalPathBase<SysAccessLog> {

    public static final QSysAccessLog sysLogininfor = new QSysAccessLog("sys_access_log");

    public final StringPath browser = createString("browser");

    public final NumberPath<Long> logId = createNumber("logId", Long.class);

    public final StringPath ipaddr = createString("ipaddr");

    public final StringPath location = createString("location");

    public final DateTimePath<java.time.LocalDateTime> accessTime = createDateTime("accessTime", java.time.LocalDateTime.class);

    public final StringPath msg = createString("msg");

    public final StringPath os = createString("os");

    public final StringPath status = createString("status");

    public final StringPath userName = createString("userName");

    public final com.querydsl.sql.PrimaryKey<SysAccessLog> primary = createPrimaryKey(logId);

    public QSysAccessLog(String variable) {
        super(SysAccessLog.class, forVariable(variable), "null", "sys_access_log");
        addMetadata();
    }

    public QSysAccessLog(String variable, String schema, String table) {
        super(SysAccessLog.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSysAccessLog(String variable, String schema) {
        super(SysAccessLog.class, forVariable(variable), schema, "sys_access_log");
        addMetadata();
    }

    public QSysAccessLog(Path<? extends SysAccessLog> path) {
        super(path.getType(), path.getMetadata(), "null", "sys_access_log");
        addMetadata();
    }

    public QSysAccessLog(PathMetadata metadata) {
        super(SysAccessLog.class, metadata, "null", "sys_access_log");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(browser, ColumnMetadata.named("browser").withIndex(5).ofType(Types.VARCHAR).withSize(50));
        addMetadata(logId, ColumnMetadata.named("log_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(ipaddr, ColumnMetadata.named("ipaddr").withIndex(3).ofType(Types.VARCHAR).withSize(128));
        addMetadata(location, ColumnMetadata.named("location").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(accessTime, ColumnMetadata.named("access_time").withIndex(9).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(msg, ColumnMetadata.named("msg").withIndex(8).ofType(Types.VARCHAR).withSize(255));
        addMetadata(os, ColumnMetadata.named("os").withIndex(6).ofType(Types.VARCHAR).withSize(50));
        addMetadata(status, ColumnMetadata.named("status").withIndex(7).ofType(Types.CHAR).withSize(1));
        addMetadata(userName, ColumnMetadata.named("user_name").withIndex(2).ofType(Types.VARCHAR).withSize(50));
    }

}

