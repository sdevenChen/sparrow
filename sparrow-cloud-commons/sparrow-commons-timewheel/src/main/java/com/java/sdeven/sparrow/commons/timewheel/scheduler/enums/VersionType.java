package com.java.sdeven.sparrow.commons.timewheel.scheduler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Scheduling time strategies
 *
 * @author tjq
 * @since 2020/3/30
 */
@Getter
@AllArgsConstructor
@ToString
public enum VersionType {

    V1(1),
    V2(2);
    private final int v;
    public static VersionType of(int v) {
        for (VersionType type : values()) {
            if (type.v == v) {
                return type;
            }
        }
        throw new IllegalArgumentException("unknown TimeExpressionType of " + v);
    }
}
