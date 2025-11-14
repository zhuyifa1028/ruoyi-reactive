package com.ruoyi.system.entity.querydsl;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.ruoyi.system.entity.SysDict;

import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysDictData is a Querydsl query type for QSysDictData
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSysDict extends com.querydsl.sql.RelationalPathBase<SysDict> {

    public static final QSysDict sysDict = new QSysDict("sys_dict_data");

    public final StringPath createBy = createString("createBy");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final StringPath cssClass = createString("cssClass");

    public final NumberPath<Long> dictCode = createNumber("dictCode", Long.class);

    public final StringPath dictLabel = createString("dictLabel");

    public final NumberPath<Long> dictSort = createNumber("dictSort", Long.class);

    public final StringPath dictType = createString("dictType");

    public final StringPath dictValue = createString("dictValue");

    public final StringPath isDefault = createString("isDefault");

    public final StringPath listClass = createString("listClass");

    public final StringPath remark = createString("remark");

    public final StringPath status = createString("status");

    public final StringPath updateBy = createString("updateBy");

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public final com.querydsl.sql.PrimaryKey<SysDict> primary = createPrimaryKey(dictCode);

    public QSysDict(String variable) {
        super(SysDict.class, forVariable(variable), "null", "sys_dict_data");
        addMetadata();
    }

    public QSysDict(String variable, String schema, String table) {
        super(SysDict.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSysDict(String variable, String schema) {
        super(SysDict.class, forVariable(variable), schema, "sys_dict_data");
        addMetadata();
    }

    public QSysDict(Path<? extends SysDict> path) {
        super(path.getType(), path.getMetadata(), "null", "sys_dict_data");
        addMetadata();
    }

    public QSysDict(PathMetadata metadata) {
        super(SysDict.class, metadata, "null", "sys_dict_data");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createBy, ColumnMetadata.named("create_by").withIndex(10).ofType(Types.VARCHAR).withSize(64));
        addMetadata(createTime, ColumnMetadata.named("create_time").withIndex(11).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(cssClass, ColumnMetadata.named("css_class").withIndex(6).ofType(Types.VARCHAR).withSize(100));
        addMetadata(dictCode, ColumnMetadata.named("dict_code").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(dictLabel, ColumnMetadata.named("dict_label").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(dictSort, ColumnMetadata.named("dict_sort").withIndex(2).ofType(Types.INTEGER).withSize(10));
        addMetadata(dictType, ColumnMetadata.named("dict_type").withIndex(5).ofType(Types.VARCHAR).withSize(100));
        addMetadata(dictValue, ColumnMetadata.named("dict_value").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(isDefault, ColumnMetadata.named("is_default").withIndex(8).ofType(Types.CHAR).withSize(1));
        addMetadata(listClass, ColumnMetadata.named("list_class").withIndex(7).ofType(Types.VARCHAR).withSize(100));
        addMetadata(remark, ColumnMetadata.named("remark").withIndex(14).ofType(Types.VARCHAR).withSize(500));
        addMetadata(status, ColumnMetadata.named("status").withIndex(9).ofType(Types.CHAR).withSize(1));
        addMetadata(updateBy, ColumnMetadata.named("update_by").withIndex(12).ofType(Types.VARCHAR).withSize(64));
        addMetadata(updateTime, ColumnMetadata.named("update_time").withIndex(13).ofType(Types.TIMESTAMP).withSize(19));
    }

}

