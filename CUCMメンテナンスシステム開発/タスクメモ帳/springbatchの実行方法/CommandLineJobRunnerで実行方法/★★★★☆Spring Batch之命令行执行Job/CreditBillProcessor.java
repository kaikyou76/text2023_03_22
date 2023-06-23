package com.xj.demo8;
 
import org.springframework.batch.item.ItemProcessor;
 
/**
 * @Author : xjfu
 * @Date : 2021/12/05 19:29
 * @Description :demo8的处理类
 */
public class CreditBillProcessor implements ItemProcessor<CreditBill, CreditBill> {
    @Override
    public CreditBill process(CreditBill bill) throws Exception {
 
        System.out.println(bill.toString());
        //做一些简单的处理
        bill.setAccountID(bill.getAccountID() + "1");
        bill.setName(bill.getName() + "2");
        bill.setAmount(bill.getAmount() + 3);
        bill.setDate(bill.getDate() + "4");
        bill.setAddress(bill.getAddress() + 5);
 
        return bill;
    }
}