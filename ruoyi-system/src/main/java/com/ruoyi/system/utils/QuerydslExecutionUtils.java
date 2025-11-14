package com.ruoyi.system.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class QuerydslExecutionUtils {

    public static void dateRange(BooleanBuilder predicate, DateTimePath<LocalDateTime> path, LocalDate startTime, LocalDate endTime) {
        if (ObjectUtils.allNotNull(startTime)) {
            predicate.and(path.goe(LocalDateTime.of(startTime, LocalTime.MIN)));
        }
        if (ObjectUtils.allNotNull(endTime)) {
            predicate.and(path.loe(LocalDateTime.of(endTime, LocalTime.MAX)));
        }
    }

}
