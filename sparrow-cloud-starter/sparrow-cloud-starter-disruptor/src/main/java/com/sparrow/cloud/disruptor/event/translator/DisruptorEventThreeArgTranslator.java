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
package com.sparrow.cloud.disruptor.event.translator;

import com.sparrow.cloud.disruptor.context.event.DisruptorEvent;
import com.lmax.disruptor.EventTranslatorThreeArg;

public class DisruptorEventThreeArgTranslator implements EventTranslatorThreeArg<DisruptorEvent, String, String, String> {

	@Override
	public void translateTo(DisruptorEvent dtEevent, long sequence, String event, String tag, String key) {
		dtEevent.setEvent(event);
		dtEevent.setTag(tag);
		dtEevent.setKey(key);
	}
	
}
