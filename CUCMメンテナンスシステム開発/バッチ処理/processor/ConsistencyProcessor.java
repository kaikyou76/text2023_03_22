/**
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * ConsistencyProcessor.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.step.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for Consistency
 *
 * <MODIFICATION HISTORY>
 * 1.0 2013/09/12 KSC Hiroaki Endo
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class ConsistencyProcessor implements ItemProcessor<Object, Object> {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(ConsistencyProcessor.class);

    @Override
    public Object process(Object param) throws Exception {
        return param;
    }
}
