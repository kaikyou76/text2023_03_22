/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LockFileManager.java
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
 * SimpleProcessor class implementation of ItemProcessor interface.
 * This processor simply returns the input parameter as is.
 *
 * <pre>
 * MODIFICATION HISTORY
 * Version Date        Author           Description
 * 1.0     2013/09/12  KSC Hiroaki Endo Initial version
 * </pre>
 *
 * @author KSC Hiroaki Endo
 * @version 1.0 2013/09/12
 */
public class SimpleProcessor implements ItemProcessor<Object, Object> {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(SimpleProcessor.class);

    /**
     * Process the provided item.
     *
     * @param item the item to be processed.
     * @return the processed item or null if to filter out the item.
     * @throws Exception if there is any error during the process.
     */
    @Override
    public Object process(Object item) throws Exception {
        return item;
    }
}
