package com.java.sdeven.sparrow.commons.timewheel.model;

import com.java.sdeven.sparrow.commons.timewheel.utils.serialize.JsonUtils;
import lombok.Data;

/**
 * @author Echo009
 * @since 2022/3/22
 */
@Data
public class LifeCycle {
    public static final LifeCycle EMPTY_LIFE_CYCLE = new LifeCycle();
    private Long start;
    private Long end;

    public static LifeCycle parse(String lifeCycle){
        try {
           return JsonUtils.parseObject(lifeCycle,LifeCycle.class);
        }catch (Exception e){
            // ignore
            return EMPTY_LIFE_CYCLE;
        }
    }

}
