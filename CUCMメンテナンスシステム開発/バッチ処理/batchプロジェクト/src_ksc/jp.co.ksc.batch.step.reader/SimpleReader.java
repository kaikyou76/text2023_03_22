/*
 * Copyright (c) 2014, NET MARKS COMPANY, LIMITED All Rights Reserved.
 *
 * LockFileManager.java
 *
 * @date 2013/09/12
 * @version 1.0
 * @author KSC Hiroaki Endo
 */

package jp.co.ksc.batch.step.reader;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * SimpleReader class implementation of ItemReader interface.
 * This reader returns a list containing a single integer value.
 * 
 * <pre>
 * MODIFICATION HISTORY
 * Version Date        Author           Description
 * 1.0     2013/09/12  KSC Hiroaki Endo Initial version
 * </pre>
 */
public class SimpleReader implements ItemReader<Object> {
    // 実行フラグ
    private boolean flg = true;

    /**
     * Reads a single item from the input source.
     *
     * @return The item read or null if the end of the input source is reached.
     * @throws Exception                  if there is any error during the read process.
     * @throws UnexpectedInputException   if the input is not as expected.
     * @throws ParseException            if there is any error in the input data.
     * @throws NonTransientResourceException if the read process encounters a non-transient issue.
     */
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<Integer> tmpList = new ArrayList<Integer>();
        tmpList.add(0);
        
        if (flg) {
            flg = false;
            return tmpList;
        } else {
            return null;
        }
    }
}
