/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

/*
 * Created on Aug 11, 2004
 */
package com.sapienter.jbilling.server.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.user.UserBL;

/**
 * @author Emil
 */
public class Util {
    public static String formatDate(Date date, Integer userId) 
            throws SessionInternalError {
        Locale locale;
        try {
            UserBL user = new UserBL(userId);
            locale = user.getLocale();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                locale);
        SimpleDateFormat df = new SimpleDateFormat(
                bundle.getString("format.date"));
        return df.format(date);
    }
    
    public static String formatMoney(Float number, Integer userId, 
            Integer currencyId, boolean forEmail) 
            throws SessionInternalError {
        Locale locale;
        try {
            // find first the right format for the number
            UserBL user = new UserBL(userId);
            locale = user.getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                    locale);
            NumberFormat format = NumberFormat.getNumberInstance(locale);
            ((DecimalFormat) format).applyPattern(bundle.getString(
                    "format.float"));
            
            // now the symbol of the currency
            CurrencyBL currency = new CurrencyBL(currencyId);
            String symbol = currency.getEntity().getSymbol();
            if (symbol.length() >= 4 && symbol.charAt(0) == '&' && 
                    symbol.charAt(1) == '#') {
                if (!forEmail) {
                    // this is an html symbol
                    // remove the first two digits
                    symbol = symbol.substring(2);
                    // remove the last digit (;)
                    symbol = symbol.substring(0, symbol.length() - 1);
                    // convert to a single char
                    Character ch = new Character((char) 
                            Integer.valueOf(symbol).intValue());
                    symbol = ch.toString();
                } else {
                    symbol = currency.getEntity().getCode();
                }
            }
            return symbol + " " + format.format(number.doubleValue());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    public static String getPeriodUnitStr(Integer id, Integer language) {
        Logger log = Logger.getLogger(Util.class);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);
            

            return descriptionHome.findIt(Constants.TABLE_PERIOD_UNIT, id,
                "description", language).getContent();
        } catch (FinderException e) {
            log.debug("Description not set for period unit " + id + " language" +
                    " " + language);
            return null;
        } catch (Exception e) {
            log.error("Exception while looking for an item description", e);
            return null;
        }
    }
    
    public static double round(double val, int places) {
        long factor = (long) Math.pow(10, places);

        //         Shift the decimal the correct number of places
        //         to the right.
        val = val * factor;

        //         Round to the nearest integer.
        long tmp = Math.round(val);

        //         Shift the decimal the correct number of places
        //         back to the left.
        return (double) tmp / factor;
    }
    
    public static float round(float val, int places) {
        return (float) round((double) val, places);
    }
    
    public static String float2string(Float arg, Locale loc) {
        if (arg == null) {
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance(loc);
        return nf.format(arg);
    }

    public static Float string2float (String arg, Locale loc) 
        throws ParseException {
        if (arg == null) {
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance(loc);
        return new Float(nf.parse(arg).floatValue());
    }

}
