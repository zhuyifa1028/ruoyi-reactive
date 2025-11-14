package com.ruoyi.system.entity.querydsl;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ColumnMetadata;

import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysUserRole is a Querydsl query type for QSysUserRole
 */
@SuppressWarnings({"this-escape", "unused"})
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSysUserRole extends com.querydsl.sql.RelationalPathBase<QSysUserRole> {

    public static final QSysUserRole sysUserRole = new QSysUserRole("sys_user_role");

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<QSysUserRole> primary = createPrimaryKey(roleId, userId);

    public QSysUserRole(String variable) {
        super(QSysUserRole.class, forVariable(variable), "null", "sys_user_role");
        addMetadata();
    }

    public QSysUserRole(String variable, String schema, String table) {
        super(QSysUserRole.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSysUserRole(String variable, String schema) {
        super(QSysUserRole.class, forVariable(variable), schema, "sys_user_role");
        addMetadata();
    }

    public QSysUserRole(Path<? extends QSysUserRole> path) {
        super(path.getType(), path.getMetadata(), "null", "sys_user_role");
        addMetadata();
    }

    public QSysUserRole(PathMetadata metadata) {
        super(QSysUserRole.class, metadata, "null", "sys_user_role");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(roleId, ColumnMetadata.named("role_id").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

