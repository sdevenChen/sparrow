/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.sdeven.sparrow.starter.autoconfigure.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.sdeven.sparrow.starter.idgenerator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Override id generation
 * @author sdeven
 * @since 1.0.0
 */
public class CustomIdGenerator implements IdentifierGenerator {

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Long nextId(Object entity) {
        String key = entity.getClass().getName();
        return idGenerator.nextId(key);
    }
}
