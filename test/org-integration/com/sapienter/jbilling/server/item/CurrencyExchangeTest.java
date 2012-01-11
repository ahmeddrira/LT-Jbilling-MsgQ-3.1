package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.util.CurrencyWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kkulagin
 * @since 10.01.12
 */
public class CurrencyExchangeTest extends TestCase {

    private static final Integer SYSTEM_CURRENCY_ID = 1;

    /**
     * should remove record for specified date with null amount
     */
    public void testRecordRemoving() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        final CurrencyWS[] currencies = api.getCurrencies();
        final List<CurrencyWS> nonSystemCurrencies = getNonSystemCurrencies(currencies);
        // there will be definitely more than 2 currencies
        final CurrencyWS currency1 = nonSystemCurrencies.get(0);
        final CurrencyWS currency2 = nonSystemCurrencies.get(1);

        Date date1 = getDate1();
        Date date2 = getDate2();

        currency1.setRate("10.0");
        currency1.setFromDate(date1);
        api.updateCurrency(currency1);

        currency1.setRate("100.0");
        currency1.setFromDate(date2);
        api.updateCurrency(currency1);

//        api.


        final CurrencyWS currency = currencies[0];
        currency.setRate("");


    }


    private static List<CurrencyWS> getNonSystemCurrencies(CurrencyWS[] currencies) {
        List<CurrencyWS> result = new ArrayList<CurrencyWS>();
        for (CurrencyWS currency : currencies) {
            if(!SYSTEM_CURRENCY_ID.equals(currency.getId())) {
                result.add(currency);
            }
        }
        return result;
    }

    private static Date getDate1(){
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2100);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private static Date getDate2() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2100);
        calendar.set(Calendar.MONTH, Calendar.APRIL);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        return calendar.getTime();
    }
}
