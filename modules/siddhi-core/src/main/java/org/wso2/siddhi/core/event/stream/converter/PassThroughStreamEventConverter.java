/*
 * Copyright (c) 2005 - 2014, WSO2 Inc. (http://www.wso2.org)
 * All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhi.core.event.stream.converter;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventPool;

public class PassThroughStreamEventConverter implements EventConverter {
    private StreamEventPool streamEventPool;

    public PassThroughStreamEventConverter(StreamEventPool streamEventPool) {
        this.streamEventPool = streamEventPool;
    }

    private void convertToInnerStreamEvent(Object[] data, boolean isExpected, long timestamp, StreamEvent borrowedEvent) {
        System.arraycopy(data, 0, borrowedEvent.getOutputData(), 0, data.length);
        borrowedEvent.setExpired(isExpected);
        borrowedEvent.setTimestamp(timestamp);
    }


    public void convertEvent(Event event, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(event.getData(), event.isExpired(), event.getTimestamp(), borrowedEvent);
    }

    public void convertStreamEvent(StreamEvent streamEvent, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(streamEvent.getOutputData(), streamEvent.isExpired(),
                streamEvent.getTimestamp(), borrowedEvent);
    }

    @Override
    public void convertData(long timeStamp, Object[] data, StreamEvent borrowedEvent) {
        convertToInnerStreamEvent(data, false, timeStamp, borrowedEvent);
    }

    /**
     * Borrow a event from the pool
     */
    @Override
    public StreamEvent borrowEvent() {
        return streamEventPool.borrowEvent();
    }

    @Override
    public void returnEvent(StreamEvent streamEvent) {
        streamEventPool.returnEvent(streamEvent);
    }
}