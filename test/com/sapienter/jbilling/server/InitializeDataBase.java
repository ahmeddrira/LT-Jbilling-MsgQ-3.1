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

package com.sapienter.jbilling.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Vector;

import javax.naming.NamingException;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.report.ReportDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author emilc
 * file:/C:/bop/tools/jboss-3.2.1_tomcat-4.1.24/server/default/deploy/betty-ejb.jar 
 * file:/C:/bop/tools/jboss-3.2.1_tomcat-4.1.24/server/default/deploy/betty.war 
 */
public final class InitializeDataBase {

    static Connection conn = null;

    static class Table {
        String name;
        String columns[];
        String data[][];
        String international_columns[][][];
        boolean isMassive;
        int maxRows;
        int minRows;
        Column columnsMetaData[];
    }
    
    static class Column {
        String dataType;
        int intRange1;
        int intRange2;
        String dateRange1;
        int dateRangeDays;
        boolean isNull;
        String constantValue;
        float floatFactor;
    }

    private static Vector tables = null;

    public static void main(String[] args) throws Exception {
        
        if (args.length != 1) {
            throw new Exception("Missing database type parameter");
        } 
        tables = new Vector();
        try {
            initData();

            if (args[0].equals("postgres")) {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://maximus:5432/betty",
                        "betty_user", "snower51");

            } else if (args[0].equals("hsql")) {
                Class.forName("org.hsqldb.jdbcDriver");
                conn = DriverManager.getConnection(
                        "jdbc:hsqldb:hsql://localhost",
                        "sa", "");
            } else {
                throw new Exception("Database not supported:" + args[0]);
            }
			conn.setAutoCommit(false);
            // for each table
            for (int tableIdx = 0; tableIdx < tables.size(); tableIdx++) {
                Table table = (Table) tables.get(tableIdx);
                processTable(table, tableIdx + 2);    // hack, because the Intialize Reports run first ...
            }

			conn.commit();
            conn.close();
			System.out.println("DONE!!");
			
        } catch (NamingException e) {
            System.err.println("Naming problem with the drivers : " + e);
            e.printStackTrace();
            
        } catch (Exception e) {
        	try {
        		conn.rollback();
        		conn.close();
        	} catch(Exception x) {}
			System.err.println("Exception ! " + e);
			e.printStackTrace();
			
        }
    }

    static void processTable(Table table, int tableIdx) 
        throws Exception {

            StringBuffer sql = new StringBuffer();
            int rowIdx=0;

            try {

                System.out.println("Now processing " + 
                        table.name + " [" + tableIdx + "]");
                // get the betty_table and others updated
                updateBettyTables(table, tableIdx);

                // generate the INSERT string with this table's columns
                sql.append("insert into " + table.name + " (");
                for (int columnsIdx = 0; columnsIdx < table.columns.length;) {
                    sql.append(table.columns[columnsIdx]);
                    columnsIdx++;
                    if (columnsIdx < table.columns.length) {
                        sql.append(",");
                    }
                }
                sql.append(") values (");
                for (int columnsIdx = 0; columnsIdx < table.columns.length;) {
                    sql.append("?");
                    columnsIdx++;
                    if (columnsIdx < table.columns.length) {
                        sql.append(",");
                    }
                }
                sql.append(")");

                System.out.println(sql.toString());

                PreparedStatement stmt = conn.prepareStatement(sql.toString());
                // for each row to be inserted, apply the data to the '?' 
                for (rowIdx = 0;
                        table.data != null && rowIdx < table.data.length;
                        rowIdx++) {

                    // normal tables don't have data for the first column (the id)
                    // but map tables have data for every column
                    int idxDifference = 0; // this will be for a map table
                    for (int columnIdx = 0;
                            columnIdx < table.columns.length;
                            columnIdx++) {
                        String field;
                        if (table.columns[columnIdx].equals("id")) {
                            // this is the id, which is automatically generated
                            field = String.valueOf(rowIdx + 1);
                            idxDifference = 1; // this is a normal table
                        } else {
                            // this is just a normal column
                            field = table.data[rowIdx][columnIdx - idxDifference];
                        }
                        stmt.setString(columnIdx + 1, field);

                    }

                    if (stmt.executeUpdate() != 1) {
                        throw new Exception(
                                "insert failed. Row "
                                + rowIdx
                                + " table "
                                + table.name);
                    }

                    // now take care of the pseudo columns with international
                    // text
                    if (table.international_columns != null) {
                        insertPseudoColumns(
                                tableIdx,
                                rowIdx + 1,
                                table.international_columns[rowIdx]);
                    }

                }
                System.out.println("inserted " + rowIdx + " rows");
                
                if (table.isMassive) {
                    Random rnd = new Random();
                    // now I have to create radom data for this table
                    if (table.columnsMetaData == null) {
                        throw new Exception("The table " + table.name + 
                            " is massive but lacks columns meta data");
                    }
                    int randomRows, totalRandomRows;
                    totalRandomRows = rnd.nextInt(table.maxRows - table.minRows) +
                            table.minRows;
                    
                    // for each radomic row to generate
                    for(randomRows = 0; randomRows < totalRandomRows; 
                            randomRows++, rowIdx ++) {
                        // this is the ID, we'll set it up automatically
                        stmt.setString(1, String.valueOf(rowIdx + 1));
                        
                        // the columns starts one ahead becaues of the ID
                        for (int columnIdx = 1;
                                columnIdx < table.columns.length;
                                columnIdx++) {
                            Column column = table.columnsMetaData[columnIdx - 1];
                            String dataType = column.dataType;
                            String data = column.constantValue;
                            
                            if (!column.isNull && data == null) {
                                if (dataType.compareTo("int") == 0) {
                                    int thisData = rnd.nextInt((column.intRange2+1) - 
                                            column.intRange1) + column.intRange1;
                                    data = String.valueOf(thisData);
                                } else if (dataType.compareTo("str") == 0) {
                                    data = "Generated test string " + rnd.nextInt();
                                } else if (dataType.compareTo("date") == 0) {
                                    Date aDate = Util.parseDate(column.dateRange1);
                                    GregorianCalendar cal = new GregorianCalendar();
                                    cal.setTime(aDate);
                                    cal.add(GregorianCalendar.DAY_OF_YEAR,
                                            rnd.nextInt(column.dateRangeDays));
                                    data = cal.get(GregorianCalendar.YEAR) + "-" +
                                            cal.get(GregorianCalendar.MONTH) + "-" +
                                            cal.get(GregorianCalendar.DAY_OF_MONTH);
                                } else if (dataType.compareTo("float") == 0) {
                                    data = String.valueOf(rnd.nextFloat() * 
                                            column.floatFactor);
                                } else {
                                    throw new Exception ("Table " + table.name +
                                            " column " + table.columns[columnIdx - 1] +
                                            " datatype = " + dataType + ". Is not" +                                            " a recognized datatype");
                                }
                            }
                            stmt.setString(columnIdx + 1, data);
                        }

                        if (stmt.executeUpdate() != 1) {
                            throw new Exception("random insert failed. Row "
                                    + rowIdx + " table " + table.name);
                        }
                    }
                    System.out.println("Inserted " + randomRows + 
                            " randomic row ");
                }

                stmt.close();
                updateBettyTablesRows(tableIdx, rowIdx + 1);
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                throw new Exception("Error inserting table " + table.name + 
                        " row " + (rowIdx + 1));
            } 
        }

    static void addTable(String name, String columns[], 
            String data[][], boolean massive) {
        addTable(name, columns, data, null, massive);
    }

    static void addTable(String name, String columns[], String data[][],
            String intColumns[][][], boolean massive) {
        addTable(name, columns, data, intColumns, massive, null, 0, 0);            
    }
    
    static void addTable(
            String name,
            String columns[],
            String data[][],
            String intColumns[][][],
            boolean massive,
            Column metaData[],
            int min, int max) {
            
        Table table;
        table = new Table();
        table.name = name;
        table.columns = columns;
        table.data = data;
        table.international_columns = intColumns;
        table.isMassive = massive;
        table.columnsMetaData = metaData;
        table.maxRows = max;
        table.minRows = min;
        tables.add(table);
    }

    static void updateBettyTables(Table table, int tableId)
        throws SQLException {

            // first the row in betty_table
            PreparedStatement stmt =
                conn.prepareStatement(
                        "insert into betty_table (id, name, next_id) "
                        + "values(?, ?, ?)");
            stmt.setInt(1, tableId);
            stmt.setString(2, table.name);
            stmt.setInt(3, 0);
            stmt.executeUpdate();
            stmt.close();

            // now each of the columns
            stmt =
                conn.prepareStatement(
                        "insert into betty_table_column (id, name, table_id) "
                        + "values (?, ?, ?)");
            for (int columnsIdx = 0;
                    columnsIdx < table.columns.length;
                    columnsIdx++) {
                stmt.setInt(1, columnsIdx);
                stmt.setString(2, table.columns[columnsIdx]);
                stmt.setInt(3, tableId);
                stmt.executeUpdate();
            }
            stmt.close();
        }

    static void updateBettyTablesRows(int tableId, int totalRows)
        throws SQLException {
        PreparedStatement stmt =
            conn.prepareStatement(
                    "update betty_table set next_id = ? " + " where id = ?");
        stmt.setInt(1, totalRows);
        stmt.setInt(2, tableId);
        stmt.executeUpdate();
        stmt.close();

        }

    static void insertPseudoColumns(int tableId, int rowId, String data[][])
            throws SQLException {

        String sql = "insert into international_description "
                + "(table_id, foreign_id, psudo_column,language_id,content) "
                + "values (?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, tableId);
        stmt.setInt(2, rowId);

        for (int entry = 0; entry < data.length; entry++) {
            // this would be the pseudo column
            stmt.setString(3, data[entry][0]);
            // and this the actual content
            stmt.setString(5, data[entry][1]);
            String language = null;
            if (data[entry].length < 3 || data[entry][2] == null) {
                language = "1"; //defaults to english
            } else {
                language = data[entry][2];
            }
            stmt.setString(4, language);

            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Should've insterted one row into international_description");
            }
        }

        stmt.close();

    }


    // idealy, this should be loaded from betty-schema.xml
    static void initData() {
        int columnIdx;

        /*
         * When adding or deleting a table, do a search on 'table_id', since
         * there are some tables with hardcoded tables ids. (like contact_map, preference)
         */
        //INTERNATIONAL_DESCRIPTION
        String descriptionColumns[] =
        { "id", "table_id", "foreign_id", "language_id" };
        String descriptionData[][] = null;
        addTable(Constants.TABLE_INTERNATIONAL_DESCRIPTION, descriptionColumns,
                descriptionData, false);

        //LANGUAGE
        String languageColumns[] = { "id", "code", "description" };
        // this HAS to follow the ISO-639
        String languageData[][] = { 
            { "en", "English" }, 
            { "es", "Spanish" },
			{ "fr", "French" },
			{ "it", "Italian" },
			{ "de", "German" },
        };
        addTable(Constants.TABLE_LANGUAGE, languageColumns, languageData, false);
        
        //CURRENCY
        // has to follow ISO-4217
        String currencyColumns[] = { "id", "symbol", "code", "country_code" };
        String currencyData[][] = { 
            { "US$", "USD", "US"}, 
            { "C$", "CAD", "CA" },
            { "&#8364;", "EUR", "EU" },
            { "&#165;", "JPY", "JP" },
            { "&#163;", "GBP", "UK" },
            { "&#8361;", "KRW", "KR" },
			{ "Sf", "CHF", "CH" }, // 7
			{ "SeK", "SEK", "SE" }, // 8
        };
        String currencyIntColumns[][][] = {
            { { "description", "United States Dollar" } },  // 1
            { { "description", "Canadian Dollar" } }, // 2
            { { "description", "Euro" } }, // 3
            { { "description", "Yen" } }, // 4
            { { "description", "Pound Sterling" } }, // 5
            { { "description", "Won" } }, // 6
            { { "description", "Swiss Franc" } }, // 7
            { { "description", "Swedish Krona" } }, // 8
        };
        addTable(Constants.TABLE_CURRENCY, currencyColumns, currencyData,
                currencyIntColumns, false);

        //ENTITY
        String entityColumns[] =
        { "id", "external_id", "description", "create_datetime", "language_id", "currency_id" };
        String entityData[][] = { 
            { "coolString1", "Sapienter", "2003-03-20", "1", "1", "1" }, 
            { "coolString2", "Direct Satellite", "2003-01-20", "1", "6" }
        };
        addTable(Constants.TABLE_ENTITY, entityColumns, entityData, false);

        //PERIOD_UNIT
        String orderPeriodUnitColumns[] = { "id" };
        String orderPeriodUnitData[][] = { null, null, null, null };
        String orderPeriodUnitIntColumns[][][] = {
            { {"description", "Month" } },
            { {"description", "Week" } }, 
            { {"description", "Day" } },
            { {"description", "Year" } }
        };
        addTable(Constants.TABLE_PERIOD_UNIT, orderPeriodUnitColumns,
                orderPeriodUnitData,orderPeriodUnitIntColumns, false);


        // INVOICE_DELIVERY_METHOD
        String invoiceDeliveryMethodColumns[] = { "id" };
        String invoiceDeliveryMethodData[][] = { null, null };

        String invoiceDeliveryMethodIntColumns[][][] = {
            { { "description", "Email" } },  // 1
            { { "description", "Paper" } }, // 2
        };
        addTable(Constants.TABLE_INVOICE_DELIVERY_METHOD, invoiceDeliveryMethodColumns, 
                invoiceDeliveryMethodData, 
                invoiceDeliveryMethodIntColumns, false);
       
       // ENTITY_DELIVERY_METHOD_MAP
       String methodMapColumns[] = {
           "method_id", "entity_id"
       };
       String methodMapData[][] = { {"1", "1"} , {"2", "1"}, {"1", "2"} };
       addTable(Constants.TABLE_ENTITY_DELIVERY_METHOD_MAP, methodMapColumns, 
            methodMapData, false); 
           
        
        //USER_STATUS
        String userStatusColumns[] = { "id", "can_login" };
        String userStatusData[][] = { {"1"}, {"1"}, {"1"}, {"1"},
            {"0"}, {"0"}, {"0"}, {"0"} };

        String userStatusIntColumns[][][] = {
            { { "description", "Active" } },  // 1
            { { "description", "Overdue" } }, // 2
            { { "description", "Overdue 2" } }, // 3
            { { "description", "Overdue 3" } }, // 4
            { { "description", "Suspended" } }, // 5
            { { "description", "Suspended 2" } }, // 6
            { { "description", "Suspended 3" } }, // 7
            { { "description", "Deleted" } }, // 8 
            // the deleted status is never displayed or used, but is necessary
            // to allow the entity to pick if it wants this step or not
        };

        addTable(Constants.TABLE_USER_STATUS, userStatusColumns, userStatusData, 
                userStatusIntColumns, false);

        //BASE_USER
        String userColumns[] = {
            "id",
            "entity_id",
            "user_name",
            "password",
            "deleted",
            "status_id",
            "currency_id",
            "create_datetime",
            "last_status_change",
            "language_id"
        };
        String userData[][] = { 
            { "1", "emil", "asdfasdf", "0", "1", "1", "2003-01-20", null, "1" },  //1
            { "1", "emil2", "qwerqwer", "0", "1", "2", "2003-02-20", null, "1" }, //2
            { "1", "internal", "asdfasdf", "0", "1", "3", "2003-03-20", "2003-12-20", "1" }, //3
            { "1", "root", "asdfasdf", "0", "1", "4", "2003-04-20", null, "1" }, //4
            { "1", "clerk", "asdfasdf", "0", "1", "5", "2003-05-20", null, "1" }, //5
            { "1", "partner", "asdfasdf", "0", "1", "1", "2003-06-20", null, "1" }, //6
            { "1", "customer", "asdfasdf", "0", "1", null, "2003-07-20", "2003-12-20", "1" }, //7
            { "1", "partner2", "asdfasdf", "0", "1", "1", "2004-01-17", null, "1" }, //8
            { "1", "customer2", "asdfasdf", "0", "1", "2", "2003-09-20", null, "1" }, //9
            { "1", "customer4", "asdfasdf", "0", "1", "3", "2003-10-20", "2003-12-20", "1" }, //10
            { "1", "customer5", "asdfasdf", "0", "1", "4", "2003-11-20", null, "1" }, //11
            { "1", "customer6", "asdfasdf", "0", "1", "5", "2003-12-20", null, "1" }, //12
            { "1", "customer7", "asdfasdf", "0", "1", "6", "2004-01-20", null, "1" }, //13
            { "2", "customer8", "asdfasdf", "0", "1", "1", "2004-02-23", null, "1" }, //14
            { "2", "partner3", "asdfasdf", "0", "1", "1", "2003-01-25", "2004-01-20", "1" }, //15
            { "2", "customer3", "asdfasdf", "0", "1", "1", "2003-01-29", null, "1" }, //16
            { "1", "customer17", "asdfasdf", "0", "1","2", "2003-09-20", null, "1" }, //17
        };
        addTable(Constants.TABLE_BASE_USER, userColumns, userData, false);

        //PARTNER
        String partnerColumns[] = { 
            "id", 
            "user_id",
            "balance",
            "total_payments",
            "total_refunds",
            "total_payouts",
            "percentage_rate",
            "referral_fee",
            "fee_currency_id",
            "one_time",
            "period_unit_id",
            "period_value",
            "next_payout_date",
            "due_payout",
            "automatic_process",
            "related_clerk"
        };
        String partnerData[][] = { 
            { "6", "0", "1000", "100", "900", "10",null,null,"0", "1", "1", "2004-04-01", "0", "1", "5" }, 
            { "8", "0", "1000", "100", "900",null, "5", "1", "0", "1", "1", "2004-03-01", "0", "0", "5" }, 
            { "15","0", "1000", "100", "900",null, "15","2", "1", "3", "10", "2004-03-15", "0", "1", "14" },
        };
        addTable(Constants.TABLE_PARTNER, partnerColumns, 
                partnerData, false);

        //CUSTOMER
        String customerColumns[] = { 
            "id", "user_id", "partner_id", "referral_fee_paid",
            "invoice_delivery_method_id", "auto_payment_type" };
        String customerData[][] = { 
            { "7", "1", null, "1", "1" }, 
            { "9", "1", null, "2", "1"},
            { "10","2", null, "1", "1"},
            { "11","2", null, "2", "1"},
            { "16","3", null, "1", "1"},
            { "13","1", null, "2", "1"},
            { "14","1", null, "1", "1"},
        };
        addTable(Constants.TABLE_CUSTOMER, customerColumns, customerData, false);
        
        //ITEM_TYPE
        String itemTypeColumns[] = {
            "id", "entity_id"
        };
        String itemTypeData[][] = null;
        addTable(Constants.TABLE_ITEM_TYPE, itemTypeColumns, itemTypeData, false);
        
        //ITEM
        String itemColumns[] =
        { "id", "internal_number", "entity_id", "percentage",
          "price_manual", "deleted" };
        String itemData[][] = {
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "1", "0" }, 
            { null,"1", null, "1", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "1", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", null, "0", "0" }, 
            { null,"1", "10", "0", "0" }, 
            { null,"2", null, "0", "0"}
        };
        String itemIntColumns[][][] = {
            { {"description", "Keyboard" } },
            { {"description", "Mouse" } },
            { {"description", "Printer" } },                    
            { {"description", "PalmPilot" } },
            { {"description", "Speakers" } },
            { {"description", "Memory" } },
            { {"description", "Fax" } },                    
            { {"description", "Notebook" } },
            { {"description", "Monitor 21 inches" } },
            { {"description", "Small server" } },
            { {"description", "10% Penalty" } },
            { {"description", "Billetes de pelicula", "2" } }
        };
        addTable(Constants.TABLE_ITEM, itemColumns, itemData, itemIntColumns, false);

        // ITEM_PRICE
        String itemPriceColumns[] = { "id", "item_id", "currency_id", "price" };
        String itemPriceData[][] = { 
            { "1", "1",  "109.99"}, 
            { "1", "2",  "199.99"},
            { "2", "1",  "31.50"},
            { "3", "3",  "205"},
            { "4", "4",  "32541"},
            { "5", "1",  "55.12"},
            { "5", "5",  "29.99"},
            { "6", "3",  "54"},
            { "6", "4",  "6000"},
            { "6", "5",  "31"},
            { "6", "6",  "68000"},
            { "7", "2",  "580"},
            { "8", "1",  "2199"},
            { "9", "1",  "1300"},
            { "10", "1",  "5800"},
            { "12", "1",  "8"},
        };
        addTable(Constants.TABLE_ITEM_PRICE, itemPriceColumns, itemPriceData, false);
        
        //ITEM_USER_PRICE
        String itemUserPriceColumns[] =
        {
            "id",
            "item_id",
            "user_id",
            "currency_id",
            "price"
        };
        String itemUserPriceData[][] = {
            {"1", "6", "1", "90"},
            {"1", "9", "1", "87"},
        };
        addTable(Constants.TABLE_ITEM_USER_PRICE, 
               itemUserPriceColumns, itemUserPriceData, false);

        //ORDER_PERIOD
        String orderPeriod[] = { "id", "entity_id", "value", "unit_id" };
        String orderPeriodData[][] = { 
                { null, null, null }, // this is 'one-time', a global period 
                { "1", "1", "1" }, 
                { "1", "1", "2" }, 
                { "1", "3", "1" }
        };
        String orderPeriodIntColumns[][][] = {
            { {"description", "One time" } },
            { {"description", "Monthly" } },
            { {"description", "Weekly" } },                    
            { {"description", "Quarterly" } }
        };
        addTable(Constants.TABLE_ORDER_PERIOD, orderPeriod, orderPeriodData,
                orderPeriodIntColumns, false);

        //ORDER_LINE_TYPE
        String orderLineTypeColumns[] = { "id", "editable" };
        String orderLineTypeData[][] = { {"1"}, {"0"} };
        String orderLinteTypeIntColumns[][][] = {
            { {"description", "Items" } },
            { {"description", "Tax" } } 
        };
        addTable(Constants.TABLE_ORDER_LINE_TYPE, orderLineTypeColumns, orderLineTypeData, 
                orderLinteTypeIntColumns, false);
                
        //ORDER_BILLING_TYPE
        String orderBillingTypeColumns[] = {"id"};
        String orderBillingTypeData [][] = { null, null };
        String orderBillingTypeIntColumns[][][] = {
            { {"description", "pre paid" } },
            { {"description", "post paid" } } 
        };
        addTable(Constants.TABLE_ORDER_BILLING_TYPE, orderBillingTypeColumns, 
                orderBillingTypeData, orderBillingTypeIntColumns, false);

        //ORDER_STATUS
        String orderStatusColumns[] = {"id"};
        String orderStatusData [][] = { null, null, null };
        String orderStatusIntColumns[][][] = {
            { {"description", "Active" } }, // 1
            { {"description", "Finished" } }, // 2
            { {"description", "Suspended" } } // 3
        };
        addTable(Constants.TABLE_ORDER_STATUS, orderStatusColumns, 
                orderStatusData, orderStatusIntColumns, false);

        //PURCHASE_ORDER
        String orderColumns[] = {
            "id",
            "user_id",
            "period_id", // 1 = pre paid (at), 2 = post paid (goy)
            "billing_type_id",
            "active_since",
            "active_until",
            "create_datetime",
            "next_billable_day",
            "created_by",
            "status_id",
            "deleted",
            "currency_id",
			"notify"
        };
        /*
         * This is test date for specific test cases, it is supposed to be billed with
         * a billing date of 2003-04-01
         */
        String orderData[][] = {
            // user 3 - has orders but none apply
            // 1 - to process = 0
            { "3", "1", "1", null, null, "2003-03-15", "2003-03-15", "1", "2", "0", "1", "0"  },
            // 2 - active too old
            { "3", "2", "1", "2002-06-01", "2002-12-31", "2002-05-15", "2003-01-15", "1", "1", "0", "1", "0"  },
            // 3 - active yet to come
            { "3", "2", "2", "2003-08-01", "2003-12-31", "2002-05-15", "2003-01-15", "1", "1", "0", "1", "0"  },
            // 4 - prepaid, quarter -  already process for this period
            { "3", "4", "1", "2003-01-01", "2003-12-31", "2002-12-15", "2003-05-01", "1", "1", "0", "1", "0"  },

            // user 4 - some orders apply
            // 5 - to process = 0
            { "4", "1", "2", null, null, "2002-12-15", "2003-03-01", "1", "2", "0", "1", "0"  },
            // 6 - a one-time good one
            { "4", "1", "1", null, null, "2003-03-15", null, "1", "1", "0", "1", "0"  },
            // 7 - a monthly, post-paid, on-going good one
            { "4", "2", "2", "2003-01-01", null, "2002-12-01", null, "1", "1", "0", "1", "0"  },
            // 8 - this is already process (sholud be marked tp=0)
            { "4", "2", "1", "2003-01-01", "2003-05-19", "2002-12-01", "2003-05-20", "1", "1", "0", "1", "0"  },
 
            // user 5 - all apply
            // 9 - one time
            { "5", "1", "2", null, null, "2003-03-15", null, "1", "1", "0", "1", "0"  },
            // 10 - monthly post-paid, about to expire
            { "5", "2", "2", "2003-01-01", "2003-04-30", "2002-12-01", "2003-03-01", "1", "1", "0", "1", "0"  },
            // 11 - weekly pre-paid. Four weeks to bill, next pay day 4/12
            { "5", "3", "1", "2003-01-01", "2003-12-31", "2002-12-01", "2003-03-15", "1", "1", "0", "1", "0"  },

            // user 10 - none apply
            // 12 - to_process = 0
            { "10", "1", "1", null, null, "2003-04-01", "2003-03-31", "1", "2", "0", "1", "0"  },
            // 13 - active too old
            { "10", "2", "1", "2003-01-01", "2003-03-31", "2002-12-15", null, "1", "1", "0", "1", "0"  },
            // 14 - active yet to come
            { "10", "3", "2", "2003-06-01", "2003-06-30", "2002-04-15", null, "1", "1", "0", "1", "0"  },
            // 15 - already process for this period
            { "10", "4", "1", "2003-03-01", "2003-04-30", "2003-02-15", "2003-06-20", "1", "1", "0", "1", "0"  },

            // user 11 - some orders apply
            // 16 - to process = 0
            { "11", "3", "2", null, null, "2003-02-15", "2003-03-15", "1", "2", "0", "1", "0"  },
            // 17 - a one-time good one
            { "11", "1", "1", null, null, "2003-04-01", null, "1", "1", "0", "1", "0"  },
            // 18 - a quarterly, post-paid, on-going good one
            { "11", "4", "2", "2002-01-01", null, "2001-11-15", "2003-01-01", "1", "1", "0", "1", "0"  },
            // 19 - this is already process (sholud be marked tp=0)
            { "11", "3", "2", "2003-03-01", "2003-03-31", "2003-02-15", "2003-04-01", "1", "1", "0", "1", "0"  },

            // user 12 - none apply
            // 20 - to_process = 0
            { "12", "1", "1", null, null, "2003-01-01", "2003-02-01", "1", "2", "0", "1", "0"  },
            // 21 - active too old (should be flagged out)
            { "12", "3", "2", "2002-01-01", "2002-03-31", "2001-12-15", "2003-04-10", "1", "1", "0", "1", "0"  },
            // 22 - active yet to come
            { "12", "3", "1", "2003-11-01", null, "2002-04-15", null, "1", "1", "0", "1", "0"  },
            // 23 - monthly, post-paid, already process for this period
            { "12", "2", "2", "2001-03-01", "2003-04-30", "2000-02-15", "2003-04-15", "1", "1", "0", "1", "0"  },

            // user 13 - all apply
            // 24 - one time
            { "13", "1", "1", null, null, "2003-02-15", null, "1", "1", "0", "1", "0"  },
            // 25 - monthly, post-paid, expired but with some time yet to be billed
            { "13", "2", "2", "2003-01-01", "2003-04-01", "2002-12-01", "2003-03-01", "1", "1", "0", "1", "1"  },
            // 26 - weekly prepaid
            { "13", "3", "1", "2003-03-01", "2003-04-30", "2003-03-01", "2003-04-04", "1", "1", "0", "1", "0"  },
            // 27 - monthly prepaid, last period to bill
            { "13", "2", "1", "2003-01-01", "2003-04-01", "2002-12-01", "2003-03-01", "1", "1", "0", "1", "0"  },
            
            // user 14 - belongs to entity 2!
            { "14", "1", "1", null, null, "2003-02-15", null, "1", "1", "0", "1", "0"  },

            // user 17 - only for status tests
            { "17", "1", "1", null, null, "2003-02-15", null, "1", "1", "0", "1", "0"  },

        };
        addTable(Constants.TABLE_PUCHASE_ORDER, orderColumns, orderData, false);

        //ORDER_LINE
        String orderLineColumns[] = {
            "id",
            "order_id",
            "item_id",
            "type_id",
            "description",
            "amount",
            "quantity",
            "price",
            "item_price",
            "create_datetime",
            "deleted" 
        };
        String orderLineData[][] = null;
        Column columnsMetaData[] = new Column[10];
        Column columnMetaData;
        columnIdx = 0;
        // order_id
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1;
        columnMetaData.intRange2 = 27; 
        columnsMetaData[columnIdx++] = columnMetaData;
        // item_id
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1;
        columnMetaData.intRange2 = 11; 
        columnsMetaData[columnIdx++] = columnMetaData;
        // type_id
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.constantValue = "1";
        columnsMetaData[columnIdx++] = columnMetaData;        
        // description
        columnMetaData = new Column();
        columnMetaData.dataType = "str";
        columnMetaData.isNull = false;
        columnsMetaData[columnIdx++] = columnMetaData;        
        // amount
        columnMetaData = new Column();
        columnMetaData.dataType = "float";
        columnMetaData.isNull = false;
        columnMetaData.floatFactor = 1000F;
        columnsMetaData[columnIdx++] = columnMetaData;        
        // quantity
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1;
        columnMetaData.intRange2 = 100; 
        columnsMetaData[columnIdx++] = columnMetaData;        
        // price
        columnMetaData = new Column();
        columnMetaData.dataType = "float";
        columnMetaData.isNull = false;
        columnMetaData.floatFactor = 10; 
        columnsMetaData[columnIdx++] = columnMetaData;        
        // item_price
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 0;
        columnMetaData.intRange2 = 1; 
        columnsMetaData[columnIdx++] = columnMetaData;        
        // create_datetime
        columnMetaData = new Column();
        columnMetaData.dataType = "date";
        columnMetaData.isNull = false;
        columnMetaData.dateRangeDays = 20;
        columnMetaData.dateRange1 = "2003-04-01";
        columnsMetaData[columnIdx++] = columnMetaData;        
        // deleted
        columnMetaData = new Column();
        columnMetaData.isNull = false;
        columnMetaData.constantValue= "0";
        columnsMetaData[columnIdx++] = columnMetaData;        
        
        addTable(Constants.TABLE_ORDER_LINE, orderLineColumns, orderLineData, null, true, 
                columnsMetaData, 250, 500);


        //PLUGGABLE_TASK_TYPE_CATEGORY
        String pluggableTaskTypeCategoryColumns[] = { "id", "description", "interface_name" };
        String pluggableTaskTypeCategoryData[][] = { 
            {"order processing task", 
             "com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask"},
            {"order_filter task",
             "com.sapienter.jbilling.server.pluggableTask.OrderFilterTask"},
            {"invoice filter task",
             "com.sapienter.jbilling.server.pluggableTask.InvoiceFilterTask"},    
            {"invoice composition task", // 4 - builds the invoice
             "com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask"},
            {"order period calculation task",
             "com.sapienter.jbilling.server.pluggableTask.OrderPeriodTask"},
            {"payment processing task",  // 6 - calls a real-time processor
             "com.sapienter.jbilling.server.pluggableTask.PaymentTask"},
            {"notification task",
             "com.sapienter.jbilling.server.pluggableTask.NotificationTask"},             
            {"payment information task", // 8 -  gets the payment instrument info (cc)
             "com.sapienter.jbilling.server.pluggableTask.PaymentInfoTask"}             
        };
        addTable(Constants.TABLE_PLUGGABLE_TASK_TYPE_CATEGORY, pluggableTaskTypeCategoryColumns,
                pluggableTaskTypeCategoryData, false);


        //PLUGGABLE_TASK_TYPE            
        String pluggableTaskTypeColumns[] =
        { "id", "category_id", "min_parameters", "class_name" };
        String pluggableTaskTypeData[][] =
        { 
            { "1", "0", "com.sapienter.jbilling.server.pluggableTask.BasicLineTotalTask" }, //1
            { "1", "2", "com.sapienter.jbilling.server.pluggableTask.GSTTaxTask" }, //2
            { "4", "0", "com.sapienter.jbilling.server.pluggableTask.CalculateDueDate" }, //3
            { "4", "0", "com.sapienter.jbilling.server.pluggableTask.BasicCompositionTask" }, //4
            { "2", "0", "com.sapienter.jbilling.server.pluggableTask.BasicOrderFilterTask" }, //5
            { "3", "0", "com.sapienter.jbilling.server.pluggableTask.BasicInvoiceFilterTask" }, //6
            { "5", "0", "com.sapienter.jbilling.server.pluggableTask.BasicOrderPeriodTask" }, //7
            { "6", "2", "com.sapienter.jbilling.server.pluggableTask.PaymentAuthorizeNetTask" }, //8
            { "7", "6", "com.sapienter.jbilling.server.pluggableTask.BasicEmailNotificationTask" }, //9
            { "8", "0", "com.sapienter.jbilling.server.pluggableTask.BasicPaymentInfoTask" }, //10
            { "6", "0", "com.sapienter.jbilling.server.pluggableTask.PaymentPartnerTestTask" }, //11
            { "7", "1", "com.sapienter.jbilling.server.pluggableTask.PaperInvoiceNotificationTask" }, //12
        };
        String pluggableTaskTypeIntColumns[][][] =
        { {
              {
                  "description",
                  "Takes the quantity and the price to calculate the totals "
                  + "for each line and the order total" 
              },
              { "title", "Basic calculation of totals" }
          }, {
              {
                  "description",
                  "Adds a line with a 7% and update the order total" 
              },
              { "title", "GST calculation" }
          }, {
              {
                  "description",
                  "Adds one month to the billing date for the due date" 
              },
              { "title", "Due date calculation" }
          }, {
              {
                  "description",
                  "Copies all the lines to generate the invoice" 
              },
              { "title", "Basic invoice generation" }
          }, {
              {
                  "description",
                  "Considers the active period and the last process" 
              },
              { "title", "Basic order filter" }
          }, {
              {
                  "description",
                  "Takes only those invoices with due date past the process date"
              },
              { "title", "Basic invoice filter" }
          }, {
              {
                "description",
                "Most common logic to calculate the billable period of an order"
             },
             { "title", "Basic period calculator" }},
          {{ "description",
             "Authorize.net payment process" },
           { "title", "Authorize.net payment" }},
          {{ "description",
             "Simple email notification" },
           { "title", "Simple email notification" }},
          {{ "description",
             "Gets a valid cc from the user" },
           { "title", "Simple payment instrument finder" }},
          {{ "description",
             "Test partner payout processor" },
           { "title", "Test partner payout" }},
          {{ "description",
             "Paper invoice notification with JasperReports" },
           { "title", "Paper invoice notificaiton" }},
        };

        addTable(Constants.TABLE_PLUGGABLE_TASK_TYPE, pluggableTaskTypeColumns,
                pluggableTaskTypeData, pluggableTaskTypeIntColumns, false);

        //PLUGGABLE_TASK
        String pluggableTaskColumns[] =
        { "id", "entity_id", "type_id", "processing_order" };
        String pluggableTaskData[][] =
        { 
            { "1", "1", "1" }, //1
            { "1", "2", "2" },
            { "1", "3", "1" },
            { "1", "4", "2" },
            { "1", "5", "1" }, //5
            { "1", "6", "1" },
            { "1", "7", "1" },
            { "1", "8", "1" },// authorize.net for entity 1
            { "1", "9", "1" },
            { "1", "10","1" },// 10 cc infor fetcher for entity 1
            { "1", "11", "2" },//11 partner payout test 
            { "1", "12", "2" },//12 paper invoice 
            { "2", "1", "1" },// now all the same for entity 2
            { "2", "2", "2" },
            { "2", "3", "1" },
            { "2", "4", "2" }, // 15
            { "2", "5", "1" },
            { "2", "6", "1" },
            { "2", "7", "1" },
            { "2", "8", "1" },// authorize.net for entity 2
            { "2", "9", "1" }, //20
            { "2", "10","1" },// cc infor fetcher for entity 2
            { "2", "11", "2" },// partner payout test 

        };
        addTable(Constants.TABLE_PLUGGABLE_TASK, pluggableTaskColumns, pluggableTaskData, false);

        //PLUGGABLE_TASK_PARAMETER
        String pluggableTaskParameterColumns[] =
        {
            "id",
            "task_id",
            "name",
            "int_value",
            "str_value",
            "float_value" 
        };
        String pluggableTaskParameterData[][] = {
            { "2", "rate", null, null, "7.0"},
            { "2", "description", null, "GST", null},
            { "9", "smtp_server", null, null, null},
            { "9", "from", null, null, null},
            { "9", "password", null, null, null},
            { "9", "port", null, null, null},
            { "9", "username", null, null, null},
            { "9", "reply_to", null, "emilconde@telus.net", null},
            { "9", "from_name", null, "Sapienter", null},
            { "9", "bcc_to", null, null, null},
            { "8", "login", null, "alphat", null},
            { "8", "transaction", null, "xxx", null},
            { "12", "design", null, "simple_invoice", null},

            { "13", "rate", null, null, "7.0"},
            { "20", "smtp_server", null, "smtp.telus.net", null},
            { "20", "from", null, "emiconde@yahoo.com", null},
            { "19", "login", null, "alphat", null},
            { "19", "transaction", null, "xxx", null},
            
        };
        addTable(Constants.TABLE_PLUGGABLE_TASK_PARAMETER, pluggableTaskParameterColumns, 
                pluggableTaskParameterData, false);


        //CONTACT
        String contactColumns[] = {
            "id",
            "ORGANIZATION_NAME",
            "STREET_ADDRES1",
            "STREET_ADDRES2",
            "CITY",
            "STATE_PROVINCE",
            "POSTAL_CODE",
            "COUNTRY_CODE",
            "LAST_NAME",
            "FIRST_NAME",
            "PERSON_INITIAL",
            "PERSON_TITLE",
            "PHONE_COUNTRY_CODE",
            "PHONE_AREA_CODE",
            "PHONE_PHONE_NUMBER",
            "FAX_COUNTRY_CODE",
            "FAX_AREA_CODE",
            "FAX_PHONE_NUMBER",
            "EMAIL",
            "CREATE_DATETIME",
            "deleted" 
        };
        String contactData[][] = {
            {
                "BC Hydro",
                "542 Dunsmir St.",
                null,
                "Vancouver",
                "BC",
                "V5T3W9",
                "CA",
                "Paul",
                "Jhonston",
                "",
                "Computer operations manager",
                null,
                "604",
                "359-8653",
                null,
                "604",
                "359-9898",
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                "Orca Bay",
                "5342 Yukon St.",
                null,
                "Vancouver",
                "BC",
                "V6r7W9",
                "CA",
                "James",
                "Kruk",
                "",
                "Director of IT",
                null,
                "604",
                "359-8873",
                null,
                "604",
                "378-8698",
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                "Smartt Net",
                "6958 Broadway",
                null,
                "Vancouver",
                "BC",
                "V6R1W9",
                "CA",
                "Donald",
                "Richar",
                "",
                "Billing Supervisor",
                null,
                "604",
                "378-9853",
                null,
                "604",
                "359-9998",
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                "Costal Cinemas",
                "522 Jazmin St.",
                null,
                "Vancouver",
                "BC",
                "V5X3Q9",
                "CA",
                "Amid",
                "Pirkat",
                "",
                "Computer operations manager",
                null,
                "604",
                "322-8653",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "John",
                "Cameron",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Peter",
                "Chisky",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Fred",
                "Foster",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Tim",
                "Paterson",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "James",
                "Rokrm",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Paula",
                "Topor",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            {
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Status",
                "Changer",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
            { //12
                null,
                "875 Sanders St.",
                null,
                "Vancouver",
                "BC",
                "V9Y3W9",
                "CA",
                "Status",
                "Changer",
                "",
                null,
                null,
                "604",
                "322-8663",
                null,
                null,
                null,
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
			{ //13
                "Sapienter Billing Software",
                "211-280 Nelson St.",
                null,
                "Vancouver",
                "BC",
                "V6B2E2",
                "CA",
                "Emil",
                "Conde",
                "",
                "President",
                null,
                "604",
                "328-9731",
                null,
                "604",
                "669-5662",
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
			{ //14
                "Direct Satellite",
                "280 Surrey St.",
                null,
                "Vancouver",
                "BC",
                "V6I2W2",
                "CA",
                "John",
                "Conde",
                "",
                "President",
                null,
                "604",
                "328-9441",
                null,
                "604",
                "669-5002",
                "emilconde@telus.net",
                "2003-04-05",
                "0" 
            },
        };
        addTable(Constants.TABLE_CONTACT, contactColumns, contactData, false);

        //CONTACT_TYPE
        String contactTypeColumns[] = { "id", "entity_id", "is_primary" };
        String contactTypeData[][] = { { "1", "1" } };
        String contactTypeIntColumns[][][] = { 
                { { "description", "Primary" } },
            };
        addTable(Constants.TABLE_CONTACT_TYPE, contactTypeColumns, 
                contactTypeData, contactTypeIntColumns, false);

        //CONTACT_MAP
        String contactMapColumns[] =
        { "id", "contact_id", "type_id", "table_id", "foreign_id" };
        String contactMapData[][] = { 
            { "1", "1", "10", "3" }, 
            { "2", "1", "10", "4" }, 
            { "3", "1", "10", "5" }, 
            { "4", "1", "10", "6" }, 
            { "5", "1", "10", "8" },
            { "6", "1", "10", "9" },
            { "7", "1", "10", "10" },
            { "8", "1", "10", "11" },
            { "9", "1", "10", "12" },
            { "10", "1","10", "13" },
            { "11", "1","10", "17" },
            { "12", "1","10", "15" },
			{ "13", "1", "5", "1" }, // the contact for entity 1 
			{ "14", "1", "5", "2" }, // the contact for entity 2
        };
        addTable(Constants.TABLE_CONTACT_MAP, contactMapColumns, contactMapData, false);


        //INVOICE_LINE_TYPE
        String invoiceLineTypeColumns[] =
        {
            "id",
            "description"
        };
        String invoiceLineTypeData[][] = {
            { "item" },
            { "tax" },
            { "due invoice" },
            { "interests" },
        };
        addTable(Constants.TABLE_INVOICE_LINE_TYPE, invoiceLineTypeColumns,
                invoiceLineTypeData, false);
                
        //PAPER_INVOICE_BATCH
        String invoiceBatchColumns[] = {
            "id", "total_invoices", "delivery_date", "is_self_managed"
        };
        String invoiceBatchData[][] = null;
        addTable(Constants.TABLE_PAPER_INVOICE_BATCH, invoiceBatchColumns,
                invoiceBatchData, false);
        

        //BILLING_PROCESS
        String billingProcessColumns[] =
        {
            "id",
            "entity_id",
            "billing_date",
            "period_unit_id",
            "period_value",
            "is_review",
            "paper_invoice_batch_id"
        };
        String billingProcessData[][] = {
        	{ "1", "2003-03-01", "1", "1", "0", null },
        };
        addTable(Constants.TABLE_BILLING_PROCESS, billingProcessColumns, 
                billingProcessData, false);

        //BILLING_PROCESS_RUN
        String billingProcessRunColumns[] =
        {
            "id",
            "process_id",
            "run_date",
            "started",
            "finished",
            "invoices_generated",
        };
        String billingProcessRunData[][] = {
            { "1", "2003-03-01", "2003-02-27", "2003-02-28", "15"}
        };
        
        addTable(Constants.TABLE_BILLING_PROCESS_RUN, billingProcessRunColumns, 
                billingProcessRunData, false);

        //BILLING_PROCESS_CONFIGURATION
        String billingProcessConfigurationColumns[] =
        {
            "id",
            "entity_id",
            "next_run_date",
            "generate_report",
            "retries",
            "days_for_retry",
            "days_for_report",
            "review_status",
            "period_unit_id",
            "period_value",
            "days_due_date"
        };
        String billingProcessConfigurationData[][] = {
            { "1", "2003-10-27", "1", "3", "2", "5", "1", "1", "1", "30" },
            { "2", "2003-10-27", "1", "3", "2", "5", "1", "1", "1", "30" },
        };
        
        addTable(Constants.TABLE_BILLING_PROCESS_CONFIGURATION, 
                billingProcessConfigurationColumns, 
                billingProcessConfigurationData, false);

        //PAYMENT_METHOD
        String paymentMethodColumns[] =
        {
            "id",
        };
        String paymentMethodData[][] = { null, null, null, null, null, null, null };
        String paymentMethodIntColumns[][][] = { 
            { { "description", "Cheque" } },
            { { "description", "Visa" } }, 
            { { "description", "MasterCard" } }, 
			{ { "description", "AMEX" } },
			{ { "description", "ACH" } },
            { { "description", "Discovery" } },
            { { "description", "Diners" } },
        };
        addTable(Constants.TABLE_PAYMENT_METHOD, paymentMethodColumns, 
                paymentMethodData, paymentMethodIntColumns, false);

        //ENTITY_PAYMENT_METHOD_MAP
        String entityPaymentMethodMapColumns[] =
        {
            "entity_id",
            "payment_method_id",
        };
        String entityPaymentMethodMapData[][] = {
            { "1", "1" },
            { "1", "2" },
			{ "1", "3" },
			{ "1", "4" },
			{ "1", "5" },
        };
        
        addTable(Constants.TABLE_ENTITY_PAYMENT_METHOD_MAP, 
                entityPaymentMethodMapColumns, 
                entityPaymentMethodMapData, false);

        //BILLING_PROCESS_RUN_TOTAL
        String billingProcessRunTotalColumns[] =
        {
            "id",
            "process_run_id",
            "currency_id",
            "total_invoiced",
            "total_paid",
            "total_not_paid"
        };
        String billingProcessRunTotalData[][] = {
            { "1", "1", "500", "400", "100"},
            { "1", "2", "500", "400", "100"},
        };
        
        addTable(Constants.TABLE_BILLING_PROCESS_RUN_TOTAL, 
                billingProcessRunTotalColumns, 
                billingProcessRunTotalData, false);

        //BILLING_PROCESS_RUN_TOTAL_PM
        String billingProcessRunTotalPMColumns[] =
        {
            "id",
            "process_run_total_id",
            "payment_method_id",
            "total",
        };
        String billingProcessRunTotalPMData[][] = {
            { "1", "1", "500.56",},
            { "1", "2", "1250.54",},
            { "1", "3", "654.32", },
            { "2", "2", "120.54",},
            { "2", "3", "64.32", },
        };
        
        addTable(Constants.TABLE_BILLING_PROCESS_RUN_TOTAL_PM, 
                billingProcessRunTotalPMColumns, 
                billingProcessRunTotalPMData, false);

        //INVOICE
        String invoiceColumns[] = {
            "id",
            "create_datetime",
            "billing_process_id",
            "user_id",
            "delegated_invoice_id",
            "due_date",
            "total",
            "to_process",
            "balance",
            "is_review",
            "deleted",
            "currency_id",
            "carried_balance",
            "paper_invoice_batch_id"
        };
        String invoiceData[][] = {
        	// user 7, none apply
        	// 1 - due date yet to come
			{ "2003-03-11", "1", "7", null, "2003-05-15", "102", "1", "102", "0", "0", "1", "0", null},
			// 2 - due date yet to come
			{ "2003-03-11", "1", "7", null, "2003-04-25", "2.3", "1", "2.3", "0", "0", "1", "0", null},
			// 3 - to_process = 0
			{ "2003-02-11", "1", "7", "2", "2003-03-15", "102", "0", "102", "0", "0", "1", "2.3", null},
			
			// user 8, some apply
			// 4 - good
			{ "2003-03-21", "1", "8", null, "2003-03-15", "102", "1", "102", "0", "0", "1", "0", null},
			// 5 - due date yet to come
			{ "2003-04-11", "1", "8", null, "2003-05-25", "2.3", "1", "2.3", "0", "0", "1", "0", null},
			// 6 - good
			{ "2003-03-01", "1", "8", null, "2003-03-25", "55", "1", "55", "0", "0", "1", "0", null},
			// 7 - to_process = 0
			{ "2003-02-11", "1", "8", "6", "2003-03-15", "102", "0", "102", "0", "0", "1", "55", null},
			
			// user 9, all apply
			// 8 - good
			{ "2003-03-21", "1", "9", null, "2003-03-15", "102", "1", "102", "0", "0", "1", "0", null},
			// 9 - good
			{ "2001-01-11", "1", "9", null, "2001-01-15", "232.3", "1", "232.3", "0", "0", "1", "0", null},
			// 10 - good
			{ "2003-03-01", "1", "9", null, "2003-03-25", "55", "1", "55", "0", "0", "1", "0", null},

			// user 10, none apply
			// 11 - to_process = 0
			{ "2003-02-11", "1", "10", "2", "2003-03-15", "102", "0", "102", "0", "0", "1", "2.3", null},
			// 12 - due date yet to come
			{ "2003-02-11", "1", "10", null, "2003-05-25", "1022", "1", "1022", "0", "0", "1", "0", null},
			// 13 - to_process = 0
			{ "2003-03-11", "1", "10", null, "2003-04-25", "2.3", "0", "2.3", "0", "0", "1", "0", null},

			// user 11, none apply
			// 14 - to_process = 0
			{ "2001-02-11", "1", "11", "2", "2003-03-15", "102", "0", "102", "0", "0", "1", "2.3", null},
			// 15 - due date yet to come
			{ "2002-02-11", "1", "11", null, "2004-05-25", "12", "1", "12", "0", "0", "1", "0", null},
			// 16 - due date yet to come
			{ "2003-04-01", "1", "11", null, "2003-04-30", "2.3", "1", "2.3", "0", "0", "1", "0", null},

			// user 12, all apply
			// 17 - good
			{ "2002-03-21", "1", "12", null, "2003-03-15", "1892", "1", "1892", "0", "0", "1", "0", null},
			// 18 - good
			{ "2001-01-11", "1", "12", null, "2001-01-15", "2326.3", "1", "2326.3", "0", "0", "1", "0", null},
			// 19 - good
			{ "2003-03-01", "1", "12", null, "2002-03-15", "42", "1", "42", "0", "0", "1", "0", null},

			// user 13, some apply
			// 20 - good
			{ "2002-04-21", "1", "13", null, "2003-02-11", "132", "1", "132", "0", "0", "1", "0", null},
			// 21 - good
			{ "2001-04-11", "1", "13", null, "2001-02-05", "2436.3", "1", "2436.3", "0", "0", "1", "0", null},
			// 22 - good
			{ "2003-04-01", "1", "13", null, "2002-02-13", "425", "1", "425", "0", "0", "1", "0", null},
			// 23 - to_process = 0
			{ "2001-02-11", "1", "13", "2", "2003-03-15", "102", "0", "102", "0", "0", "1", "2.3", null},
            
            // user 14 - belongs to entity 2!
            // 24 - 
            { "2001-02-11", "1", "14", "2", "2003-03-15", "102", "0", "102", "0", "0", "1", "2.3", null},
            
            // user 17 - only for test with status
            { "2001-02-11", "1", "17", "2", "2003-03-15", "102", "1", "90", "0", "0", "1", "0", null},
			
        };
        addTable(Constants.TABLE_INVOICE, invoiceColumns, invoiceData, false);

        //INVOICE_LINE
        String invoiceLineColumns[] =
        {
            "id",
            "invoice_id",
            "type_id",
            "description",
            "amount",
            "quantity",
            "price",
            "deleted"
        };
        String invoiceLineData[][] = null;
        columnsMetaData = new Column[9];
        columnIdx = 0;
        // invoice_id
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1;
        columnMetaData.intRange2 = 23; 
        columnsMetaData[columnIdx++] = columnMetaData;
       // type_id
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1;
        columnMetaData.intRange2 = 4; 
        columnsMetaData[columnIdx++] = columnMetaData;
        // description
        columnMetaData = new Column();
        columnMetaData.dataType = "str";
        columnMetaData.isNull = false;
        columnsMetaData[columnIdx++] = columnMetaData;
        // amount
        columnMetaData = new Column();
        columnMetaData.dataType = "float";
        columnMetaData.isNull = false;
        columnMetaData.floatFactor = 1000F; 
        columnsMetaData[columnIdx++] = columnMetaData;
        // quantity
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.isNull = false;
        columnMetaData.intRange1 = 1; 
        columnMetaData.intRange2 = 1000;
        columnsMetaData[columnIdx++] = columnMetaData;
        // price
        columnMetaData = new Column();
        columnMetaData.dataType = "float";
        columnMetaData.isNull = false;
        columnMetaData.floatFactor = 1000F; 
        columnsMetaData[columnIdx++] = columnMetaData;
        // deleted
        columnMetaData = new Column();
        columnMetaData.dataType = "int";
        columnMetaData.constantValue = "0";
        columnsMetaData[columnIdx++] = columnMetaData;
        
        addTable(Constants.TABLE_INVOICE_LINE, invoiceLineColumns, invoiceLineData, null, true,
                columnsMetaData, 250, 500);


        //PAYMENT_RESULT
        String paymentResultColumns[] =
        {
            "id",
        };
        String paymentResultData[][] = { null, null, null, null };
        String paymentResultIntColumns[][][] = { 
            { 
                { "description", "Successful" }
            }, 
            {
                { "description", "Failed" }
            },
            {
                { "description", "Processor unavailable" }
            },
            {
                { "description", "Entered" } // for cheques
            },
        };
        addTable(Constants.TABLE_PAYMENT_RESULT, paymentResultColumns, 
                paymentResultData, paymentResultIntColumns, false);


        //PAYMENT
        String paymentColumns[] =
        {
            "id",
            "user_id",
            "attempt",
            "result_id",
            "amount",
            "create_datetime",
            "payment_date",
            "method_id",
            "credit_card_id",
            "deleted",
            "is_refund",
            "payment_id",
            "currency_id",
            "payout_id"
        };
        String paymentData[][] = {
            {"6", "1", "1", "100", "2004-01-01", "2004-01-01","1",null, "0","0", null,"1", null},
            {"15", "1", "1","100", "2004-01-01", "2004-01-01","1",null, "0","0", null,"1", null},
            {"7", "1", "1", "100", "2004-02-15", "2004-02-15","1",null, "0","0", null,"1", null},
            {"7", "1", "1", "100", "2004-02-16", "2004-02-16","1",null, "0","1", "3",  "1", null},
            {"7", "1", "1", "100", "2004-03-10", "2004-03-10","1",null, "0","0", null,"2", null},
            {"7", "1", "1", "100", "2004-03-15", "2004-03-15","1",null, "0","0", null,"3", null},
            {"10", "1", "1","100", "2004-01-16", "2004-01-16","1",null, "0","0", null,"4", null},
            {"10", "1", "1","100", "2004-01-30", "2004-01-30","1",null, "0","0", null,"5", null},
            {"11", "1", "1","100", "2004-02-17", "2004-02-17","1",null, "0","0", null,"6", null},
            {"11", "1", "2","100", "2004-02-17", "2004-02-01","1",null, "0","0", null,"6", null},
            {"16", "1", "1","100", "2004-03-01", "2004-03-01","1",null, "0","0", null,"1", null},
            {"16", "1", "1","100", "2004-03-09", "2004-03-09","1",null, "0","0", null,"2", null},
            {"16", "1", "1","100", "2004-03-10", "2004-03-10","1",null, "0","0", null,"3", null},
            {"16", "1", "1","100", "2004-03-14", "2004-03-14","1",null, "0","0", null,"4", null},
        };
        addTable(Constants.TABLE_PAYMENT, paymentColumns, paymentData, false);

        //PAYMENT_INFO_CHEQUE
        String paymentInfoChequeColumns[] =
        {
            "id",
            "payment_id",
            "bank",
            "cheque_number",
            "cheque_date",
        };
        String paymentInfoChequeData[][] = null;
        addTable(Constants.TABLE_PAYMENT_INFO_CHEQUE, paymentInfoChequeColumns, 
                paymentInfoChequeData, false);

        // ACH
        String achColumns[] =
        {
            "id",
			"user_id",
			"aba_routing",
            "bank_account",
			"account_type",
			"bank_name",
			"account_name",
        };
        String achData[][] = null;
        addTable(Constants.TABLE_ACH, achColumns, achData, false);
        
        //CREDIT_CARD
        String creditCardColumns[] =
        {
            "id",
            "cc_number",
            "cc_expiry",
            "name",
            "cc_type",
            "deleted"
        };
        String creditCardData[][] = {
            {"4111111111111111", "2005-10-31", "Peter K. Chisker Jr.", "2", "0"},
            {"4111111111111111", "2005-10-31", "Partner1", "2", "0"},
            {"4111111111111111", "2005-10-31", "Partner2", "1", "0"},
            {"4111111111111111", "2005-10-31", "Partner3", "1", "0"},
        };
        addTable(Constants.TABLE_CREDIT_CARD, creditCardColumns, 
                creditCardData, false);

        //USER_CREDIT_CARD_MAP
        String userCreditCardMapColumns[] =
        {
            "user_id",
            "credit_card_id",
        };
        String userCreditCardMapData[][] = {
            {"9", "1"},
            {"6", "2"},
            {"8", "3"},
            {"15", "4"},
        };
        addTable(Constants.TABLE_USER_CREDIT_CARD_MAP, userCreditCardMapColumns, 
                userCreditCardMapData, false);


        //EVENT_LOG_MODULE
        String eventLogModuleColumns[] =
        {
            "id",
        };
        String eventLogModuleData[][] = { null, null, null, null, null, null,
                null, null, null };
        String eventLogModuleIntColumns[][][] =
        { 
            {{ "description", "Billing Process" }},
            {{ "description", "User maintenance" }},
            {{ "description", "Item maintenance" }},
            {{ "description", "Item type maintenance" }},
            {{ "description", "Item user price maintenance" }},
            {{ "description", "Promotion maintenance" }},
            {{ "description", "Order maintenance" }},
            {{ "description", "Credit card maintenance" }},
            {{ "description", "Invoice maintenance" }},
        };
         
        addTable(Constants.TABLE_EVENT_LOG_MODULE, eventLogModuleColumns, 
                eventLogModuleData, eventLogModuleIntColumns, false);

        //EVENT_LOG_MESSAGE
        String eventLogMessageColumns[] =
        {
            "id",
        };
        String eventLogMessageData[][] = { null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null };
        String eventLogMessageIntColumns[][][] =
        {    // billing process
            {{ "description",  //1
                "A prepaid order has unbilled time before the billing process date" }},
            {{ "description", //2
                "Order has no active time at the date of process." }},
            {{ "description", //3
                "At least one complete period has to be billable." }},
            {{ "description", //4
                "Already billed for the current date." }},
            {{ "description", //5
                "This order had to be maked for exclusion in the last process." }},
            {{ "description", //6
                "Pre-paid order is being process after its expiration." }},
            // user maintenance
            {{ "description", //7
                "A row was marked as deleted." }},
            {{ "description", //8
               "A user password was changed." }},
            // general update of a row          
            {{ "description", //9
               "A row was updated." }},
            {{ "description", //10
               "Running a billing process, but a review is found unapproved." }},
            {{ "description", //11
               "Running a billing process, review is required but not present." }},
            {{ "description", //12
               "A user status was changed." }},
            {{ "description", //13
               "An order status was changed." }},
            {{ "description", //14
               "A user had to be aged, but there's no more steps configured." }},
            {{ "description", //15
               "A partner has a payout ready, but no payment instrument." }},
            {{ "description", //16
               "A purchase order as been manually applied to an invoice." }},
        };
        addTable(Constants.TABLE_EVENT_LOG_MESSAGE, eventLogMessageColumns, 
                eventLogMessageData, eventLogMessageIntColumns, false);


        //EVENT_LOG
        String eventLogColumns[] =
        {
            "id",
            "entity_id",
            "user_id",
            "table_id",
            "foreign_id",
            "create_datetime",
            "level",
            "module_id",
            "message_id",
            "old_num",
            "old_str",
            "old_date"
        };
        String eventLogData[][] = null;
        addTable(Constants.TABLE_EVENT_LOG, eventLogColumns, eventLogData, false);


        //ORDER_PROCESS
        String biPeriodsColumns[] =
        {
            "id",
            "order_id",
            "invoice_id",
            "billing_process_id",
            "periods_included",
            "period_start",
            "period_ends"
        };
        String biPeriodsData[][] = null;
        addTable(Constants.TABLE_ORDER_PROCESS, biPeriodsColumns, biPeriodsData, false);


        //PREFERENCE_TYPE
        String preferenceTypeColumns[] =
        {
            "id",
        };
        String preferenceTypeMessageData[][] = { null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null
        };
        String preferenceTypeIntColumns[][][] =
        { 
          { { "description", "Process payment with billing process" } },
          { { "description", "URL of CSS file" } },
          { { "description", "URL of logo graphic" } },
          { { "description", "Grace period" } }, //4
          { { "description", "Partner percentage rate" } },
          { { "description", "Partner referral fee" } },
          { { "description", "Partner one time payout" } }, //7
          { { "description", "Partner period unit payout" } },
          { { "description", "Partner period value payout" } },
          { { "description", "Partner automatic payout" } }, //10
          { { "description", "User in charge of partners " } },
          { { "description", "Partner fee currency" } }, //12
          { { "description", "Self delivery of paper invoices"} }, //13
          { { "description", "Include customer notes in invoice"} }, //14
          { { "description", "Days before expiration for order notification 1"} }, //15
          { { "description", "Days before expiration for order notification 2"} }, //16
          { { "description", "Days before expiration for order notification 3"} }, //17
          { { "description", "Invoice number prefix"} }, //18
          { { "description", "Invoice number"} }, //19
          { { "description", "Allow invoice deletion"} }, //20
        };
        addTable(Constants.TABLE_PREFERENCE_TYPE, preferenceTypeColumns, 
                preferenceTypeMessageData, preferenceTypeIntColumns, false);

        //PREFERENCE
        String preferenceColumns[] =
        {
            "id",
            "type_id",
            "table_id",
            "foreign_id",
            "int_value",
            "str_value",
            "float_value"
        };
        // the table_id = 5, foregin_id = 1, means entity = 1
        String preferenceData[][] = {
            {"1", "5", "1", "1", null, null },
            //{"1", "2", "9", "1", null, null },
            {"2", "5", "1", null, "/billing/css/sapienter.css", null },
            {"3", "5", "1", null, "/billing/graphics/logo.gif", null },
            {"4", "5", "1", "5", null, null },
            {"5", "5", "1", null, null, "10" },
            {"6", "5", "1", null, null, "0" },
            {"7", "5", "1", "0",  null, null },
            {"8", "5", "1", "1", null, null },
            {"9", "5", "1", "3", null, null },
            {"10","5", "1", "1", null, null },
            {"11","5", "1", "3", null, null },
            {"12","5", "1", "1", null, null },
            {"13","5", "1", "1", null, null },
			{"14","5", "1", "1", null, null },
			{"15","5", "1", "30", null, null },
            {"17","5", "1", "10", null, null },
            {"20","5", "1", "1", null, null },
        };
        addTable(Constants.TABLE_PREFERENCE, preferenceColumns, 
                preferenceData, false);


        //NOTIFICATION_MESSAGE_TYPE
        String notificationMessageTypeColumns[] = {
            "id", "sections"
        };
        String notificationMessageTypeMessageData[][] = { 
            {"2"}, {"2"},{"2"},{"2"},{"2"},{"2"},{"2"},{"2"},{"2"},{"2"},
            {"2"}, {"2"}, {"2"}, {"2"}, {"2"}, {"2"}, {"2"}
        };
        String notificationMessageTypeIntColumns[][][] =
        { 
            { { "description", "Invoice (email)" } },   //1
            { { "description", "User Reactivated" } },  //2
            { { "description", "User Overdue" } },     //3 
            { { "description", "User Overdue 2" } },   //4
            { { "description", "User Overdue 3" } },   //5
            { { "description", "User Suspended" } },   //6
            { { "description", "User Suspended 2" } }, //7
            { { "description", "User Suspended 3" } }, //8
            { { "description", "User Deleted" } },     //9
            { { "description", "Payout Remainder" } }, //10
            { { "description", "Partner Payout" } }, //11
            { { "description", "Invoice (paper)" } },   //12
            { { "description", "Order about to expire. Step 1" } },   //13
            { { "description", "Order about to expire. Step 2" } },   //14
            { { "description", "Order about to expire. Step 3" } },   //15
            { { "description", "Payment (success)" } },   //16
            { { "description", "Payment (fail)" } },   //17
        };
        addTable(Constants.TABLE_NOTIFICATION_MESSAGE_TYPE, 
                notificationMessageTypeColumns, 
                notificationMessageTypeMessageData, 
                notificationMessageTypeIntColumns, false);

        //NOTIFICATION_MESSAGE
        String notificationMessageColumns[] =
        {
            "id",
            "type_id",
            "entity_id",
            "language_id",
        };
        String notificationMessageData[][] = {
            {"1", "1", "1" },
            {"2", "1", "1" },
            {"3", "1", "1" },
            {"4", "1", "1" },
            {"5", "1", "1" },
            {"6", "1", "1" },
            {"7", "1", "1" },
            {"8", "1", "1" },
            {"9", "1", "1" },
            {"10","1", "1" },
            {"11","1", "1" },
            {"12","1", "1" },
            {"10","2", "1" }, //13
            {"11","2", "1" }, //14
			{"13","1", "1" }, //15
            {"15","1", "1" }, //16
        };
        addTable(Constants.TABLE_NOTIFICATION_MESSAGE, notificationMessageColumns, 
                notificationMessageData, false);

        //NOTIFICATION_MESSAGE_SECTION
        String notificationMessageSectionColumns[] =
        {
            "id",
            "message_id",
            "section"
        };
        String notificationMessageSectionData[][] = {
            {"1", "1" }, {"1", "2" },
            {"2", "1" }, {"2", "2" },
            {"3", "1" }, {"3", "2" },
            {"4", "1" }, {"4", "2" },
            {"5", "1" }, {"5", "2" },
            {"6", "1" }, {"6", "2" },
            {"7", "1" }, {"7", "2" },
            {"8", "1" }, {"8", "2" },
            {"9", "1" }, {"9", "2" },
            {"10", "1" }, {"10", "2" },
            {"11", "1" }, {"11", "2" },
            {"12", "1" }, {"12", "2" },
            {"13", "1" }, {"13", "2" },
            {"14", "1" }, {"14", "2" },
			{"15", "1" }, {"15", "2" },
            {"16", "1" }, {"16", "2" },
        };
        addTable(Constants.TABLE_NOTIFICATION_MESSAGE_SECTION, 
                notificationMessageSectionColumns, 
                notificationMessageSectionData, false);

        //NOTIFICATION_MESSAGE_LINE
        String notificationMessageLineColumns[] =
        {
            "id",
            "message_section_id",
            "content"
        };
        String notificationMessageLineData[][] = {
            {"1", "Your invoice is ready" },
            {"2", "This is your innvoice, the total is |total| and the " +                  "due date is |due date|." },
            {"2", "\nThank you!" },
            {"3", "Your account has been reactivated" },
            {"4", "Your account is active again. " },
            {"5", "Your account is overdue" },
            {"6", "Please pay your latest invoice that totals |total|. " },
            {"7", "Your account is still overdue" },
            {"8", "Please pay your latest invoice that totals |total|. " },
            {"9", "Your account will be suspended" },
            {"10", "Please pay your latest invoice that totals |total|. " },
            {"11", "Your account has be suspended" },
            {"12", "Please pay your latest invoice that totals |total| for reactivation. " },
            {"13", "Your account is still suspended" },
            {"14", "Please pay your latest invoice that totals |total| for reactivation. " },
            {"15", "Your account will be terminated" },
            {"16", "Please pay your latest invoice that totals |total|. " },
            {"17", "Your account has been terminated" },
            {"18", "Your balance is |total|. Collection will be persued by a collection agency" },
            {"19", "Partner is due for a payout" },
            {"20", "The partner |partner_id| should be paid |total|" },
            {"21", "A payment has been made from |company|" },
            {"22", "A payment for the period starting |period_start| and ending |period_end| for a total of |total| has been made. Thank you for your business." },
            {"23", "Please visit our website to find out our latest offers!" },
            {"24", "Thanks for using our services. We appreciate your business." },
            {"25", "Partner is due for a payout" },
            {"26", "The partner |partner_id| should be paid |total|" },
            {"27", "A payment has been made from |company|" },
            {"28", "A payment for the period starting |period_start| and ending |period_end| for a total of |total| has been made. Thank you for your business." },
            {"29", "Your subscription is about to expire" },
            {"30", "Your subscription that started on |period_start| will expire on |period_end|" },
            {"31", "Your subscription is about to expire!!" },
            {"32", "This is your final notification. Your subscription that started on |period_start| will expire on |period_end|" },
            
        };
        addTable(Constants.TABLE_NOTIFICATION_MESSAGE_LINE, 
                notificationMessageLineColumns, 
                notificationMessageLineData, false);

        //NOTIFICATION_MESSAGE_ARCHIVE
        String notificationMessageArchiveColumns[] =
        {
            "id",
            "create_datetime",
            "type_id",
            "user_id",
            "result_message"
        };
        String notificationMessageArchiveData[][] = null;
        addTable(Constants.TABLE_NOTIFICATION_MESSAGE_ARCHIVE, 
                notificationMessageArchiveColumns, 
                notificationMessageArchiveData, false);

        //NOTIFICATION_MESSAGE_ARCHIVE_LINE
         String notificationMessageArchiveLineColumns[] =
         {
             "id",
             "message_archive_id",
             "content"
         };
         String notificationMessageArchiveLineData[][] = null;
         addTable(Constants.TABLE_NOTIFICATION_MESSAGE_ARCHIVE_LINE, 
                 notificationMessageArchiveLineColumns, 
                 notificationMessageArchiveLineData, false);


        //PERMISSION_TYPE
        String permissionTypeColumns[] =
        {
            "id",
            "description",
        };
        String permissionTypeData[][] = {
            {"Menu option"} ,  // 1
            {"User creation"}, // 2
            {"User edition"},  // 3
            {"Item edition"},  // 4
            {"Reports"},       // 5
            {"Orders"},        // 6
            {"Invoices"},      // 7
        };
        addTable(Constants.TABLE_PERMISSION_TYPE, 
                permissionTypeColumns, permissionTypeData, false);

        //PERMISSION
        String permissionColumns[] =
        {
            "id", "type_id", "foreign_id"
        };
        String permissionData[][] = {
            {"1", "1"}, // 1 menu
            {"1", "2"}, // 2
            {"1", "3"}, // 3
            {"1", "4"}, // 4 menu - system
            {"1", "5"}, // 5 menu - users
            {"1", "6"}, // 6 menu - users - all
            {"2", null}, // 7 user create - specify type
            {"2", null}, // 8 user create - type root
            {"2", null}, // 9 user create - type clerk
            {"2", null}, // 10 user create - type partner
            {"2", null}, // 11 user create - type customer
            {"3", null}, // 12 user edit - change entity 
            {"3", null}, // 13 user edit - change type
            {"3", null}, // 14 user edit - view type
            {"3", null}, // 15 user edit - change username
            {"3", null}, // 16 user edit - change password
            {"3", null}, // 17 user edit - change language
            {"3", null}, // 18 user edit - view language
            {"1", "7"},  // 19 menu - users - list
            {"3", null}, // 20 user edit - edit user status
            {"3", null}, // 21 user edit - view user status
            {"1", "8"},  // 22 menu - account
            {"1", "9"},  // 23 menu - 
            {"1", "10"}, // 24 menu -
            {"1", "11"}, // 25 menu -
            {"1", "12"}, // 26 menu - change password sub-option
            {"1", "13"}, // 27 menu - edit contact sub-option
            {"1", "14"}, // 28 menu - logout
            {"1", "15"}, // 29 menu - items
            {"1", "16"}, // 30 menu - Items - Types
            {"1", "17"}, // 31 menu - Items - Create
            {"1", "18"}, // 32 menu - Items - List
            {"1", "19"}, // 33 menu - Items - Types - Create
            {"4", null}, // 34 item edit - can edit an item
            {"1", "20"}, // 35 menu - Items - Types - List            
            {"1", "21"}, // 36 menu - Promotion
            {"1", "22"}, // 37 menu - Promotion - Create
            {"1", "23"}, // 38 menu - Promotion - List
            {"1", "24"}, // 39 menu - Payments - create cheque
            {"1", "25"}, // 40 menu - Payments - create cc
            {"1", "26"}, // 41 menu - Payments - List
            {"1", "27"}, // 42 menu - Orders - Create
            {"1", "28"}, // 43 menu - Orders - List
            {"1", "29"}, // 44 menu - user edit - edit credit card
            {"1", "30"}, // 45 menu - user edit - edit credit card (f cus)
            {"1", "31"}, // 46 menu - Refund
            {"1", "32"}, // 47 menu - Refund - cheque
            {"1", "33"}, // 48 menu - Refund - credit card
            {"1", "34"}, // 49 menu - Refund - list
            {"1", "35"}, // 50 menu - Invoice
            {"1", "36"}, // 51 menu - Invoice - list
            {"1", "37"}, // 52 menu - Process 
            {"1", "38"}, // 53 menu - Process - list
            {"1", "39"}, // 54 menu - Process - edit config
            {"1", "40"}, // 55 menu - Process - latest
            {"1", "41"}, // 56 menu - Process - prelim report
            {"1", "42"}, // 57 menu - Notifications
            {"1", "43"}, // 58 menu - Notifications - Compose
            {"1", "44"}, // 59 menu - Notifications - Parameters
            {"1", "45"}, // 60 menu - Notifications - Emails list
            {"1", "46"}, // 61 menu - Customer
            {"1", "47"}, // 62 menu - Reports - Orders
            {"1", "48"}, // 63 menu - Reports - Invoice
            {"1", "49"}, // 64 menu - Reports - Payment
            {"1", "50"}, // 65 menu - Reports - Refund
            {"1", "51"}, // 66 menu - Reports - Customer
            {"5",  "1"}, // 67 report - g. orders
            {"5",  "2"}, // 68 report - g. invoices
            {"5",  "3"}, // 69 report - g. payments
            {"5",  "4"}, // 70 report - g. order lines
            {"5",  "5"}, // 71 report - g. refunds
            {"5",  "6"}, // 72 report - total invoiced by date
            {"5",  "7"}, // 73 report - total paymets by date
            {"5",  "8"}, // 74 report - total refunds by date
            {"5",  "9"}, // 75 report - total ordered by date
            {"5", "10"}, // 76 report - invoices overdue
            {"1", "52"}, // 77 menu - Customer list
            {"1", "53"}, // 78 menu - report list
            {"1", "54"}, // 79 menu - Customer - New
            {"1", "55"}, // 80 menu - System - Branding
            {"3", null}, // 81 user edit - change currency
            {"3", null}, // 82 user edit - view currency
            {"1", "56"}, // 83 menu - System - Currency
            {"5", "11"}, // 84 report - invoices carring a balance
            {"1", "57"}, // 85 menu - System - Ageing
            {"1", "6"}, //  86 menu - users - all - create
            {"1", "59"}, // 87 menu - users - partner
            {"1", "60"}, // 88 menu - users - customers
            {"1", "61"}, // 89 menu - users - new partner
            {"1", "62"}, // 90 menu - users - partner list
            {"1", "63"}, // 91 menu - users - partner defaults
            {"1", "64"}, // 92 menu - users - partner reports
            {"1", "65"}, // 93 menu - users - customers reports
            {"1", "66"}, // 94 menu - users - customers list
            {"1", "67"}, // 95 menu - users - customers new
            {"1", "68"}, // 96 menu - statement
            {"1", "69"}, // 97 menu - statement - latest
            {"1", "70"}, // 98 menu - statement - payouts
            {"5", "12"}, // 99 report - selected partner: customers orders
            {"5", "13"}, //100 report - selected partner: customers payments
            {"5", "14"}, //101 report - selected partner: customers refunds
            {"5", "15"}, //102 report - general partners
            {"5", "16"}, //103 report - general payouts
            {"1", "71"}, //104 menu - reports (for partners)
            {"1", "72"}, //105 menu - users - partners - due payment
            {"1", "73"}, //106 menu - reports list (for partners)
            {"1", "74"}, //107 menu - notification preferences
			{"6", null}, //108 orders - show left menu with options
			{"1", "75"}, //109 menu - payment - ach
            {"1", "76"}, //110 menu - invoice - numbering
            {"5", "17"}, //111 report - invoice lines
            {"5", "18"}, //112 report - users
            {"7", null}, //113 invoices - can delete
            {"3", null}, //114 user edit - links to edition of cc/contact/ach, etc
            {"2", null}, //115 user create - link in initial page - left bar
            {"1", "77"}, //116 menu - edit ach for customers
        };
        String permissionIntColumns[][][] =
        { 
            { { "description", "Menu Order option" },
              { "title", "Menu Order option" } },
            { { "description", "Menu Payment option" },
              { "title", "Menu Payment option" } },
            { { "description", "Menu Report option" },
              { "title", "Menu Report option" } },
            { { "description", "Menu System option" },
              { "title", "Menu System option" } },
            { { "description", "Menu System-User option" },
              { "title", "Menu System-User option" } },
            { { "description", "Menu System-User-All option" },
              { "title", "Menu System-User-All option" } },
            { { "description", "Selection of user type when creating a user" }, // 
              { "title", "User type selection" } },
            { { "description", "Can create root users" },
              { "title", "Can create root users" } },
            { { "description", "Can create clerk users" },
              { "title", "Can create clerk users" } },
            { { "description", "Can create partner users" },
              { "title", "Can create partner users" } },
            { { "description", "Can create customer users" },
              { "title", "Can create customer users" } },
            { { "description", "Can change entity when editing a user" },
              { "title", "Can change entity" } },
            { { "description", "Can change type when editing a user" },
              { "title", "Can change type" } },
            { { "description", "Can view type when editing a user" },
              { "title", "Can view type" } },
            { { "description", "Can change username when editing a user" },
              { "title", "Can change username" } },
            { { "description", "Can change password when editing a user" },
              { "title", "Can change password" } },
            { { "description", "Can change langauge when editing a user" },
              { "title", "Can change langauge" } },
            { { "description", "Can view language when editing a user" },
              { "title", "Can view language" } },
            { { "description", "Menu System-User-Maintain option" },
              { "title", "Menu System-User-Maintain option" } },
            { { "description", "Can change user status when editing a user" },
              { "title", "Can change status" } },
            { { "description", "Can view status when editing a user" },
              { "title", "Can view status" } },
            { { "description", "Menu Account sub-option" },
              { "title", "Menu Account sub-option" } },
            { { "description", "Menu change password lm-option" },
              { "title", "Menu change password lm-option" } },
            { { "description", "Menu edit contact info lm-option" },
              { "title", "Menu edit contact info lm-option" } },
            { { "description", "Menu account option" },
              { "title", "Menu account option" } },
            { { "description", "Menu change password sub-option" },
              { "title", "Menu change password sub-option" } },
            { { "description", "Menu edit contact info sub-option" },
              { "title", "Menu edit contact info sub-option" } },
            { { "description", "Menu logout option" },
              { "title", "Menu logout option" } },
            { { "description", "Menu items option" },
              { "title", "Menu items option" } },
            { { "description", "Menu Items - Types option" },
              { "title", "Menu Items - Types option" } },
            { { "description", "Menu Items - Create option" },
              { "title", "Menu Items - Create option" } },
            { { "description", "Menu Items - List option" },
              { "title", "Menu Items - List option" } },
            { { "description", "Menu Items - Types - Create option" },
              { "title", "Menu Items - Types - Create option" } },
            { { "description", "Can edit item fields (read-write)" },
              { "title", "Can edit item fields" } },
            { { "description", "Menu Items - Types - List option" },
              { "title", "Menu Items - Types - List option" } },
            { { "description", "Menu Promotion" },
              { "title", "Menu Promotion" } },
            { { "description", "Menu Promotion create" },
              { "title", "Menu Promotion create" } },
            { { "description", "Menu Promotion list" },
              { "title", "Menu Promotion list" } },
            { { "description", "Menu Payments new cheque" },
              { "title", "Menu Payments new cheque" } },
            { { "description", "Menu Payments new credit card" },
              { "title", "Menu Payments new cc" } },
            { { "description", "Menu Payments list" },
              { "title", "Menu Payments list" } },
            { { "description", "Menu Order create" },
              { "title", "Menu Order create" } },
            { { "description", "Menu Order list" },
              { "title", "Menu Order list" } },
            { { "description", "Menu Credit Card edit" },
              { "title", "Menu Credit Card" } },
            { { "description", "Menu Credit Card edit" },
              { "title", "Menu Credit Card" } },
            { { "description", "Menu Refund option" },
              { "title", "Menu Refund option" } },
            { { "description", "Menu Refund option - cheque" },
              { "title", "Menu Refund option - cheque" } },
            { { "description", "Menu Refund option - credit card" },
              { "title", "Menu Refund option - cc" } },
            { { "description", "Menu Refund option - list" },
              { "title", "Menu Refund list" } },
            { { "description", "Menu Invoice option" },
              { "title", "Menu Invoice" } },
            { { "description", "Menu Invoice option - list" },
              { "title", "Menu Invoice list" } },
            { { "description", "Menu Process option" },
              { "title", "Menu Process option" } },
            { { "description", "Menu Process - list option" },
              { "title", "Menu Process list" } },
            { { "description", "Menu Process - edit configuration option" },
              { "title", "Menu Process - Configuration" } },
            { { "description", "Menu Process - see latest option" },
              { "title", "Menu Process - Latest" } },
            { { "description", "Menu Process - Review option" },
              { "title", "Menu Process review" } },
            { { "description", "Menu Notification" },
              { "title", "Menu Notification" } },
            { { "description", "Menu Notification - Compose" },
              { "title", "Menu Notification Compose" } },
            { { "description", "Menu Notification - Parameters" },
              { "title", "Menu Notificaiton Parameters" } },
            { { "description", "Menu Notification - Emails list" },
              { "title", "Menu Notification Emails" } },
            { { "description", "Menu Customers " },
              { "title", "Menu Customers" } },
            { { "description", "Menu Reports - Orders" },
              { "title", "Menu Reports Orders" } },
            { { "description", "Menu Reports - Invoice" },
              { "title", "Menu Reports Invoice" } },
            { { "description", "Menu Reports - Payment" },
              { "title", "Menu Reports Payment" } },
            { { "description", "Menu Reports - Refund" },
              { "title", "Menu Reports Refund" } },
            { { "description", "Menu Reports - Customer" },
              { "title", "Menu Reports Customer" } },
            { { "description", "Report General orders" },
              { "title", "Report General orders" } },
            { { "description", "Report General invoices" },
              { "title", "Report General invoices" } },
            { { "description", "Report General payments" },
              { "title", "Report General payments" } },
            { { "description", "Report General order lines" },
              { "title", "Report General order lines" } },
            { { "description", "Report General Refunds " },
              { "title", "Report General Refunds " } },
            { { "description", "Report Total invoiced by date range " },
              { "title", "Report Total invoiced " } },
            { { "description", "Report Total payments by date range " },
              { "title", "Report Total payments" } },
            { { "description", "Report Total refunds by date range " },
              { "title", "Report Total refunds" } },
            { { "description", "Report Total ordered by date range " },
              { "title", "Report Total ordered" } },
            { { "description", "Invoices overdue " },
              { "title", "Report Invoices overdue " } },
            { { "description", "Menu Customer option - list" },
              { "title", "Menu Customer list" } },
            { { "description", "Menu Report option - list" },
              { "title", "Menu Report list" } },
            { { "description", "Menu Customer option - new" },
              { "title", "Menu Customer New" } },
            { { "description", "Menu System option - Branding" },
              { "title", "Menu System - Branding" } },
            { { "description", "Can change currency when editing a user" },
              { "title", "Can change langauge" } },
            { { "description", "Can view currency when editing a user" },
              { "title", "Can view language" } },
            { { "description", "Menu System option - Currencies" },
              { "title", "Menu System - Currencies" } },
            { { "description", "Invoices carring a balance " },
              { "title", "Report Invoices carring a balance " } },
            { { "description", "Menu System option - Ageing" },
              { "title", "Menu System - Ageing" } },
            { { "description", "Menu Users option - All create" },
              { "title", "Menu Users - All create" } },
            { { "description", "Menu Partner option" },
              { "title", "Menu Partner" } },
            { { "description", "Menu Customer option" },
              { "title", "Menu Customer" } },
            { { "description", "Menu Partner option - New" },
              { "title", "Menu Partner - New" } },
            { { "description", "Menu Partner option - List" },
              { "title", "Menu Partner - List" } },
            { { "description", "Menu Partner option - Defaults" },
              { "title", "Menu Partner - Defaults" } },
            { { "description", "Menu Partner option - Reports" },
              { "title", "Menu Partner - Reports" } },
            { { "description", "Menu Customer option - Reports" },
              { "title", "Menu Customer - Reports" } },
            { { "description", "Menu Customer option - List" },
              { "title", "Menu Customer - List" } },
            { { "description", "Menu Customer option - New" },
              { "title", "Menu Customer - New" } },
            { { "description", "Menu Statement option" },
              { "title", "Menu Statement" } },
            { { "description", "Menu Statement option - latest" },
              { "title", "Menu Statement latest" } },
            { { "description", "Menu Statement option - Payouts" },
              { "title", "Menu Statement payouts" } },
            { { "description", "Report Partners customers orders" },
              { "title", "Report partner ordres" } },
            { { "description", "Report Partners customers payments" },
              { "title", "Report partner payments" } },
            { { "description", "Report partners customers refunds" },
              { "title", "Report partner refunds" } },
            { { "description", "Report General partners" },
              { "title", "Report General partners" } },
            { { "description", "Report General payouts" },
              { "title", "Report General payouts" } },
            { { "description", "Menu Reports parterns option" },
              { "title", "Menu Reports" } },
            { { "description", "Menu Partner option - due payment" },
              { "title", "Menu Partner - payable" } },
            { { "description", "Menu Reports list parterns option" },
              { "title", "Menu Reports List" } },
            { { "description", "Menu Notification preferences" },
              { "title", "Menu Notif Preferences" } },
            { { "description", "Order options: g.invoice, suspend, etc" },
            { "title", "Order left menu options" } },
            { { "description", "Menu Payment ACH" },
              { "title", "Menu Payment ACH" } },
            { { "description", "Menu Invoice Numbering" },
              { "title", "Menu Invoice Numbering" } },
            { { "description", "Menu Reports invoice details option" },
              { "title", "Menu Reports Invoice Details" } },
            { { "description", "Menu Reports users option" },
              { "title", "Menu Reports Users" } },
            { { "description", "Invoice delete left menu option" }, // 113
              { "title", "Invoice delete" } },
            { { "description", "User edit links" }, // 114
              { "title", "User edit links" } },
            { { "description", "User create - inital left menu" }, //115
              { "title", "User create - inital left menu" } },
            { { "description", "Menu edit ach option" }, //116
              { "title", "Menu edit ach" } },              
        };
        
        addTable(Constants.TABLE_PERMISSION, permissionColumns,
                permissionData, permissionIntColumns, false);

        //ROLE
        String roleColumns[] =
        {
            "id",
        };
        String roleData[][] = {
            {null}, {null}, {null}, {null}, {null}
        };
        // it is important that the first role is the most important, and from there
        // they go ordered in importance (desc)
        String roleIntColumns[][][] =
        { 
            { { "description", "An internal user with all the permissions" },
              { "title", "Internal" } },
            { { "description", "The super user of an entity" },
              { "title", "Super user" } },
            { { "description", "A billing clerk" },
              { "title", "Clerk" } },
            { { "description", "A partner that will bring customers" },
              { "title", "Partner" } },
            { { "description", "A customer that will query his/her account" },
              { "title", "Customer" } },

        };
        
        addTable(Constants.TABLE_ROLE, roleColumns,
                roleData, roleIntColumns, false);

        //PERMISSION_ROLE_MAP
        String permissionRoleColumns[] =
        {
            "permission_id",
            "role_id",
        };
        String permissionRoleData[][] = {
            // internal role
            {"1","1"}, // MENUS
            {"2","1"},
            {"3","1"},
            {"4","1"},  // menu - system
            {"80","1"}, // menu - system - Branding
            {"83","1"}, // menu - system - Currencies
            {"85","1"}, // menu - system - Ageing            
            {"5","1"},  // menu - users 
            {"87","1"}, // menu - users - partner
            {"89","1"}, // menu - users - partner - new
            {"90","1"}, // menu - users - partner - list
            {"91","1"}, // menu - users - partner - defaults
            {"92","1"}, // menu - users - partner - reports
            {"105","1"},// menu - users - partner - due payout
            {"88","1"}, // menu - users - customers
            {"93","1"}, // menu - users - customers - reports
            {"94","1"}, // menu - users - customers - list
            {"95","1"}, // menu - users - customers - new
            {"6","1"},  // menu - users - all
            {"86","1"}, // menu - users - all - create
            {"19","1"}, // menu - users - all - list
            {"22","1"}, // account sub option
            {"23","1"},
            {"24","1"},
            {"29","1"}, // menu - items 
            {"30","1"}, // menu - items - Types
            {"31","1"}, // menu - Items - Create
            {"32","1"}, // menu - Items - List
            {"33","1"}, // menu - Items - Types - Create
            {"35","1"}, // menu - Items - Types - List            
            {"36","1"}, // menu - Promotion
            {"37","1"}, // menu - Promotion - create
            {"38","1"}, // menu - Promotion - create
            {"39","1"}, // menu - Payment - create cheque
            {"40","1"}, // menu - Payment - create cc
            {"109","1"},// menu - Payment - create ach
            {"41","1"}, // menu - Payment - list
            {"42","1"}, // menu - Orders - create
            {"43","1"}, // menu - Orders - list
            {"44","1"}, // menu - User - credit card
            {"46","1"}, // menu - Refund
            {"47","1"}, // menu - Refund - cheque
            {"48","1"}, // menu - Refund - cc
            {"49","1"}, // menu - Refund - list
            {"50","1"}, // menu - Invoice
            {"51","1"}, // menu - Invoice - list
            {"110","1"},// menu - Invoice - numbering
            {"52","1"}, // menu - Process
            {"53","1"}, // menu - Process - list
            {"54","1"}, // menu - Process - edit config
            {"55","1"}, // menu - Process - see latest
            {"56","1"}, // menu - Process - prelim-report
            {"57","1"}, // menu - Notification
            {"58","1"}, // menu - Notification - configuration
            {"59","1"}, // menu - Notification - config - compose
            {"60","1"}, // menu - Notification - config - parameters
            {"107","1"},// menu - Notification - preferences
            {"62","1"}, // menu - Reports - Orders
            {"63","1"}, // menu - Reports - Invoice
            {"64","1"}, // menu - Reports - Payment
            {"65","1"}, // menu - Reports - Refund
            {"78","1"}, // menu - Reports - List
            {"67","1"}, // report - g. orders
            {"68","1"}, // report - g. invoices
            {"69","1"}, // report - g. payments
            {"70","1"}, // report - g. order lines
            {"71","1"}, // report - g. refunds
            {"72","1"}, // report - total invoiced by date
            {"73","1"}, // report - total paymets by date
            {"74","1"}, // report - total refunds by date
            {"75","1"}, // report - total ordered by date
            {"76","1"}, // report - invoices overdue
            {"84","1"}, // report - invoices carring a balance
            {"99","1"}, // report - partners customers orders
            {"100","1"},// report - partners customers payments
            {"101","1"},// report - partners customers refunds
            {"102","1"},// report - g, partners
            {"103","1"},// report - g. payouts
            {"111","1"},// report - invoice lines
            {"112","1"},// report - users
            {"7","1"}, // USER CREATE - all
            {"8","1"},
            {"9","1"},
            {"10","1"},
            {"11","1"},
            {"115","1"}, // create link in initial page
            {"12","1"}, // user edit - all the 'change' none of the 'views'
            {"13","1"},
            {"15","1"},
            {"16","1"},
            {"17","1"},
            {"20","1"},
            {"81","1"},
            {"114", "1"}, // links to edit of cc/contact/ach etc
            {"34","1"}, // item edit
			{"108", "1"}, // order left menu options
            {"113", "1"}, // invoice delete
            // entity root role
            {"1","2"}, // menus
            {"2","2"},
            {"3","2"},
            {"4","2"},
            {"80","2"}, // menu - system - Branding
            {"83","2"}, // menu - system - Currencies
            {"85","2"}, // menu - system - Ageing            
            {"5", "2"}, // menu - users 
            {"87","2"}, // menu - users - partner
            {"89","2"}, // menu - users - partner - new
            {"90","2"}, // menu - users - partner - list
            {"91","2"}, // menu - users - partner - defaults
            {"92","2"}, // menu - users - partner - reports
            {"105","2"},// menu - users - partner - due payout
            {"88","2"}, // menu - users - customers
            {"93","2"}, // menu - users - customers - reports
            {"94","2"}, // menu - users - customers - list
            {"95","2"}, // menu - users - customers - new
            {"6","2"},  // menu - users - all
            {"86", "2"},// menu - users - all - create
            {"19","2"}, // menu - users - all - list
            {"22","2"}, // account sub option
            {"23","2"},
            {"24","2"},
            {"29","2"}, // menu - items 
            {"30","2"}, // menu - items - Types
            {"31","2"}, // menu - Items - Create
            {"32","2"}, // menu - Items - List
            {"33","2"}, // menu - Items - Types - Create
            {"35","2"}, // menu - Items - Types - List     
            {"36","2"}, // menu - Promotion
            {"37","2"}, // menu - Promotion - create
            {"38","2"}, // menu - Promotion - create
            {"39","2"}, // menu - Payment - create cheque
            {"40","2"}, // menu - Payment - create cc
            {"109","2"},// menu - Payment - create ach
            {"41","2"}, // menu - Payment - list
            {"42","2"}, // menu - Orders - create
            {"43","2"}, // menu - Orders - list
            {"44","2"}, // menu - User - credit card
            {"46","2"}, // menu - Refund
            {"47","2"}, // menu - Refund - cheque
            {"48","2"}, // menu - Refund - cc
            {"49","2"}, // menu - Refund - list
            {"50","2"}, // menu - Invoice
            {"51","2"}, // menu - Invoice - list
            {"110","2"},// menu - Invoice - numbering
            {"52","2"}, // menu - Process
            {"53","2"}, // menu - Process - list
            {"54","2"}, // menu - Process - edit config
            {"55","2"}, // menu - Process - see latest
            {"56","2"}, // menu - Process - prelim-report
            {"57","2"}, // menu - Notification
            {"58","2"}, // menu - Notification - compose
            {"59","2"}, // menu - Notification - parameters
            {"60","2"}, // menu - Notification - emails list
            {"107","2"},// menu - Notification - preferences
            {"62","2"}, // menu - Reports - Orders
            {"63","2"}, // menu - Reports - Invoice
            {"64","2"}, // menu - Reports - Payment
            {"65","2"}, // menu - Reports - Refund
            {"67","2"}, // report - g. orders
            {"68","2"}, // report - g. invoices
            {"69","2"}, // report - g. payments
            {"70","2"}, // report - g. order lines
            {"71","2"}, // report - g. refunds
            {"72","2"}, // report - total invoiced by date
            {"73","2"}, // report - total paymets by date
            {"74","2"}, // report - total refunds by date
            {"75","2"}, // report - total ordered by date
            {"76","2"}, // report - invoices overdue
            {"84","2"}, // report - invoices carring a balance
            {"99","2"}, // report - partners customers orders
            {"100","2"},// report - partners customers payments
            {"101","2"},// report - partners customers refunds
            {"102","2"},// report - g, partners
            {"103","2"},// report - g. payouts
            {"111","2"},// report - invoice lines
            {"112","2"},// report - users
            {"78","2"}, // menu - Reports - List
            {"7","2"}, // user create - all but root type
            {"9","2"},
            {"10","2"},
            {"11","2"},
            {"115","2"}, // create link in initial page
            {"13","2"}, // user edit - all the 'change' but entity
           // {"15","2"}, don't allow root to change usernames, it's trouble
            {"16","2"},
            {"17","2"},
            {"20","2"},
            {"81","2"},
            {"114", "2"}, // links to edit of cc/contact/ach etc
            {"34","2"}, // item edit
            {"108", "2"}, // order left menu options
            {"113", "2"}, // invoice delete
            // billing clerk role           
            {"1","3"}, // menus
            {"2","3"},
            {"3","3"},
            {"4","3"},
            {"5","3"},  // menu - users 
            {"87","3"}, // menu - users - partner
            {"89","3"}, // menu - users - partner - new
            {"90","3"}, // menu - users - partner - list
            {"91","3"}, // menu - users - partner - defaults
            {"92","3"}, // menu - users - partner - reports
            {"105","3"},// menu - users - partner - due payout
            {"88","3"}, // menu - users - customers
            {"93","3"}, // menu - users - customers - reports
            {"94","3"}, // menu - users - customers - list
            {"95","3"}, // menu - users - customers - new
            {"6","3"},  // menu - users - all
            {"86","3"}, // menu - users - all - create
            {"19","3"}, // menu - users - all - list
            {"22","3"}, // account sub option
            {"23","3"},
            {"24","3"},
            {"29","3"}, // menu - items 
            {"32","3"}, // menu - Items - List
            {"39","3"}, // menu - Payment - create cheque
            {"40","3"}, // menu - Payment - create cc
            {"109","3"},// menu - Payment - create ach
            {"41","3"}, // menu - Payment - list
            {"42","3"}, // menu - Orders - create
            {"43","3"}, // menu - Orders - list
            {"44","3"}, // menu - User - credit card
            {"46","3"}, // menu - Refund
            {"47","3"}, // menu - Refund - cheque
            {"48","3"}, // menu - Refund - cc
            {"49","3"}, // menu - Refund - list
            {"50","3"}, // menu - Invoice
            {"51","3"}, // menu - Invoice - list
            {"52","3"}, // menu - Process
            {"53","3"}, // menu - Process - list
            {"55","3"}, // menu - Process - see latest
            {"56","3"}, // menu - Process - prelim-report
            {"62","3"}, // menu - Reports - Orders
            {"63","3"}, // menu - Reports - Invoice
            {"64","3"}, // menu - Reports - Payment
            {"65","3"}, // menu - Reports - Refund
            {"67","3"}, // report - g. orders
            {"68","3"}, // report - g. invoices
            {"69","3"}, // report - g. payments
            {"71","3"}, // report - g. refunds
            {"73","3"}, // report - total paymets by date
            {"74","3"}, // report - total refunds by date
            {"75","3"}, // report - total ordered by date
            {"76","3"}, // report - invoices overdue
            {"84","3"}, // report - invoices carring a balance
            {"111","3"},// report - invoice lines
            {"112","3"},// report - users
            {"78","3"}, // menu - Reports - List
            {"7","3"}, // user create - no root, clerk type
            {"10","3"},
            {"11","3"},
            {"115","3"}, // create link in initial page
            {"14","3"}, // user edit - can view type and edit password, status and language
            {"16","3"},
            {"17","3"},
            {"20","3"},
            {"82","3"},
            {"114", "3"}, // links to edit of cc/contact/ach etc
            {"108", "3"}, // order left menu options
            {"113", "3"}, // invoice delete
            // partner role           
            {"1","4"}, // menus
            {"2","4"},
            {"104","4"},// menu - reports
            {"106","4"},// menu - reports list
            {"4","4"},
            {"96","4"}, // menu - statement
            {"97","4"}, // menu - statement - latest
            {"98","4"}, // menu - statement - payouts
            {"22","4"}, // account sub option
            {"23","4"},
            {"24","4"},
            {"40","4"}, // menu - Payment - create cc
            {"109","4"},// menu - Payment - create ach
            {"41","4"}, // menu - Payment - list
            {"43","4"}, // menu - Orders - list
            {"44","4"}, // menu - User - credit card
            {"46","4"}, // menu - Refund (list)            
            {"50","4"}, // menu - Invoice
            {"51","4"}, // menu - Invoice - list
            {"7","4"}, // user create - only customers
            {"11","4"},
            {"18","4"}, // user edit - can view language, status, currency
            {"21","4"},
            {"82","4"},
            {"61","4"}, // menu - Customer
            {"77","4"}, // menu - Customer list
            {"79","4"}, // menu - customer - new
            {"99","4"}, // report - partners customers orders
            {"100","4"},// report - partners customers payments
            {"101","4"},// report - partners customers refunds
            {"108", "4"}, // order left menu options

            // customer role           
            {"1","5"}, // menu - order
            {"2","5"}, // menu - payment
            {"25","5"}, // account option
            {"26","5"}, // menu - change password sub-option
            {"27","5"}, // menu - edit contact sub-option
            {"40","5"}, // menu - Payment - create cc
            {"109","5"},// menu - Payment - create ach
            {"41","5"}, // menu - Payment - list
            {"43","5"}, // menu - Orders - list     
            {"45","5"}, // menu - User - credit card     
            {"116", "5"},//menu - User - ach
            {"46","5"}, // menu - Refund (list)            
            {"50","5"}, // menu - Invoice
            {"51","5"}, // menu - Invoice - list
            // user edit - no permissions
            
        };
        addTable(Constants.TABLE_PERMISSION_ROLE_MAP, 
                permissionRoleColumns, permissionRoleData, false);

        //USER_ROLE_MAP
        String userRoleColumns[] =
        {
            "user_id",
            "role_id",
        };
        String userRoleData[][] = {
            {"1","1"}, 
            {"3","1"},
            {"4","2"},
            {"5","3"},
            {"6","4"},
            {"7","5"}
        };
        addTable(Constants.TABLE_USER_ROLE_MAP, 
                userRoleColumns, userRoleData, false);

        //MENU_OPTION
        String menuOptionColumns[] =
        {
            "id", "link", "level", "parent_id"
        };
        String menuOptionData[][] = {
            {"/order/list.jsp", "1", null}, //1 - order
            {"/payment/list.jsp", "1", null}, //2 - payment
            {"/report/list.jsp", "1", null}, //3 - report
            {"/userAccount.do", "1", null}, //4 - system
            {"/user/maintain.jsp", "1", null }, //5 -  user
            {"/user/maintain.jsp", "2", "5"}, //6 - user - all
            {"/user/create.jsp?create=yes", "3", "6"}, //7 - user - all - create
            {"/userAccount.do", "2", "4"}, //8 - system - my account 
            {"/user/edit.jsp", "3", "8"}, //9 - account - change passwd
            {"/user/editContact.jsp", "3", "8"}, //10 - account - edit contact
            {"/userAccount.do", "1", null}, //11 - account (for customers)
            {"/user/edit.jsp", "2", "11"}, //12 - account - change passwd (f cus)
            {"/user/editContact.jsp", "2", "11"}, //13 - account - edit contact (f cus)
            {"/logout.do", "1", null}, //14 - Logout [this is obsoleted, the logout is always beside the title. It remains here because removing it would modify the ids of all the subsequent options]
            {"/item/list.jsp", "1", null}, //15 - Items
            {"/item/listType.jsp", "2", "15"}, //16 - Items - Types
            {"/item/create.jsp?create=yes", "2", "15"}, //17 - Items - Create
            {"/item/list.jsp", "2", "15"}, //18 - Items - List
            {"/item/createType.jsp?create=yes", "3", "16"}, //19 - Items - Types - Create
            {"/item/listType.jsp", "3", "16"}, //20 - Items - Types - List
            {"/item/promotionList.jsp", "1", null}, //21 - Promotions
            {"/item/promotionCreate.jsp?create=yes", "2", "21"}, //22 - Promotions - Create
            {"/item/promotionList.jsp", "2", "21"}, //23 - Promotions - List
            {"/payment/customerSelect.jsp?create=yes&cheque=yes", "2", "2"}, //24 - Payment - Create Cheque
            {"/payment/customerSelect.jsp?create=yes&cc=yes", "2", "2"}, //25 - Payment - Create CC
            {"/payment/list.jsp", "2", "2"}, //26 - Payment - List
            {"/order/newOrder.jsp", "2", "1"}, //27 - order - create
            {"/order/list.jsp", "2", "1"}, //28 - order - list
            {"/creditCardMaintain.do?action=setup&mode=creditCard", "3", "8"}, //29 - account - edit credit card
            {"/creditCardMaintain.do?action=setup&mode=creditCard", "2", "11"}, //30 - account - edit credit card (f cus)
            {"/payment/listRefund.jsp", "1", null}, //31 - Refund
            {"/payment/customerSelect.jsp?create=yes&cheque=yes&refund=yes", "2", "31"}, //32 - Refund - Create Cheque
            {"/payment/customerSelect.jsp?create=yes&cc=yes&refund=yes", "2", "31"}, //33 - Refund - Create CC
            {"/payment/listRefund.jsp", "2", "31"}, //34 - Refund - List
            {"/invoice/list.jsp", "1", null}, //35 - Invoice
            {"/invoice/list.jsp", "2", "35"}, //36 - Invoice - List
            {"/processMaintain.do?action=view&latest=yes", "1", null}, //37 - Process (defaults to latest)
            {"/process/list.jsp", "2", "37"}, //38 - Process - List
            {"/processConfigurationMaintain.do?action=setup&mode=configuration", "2", "37"}, //39 - Process - Edit configuration
            {"/processMaintain.do?action=view&latest=yes", "2", "37"}, //40 - Process - See latest
            {"/processMaintain.do?action=review", "2", "37"}, //41 - Process - prelim-report
            {"/notification/listTypes.jsp", "1", null}, //42 - Notification 
            {"/notification/listTypes.jsp", "2", "42"}, //43 - Notification - Compose
            {"/parameterMaintain.do?action=setup&mode=parameter&type=notification", "2", "42"}, //44 - Notification - Parameters
            {"/notification/emails.jsp", "2", "42"}, //45 - Notification - Emails list
            {"/user/list.jsp", "1", null}, // 46 - Customers (for partners)
            {"/reportList.do?type=1", "2", "1"}, //47 - Reports - Orders
            {"/reportList.do?type=2", "2", "35"}, //48 - Reports - Invoice
            {"/reportList.do?type=3", "2", "2"}, //49 - Reports - Payment
            {"/reportList.do?type=4", "2", "31"}, //50 - Reports - Refund
            {"/reportList.do?type=5", "2", "46"}, //51 - Reports - Customer
            {"/user/list.jsp", "2", "46"}, // 52 - Customers - List (for partners)
            {"/report/list.jsp", "2", "3"}, //53 - report - list
            {"/user/create.jsp?create=yes&customer=yes&frompartner=yes", "2", "46"}, // 54 - Customers - New (for partners)
            {"/brandingMaintain.do?action=setup&mode=branding", "2", "4"}, // 55 - System - Branding
            {"/currencyMaintain.do?action=setup", "2", "4"}, // 56 - System - Currencies
            {"/ageingMaintain.do?action=setup", "2", "4"}, // 57 - System - Ageing
            {"/user/create.jsp?create=yes", "3", "6"}, // 58 - Users - All - create
            {"/user/listPartner.jsp", "2", "5"}, // 59 - Users - Partners
            {"/user/list.jsp", "2", "5"}, // 60 - Users - Customers
            {"/user/create.jsp?create=yes&partner=yes", "3", "59"}, // 61 - Users - Partners - New
            {"/user/listPartner.jsp", "3", "59"}, // 62 - Users - Partners - List
            {"/partnerDefaults.do?action=setup&mode=partnerDefault", "3", "59"}, // 63 - Users - Partners - Defaults
            {"/reportList.do?type=6", "3", "59"}, // 64 - Users - Partners - Reports
            {"/reportList.do?type=5", "3", "60"}, // 65 - Users - Customers - Reports
            {"/user/list.jsp", "3", "60"}, // 66 - Users - Customers - List
            {"/user/create.jsp?create=yes&customer=yes", "3", "60"}, // 67 - Users - Customers - New
            {"/partnerMaintain.do?action=view&self=yes", "1", null}, // 68 - Statement (for partners only)
            {"/partnerMaintain.do?action=view&self=yes", "2", "68"}, // 69 - Statement - Latest
            {"/user/payoutList.jsp", "2", "68"}, // 70 - Statement - Payouts
            {"/reportList.do?type=7", "1", null}, // 71 - Reports (for partners)
            {"/reportTrigger.do?mode=partner&id=" + ReportDTOEx.REPORT_PARTNER , "3", "59"}, // 72 - Users - Partners - Due payment
            {"/reportList.do?type=7", "2", "71"}, //73 - report - list for partners
            {"/notificationPreference.do?action=setup&mode=notificationPreference", "2", "42"}, //74 - notification - preferences
            {"/payment/customerSelect.jsp?create=yes&ach=yes", "2", "2"}, //75 - Payment - Create ACH
            {"/numberingMaintain.do?action=setup&mode=invoiceNumbering", "2", "35"}, //76 - Invoice - List
            {"/achMaintain.do?action=setup&mode=ach", "2", "11"}, //77 - account - edit ach (f cus)
        };
        
        String menuOptionIntColumns[][][] =
        { 
            { {"display", "Orders" } }, //1
            { {"display", "Payments" } }, //2
            { {"display", "Reports" } },  //3                   
            { {"display", "System" } }, //4
            { {"display", "Users" } }, //5
            { {"display", "All" } }, //6
            { {"display", "List - Edit" } }, //7
            { {"display", "My Account" } }, //8
            { {"display", "Change Password" } }, //9
            { {"display", "Edit Contact Information" } }, //10
            { {"display", "Account" } }, //11
            { {"display", "Change Password" } }, //12
            { {"display", "Edit Contact Information" } }, //13
            { {"display", "OBSOLETED" } }, //14        
            { {"display", "Items" } }, //15
            { {"display", "Types" } }, //16
            { {"display", "Create" } }, //17
            { {"display", "List" } }, //18
            { {"display", "Create Type" } }, //19
            { {"display", "List/Edit Types" } }, //20
            { {"display", "Promotions" } }, //21
            { {"display", "Create" } }, //22
            { {"display", "List" } }, //23
            { {"display", "Cheque" } }, //24
            { {"display", "Credit Card" } }, //25
            { {"display", "List" } }, //26
            { {"display", "Create" } }, //27
            { {"display", "List" } }, //28
            { {"display", "Edit Credit Card" } }, //29
            { {"display", "Edit Credit Card" } }, //30
            { {"display", "Refunds" } }, //31
            { {"display", "Cheque" } }, //32
            { {"display", "Credit Card" } }, //33
            { {"display", "List" } }, //34
            { {"display", "Invoices" } }, //35
            { {"display", "List" } }, //36
            { {"display", "Process" } }, //37
            { {"display", "List" } }, //38
            { {"display", "Configuration" } }, //39
            { {"display", "Latest" } }, //40
            { {"display", "Review" } }, //41
            { {"display", "Notification" } }, //42
            { {"display", "Compose" } }, //43
            { {"display", "Parameters" } }, //44
            { {"display", "Emails list" } }, //45
            { {"display", "Customers" } }, //46
            { {"display", "Reports" } }, //47
            { {"display", "Reports" } }, //48
            { {"display", "Reports" } }, //49
            { {"display", "Reports" } }, //50
            { {"display", "Reports" } }, //51
            { {"display", "List" } }, // 52
            { {"display", "List" } }, // 53
            { {"display", "New" } }, // 54
            { {"display", "Branding" } }, // 55
            { {"display", "Currencies" } }, // 56
            { {"display", "Ageing" } }, // 57
            { {"display", "Create" } }, // 58
            { {"display", "Partners" } }, // 59
            { {"display", "Customers" } }, // 60
            { {"display", "New" } }, // 61
            { {"display", "List" } }, // 62
            { {"display", "Defaults" } }, // 63
            { {"display", "Reports" } }, // 64
            { {"display", "Reports" } }, // 65
            { {"display", "List" } }, // 66
            { {"display", "New" } }, // 67
            { {"display", "Statement" } }, // 68
            { {"display", "Latest" } }, // 69
            { {"display", "Payouts" } }, // 70
            { {"display", "Reports" } },  //71
            { {"display", "Partners Due Payout" } },  //72
            { {"display", "List" } },  //73
            { {"display", "Preferences" } },  //74
            { {"display", "ACH" } },  //75
            { {"display", "Numbering" } },  //76
            { {"display", "Edit ACH" } },  //77
        };
        
        addTable(Constants.TABLE_MENU_OPTION, menuOptionColumns,
                menuOptionData, menuOptionIntColumns, false);

        //COUNTRY
        String countryColumns[] = {
            "id", "code" 
        };
        // this has to follow ISO-3166
        String countryData[][] = {
            {"AF"},{"AL"},{"DZ"},{"AS"},{"AD"},{"AO"},{"AI"},{"AQ"},{"AG"},{"AR"},
            {"AM"},{"AW"},{"AU"},{"AT"},{"AZ"},{"BS"},{"BH"},{"BD"},{"BB"},{"BY"},
            {"BE"},{"BZ"},{"BJ"},{"BM"},{"BT"},{"BO"},{"BA"},{"BW"},{"BV"},{"BR"},
            {"IO"},{"BN"},{"BG"},{"BF"},{"BI"},{"KH"},{"CM"},{"CA"},{"CV"},{"KY"},
            {"CF"},{"TD"},{"CL"},{"CN"},{"CX"},{"CC"},{"CO"},{"KM"},{"CG"},{"CK"},
            {"CR"},{"CI"},{"HR"},{"CU"},{"CY"},{"CZ"},{"CD"},{"DK"},{"DJ"},{"DM"},
            {"DO"},{"TP"},{"EC"},{"EG"},{"SV"},{"GQ"},{"ER"},{"EE"},{"ET"},{"FK"},
            {"FO"},{"FJ"},{"FI"},{"FR"},{"GF"},{"PF"},{"TF"},{"GA"},{"GM"},{"GE"},
            {"DE"},{"GH"},{"GI"},{"GR"},{"GL"},{"GD"},{"GP"},{"GU"},{"GT"},{"GN"},
            {"GW"},{"GY"},{"HT"},{"HM"},{"HN"},{"HK"},{"HU"},{"IS"},{"IN"},{"ID"},
            {"IR"},{"IQ"},{"IE"},{"IL"},{"IT"},{"JM"},{"JP"},{"JO"},{"KZ"},{"KE"},
            {"KI"},{"KR"},{"KW"},{"KG"},{"LA"},{"LV"},{"LB"},{"LS"},{"LR"},{"LY"},
            {"LI"},{"LT"},{"LU"},{"MO"},{"MK"},{"MG"},{"MW"},{"MY"},{"MV"},{"ML"},
            {"MT"},{"MH"},{"MQ"},{"MR"},{"MU"},{"YT"},{"MX"},{"FM"},{"MD"},{"MC"},
            {"MN"},{"MS"},{"MA"},{"MZ"},{"MM"},{"NA"},{"NR"},{"NP"},{"NL"},{"AN"},
            {"NC"},{"NZ"},{"NI"},{"NE"},{"NG"},{"NU"},{"NF"},{"KP"},{"MP"},{"NO"},
            {"OM"},{"PK"},{"PW"},{"PA"},{"PG"},{"PY"},{"PE"},{"PH"},{"PN"},{"PL"},
            {"PT"},{"PR"},{"QA"},{"RE"},{"RO"},{"RU"},{"RW"},{"WS"},{"SM"},{"ST"},
            {"SA"},{"SN"},{"YU"},{"SC"},{"SL"},{"SG"},{"SK"},{"SI"},{"SB"},{"SO"},
            {"ZA"},{"GS"},{"ES"},{"LK"},{"SH"},{"KN"},{"LC"},{"PM"},{"VC"},{"SD"},
            {"SR"},{"SJ"},{"SZ"},{"SE"},{"CH"},{"SY"},{"TW"},{"TJ"},{"TZ"},{"TH"},
            {"TG"},{"TK"},{"TO"},{"TT"},{"TN"},{"TR"},{"TM"},{"TC"},{"TV"},{"UG"},
            {"UA"},{"AE"},{"UK"},{"US"},{"UM"},{"UY"},{"UZ"},{"VU"},{"VA"},{"VE"},
            {"VN"},{"VG"},{"VI"},{"WF"},{"YE"},{"ZM"},{"ZW"},
        };
        String countryIntColumns[][][] = {
            { {"description", "Afghanistan"} }, { {"description", "Albania"} }, 
            { {"description", "Algeria"} }, { {"description", "American Samoa"} }, 
            { {"description", "Andorra"} }, { {"description", "Angola"} }, 
            { {"description", "Anguilla"} }, { {"description", "Antarctica"} }, 
            { {"description", "Antigua and Barbuda"} }, { {"description", "Argentina"} }, 
            { {"description", "Armenia"} }, { {"description", "Aruba"} }, 
            { {"description", "Australia"} }, { {"description", "Austria"} }, 
            { {"description", "Azerbaijan"} }, { {"description", "Bahamas"} }, 
            { {"description", "Bahrain"} }, { {"description", "Bangladesh"} }, 
            { {"description", "Barbados"} }, { {"description", "Belarus"} }, 
            { {"description", "Belgium"} }, { {"description", "Belize"} }, 
            { {"description", "Benin"} }, { {"description", "Bermuda"} }, 
            { {"description", "Bhutan"} }, { {"description", "Bolivia"} }, 
            { {"description", "Bosnia and Herzegovina"} }, { {"description", "Botswana"} }, 
            { {"description", "Bouvet Island"} }, { {"description", "Brazil"} }, 
            { {"description", "British Indian Ocean Territory"} }, { {"description", "Brunei"} }, 
            { {"description", "Bulgaria"} }, { {"description", "Burkina Faso"} }, 
            { {"description", "Burundi"} }, { {"description", "Cambodia"} }, 
            { {"description", "Cameroon"} }, { {"description", "Canada"} }, 
            { {"description", "Cape Verde"} }, { {"description", "Cayman Islands"} }, 
            { {"description", "Central African Republic"} }, { {"description", "Chad"} }, 
            { {"description", "Chile"} }, { {"description", "China"} }, 
            { {"description", "Christmas Island"} }, { {"description", "Cocos &#40;Keeling&#41; Islands"} }, 
            { {"description", "Colombia"} }, { {"description", "Comoros"} }, 
            { {"description", "Congo"} }, { {"description", "Cook Islands"} }, 
            { {"description", "Costa Rica"} }, { {"description", "Côte d&#39;Ivoire"} }, 
            { {"description", "Croatia &#40;Hrvatska&#41;"} }, { {"description", "Cuba"} }, 
            { {"description", "Cyprus"} }, { {"description", "Czech Republic"} }, 
            { {"description", "Congo &#40;DRC&#41;"} }, { {"description", "Denmark"} }, 
            { {"description", "Djibouti"} }, { {"description", "Dominica"} }, 
            { {"description", "Dominican Republic"} }, { {"description", "East Timor"} }, 
            { {"description", "Ecuador"} }, { {"description", "Egypt"} }, 
            { {"description", "El Salvador"} }, { {"description", "Equatorial Guinea"} }, 
            { {"description", "Eritrea"} }, { {"description", "Estonia"} }, 
            { {"description", "Ethiopia"} }, { {"description", "Falkland Islands &#40;Islas Malvinas&#41;"} }, 
            { {"description", "Faroe Islands"} }, { {"description", "Fiji Islands"} }, 
            { {"description", "Finland"} }, { {"description", "France"} }, 
            { {"description", "French Guiana"} }, { {"description", "French Polynesia"} }, 
            { {"description", "French Southern and Antarctic Lands"} }, { {"description", "Gabon"} }, 
            { {"description", "Gambia"} }, { {"description", "Georgia"} }, 
            { {"description", "Germany"} }, { {"description", "Ghana"} }, 
            { {"description", "Gibraltar"} }, { {"description", "Greece"} }, 
            { {"description", "Greenland"} }, { {"description", "Grenada"} }, 
            { {"description", "Guadeloupe"} }, { {"description", "Guam"} }, 
            { {"description", "Guatemala"} }, { {"description", "Guinea"} }, 
            { {"description", "Guinea-Bissau"} }, { {"description", "Guyana"} }, 
            { {"description", "Haiti"} }, { {"description", "Heard Island and McDonald Islands"} }, 
            { {"description", "Honduras"} }, { {"description", "Hong Kong SAR"} }, 
            { {"description", "Hungary"} }, { {"description", "Iceland"} }, 
            { {"description", "India"} }, { {"description", "Indonesia"} }, 
            { {"description", "Iran"} }, { {"description", "Iraq"} }, 
            { {"description", "Ireland"} }, { {"description", "Israel"} }, 
            { {"description", "Italy"} }, { {"description", "Jamaica"} }, 
            { {"description", "Japan"} }, { {"description", "Jordan"} }, 
            { {"description", "Kazakhstan"} }, { {"description", "Kenya"} }, 
            { {"description", "Kiribati"} }, { {"description", "Korea"} }, 
            { {"description", "Kuwait"} }, { {"description", "Kyrgyzstan"} }, 
            { {"description", "Laos"} }, { {"description", "Latvia"} }, 
            { {"description", "Lebanon"} }, { {"description", "Lesotho"} }, 
            { {"description", "Liberia"} }, { {"description", "Libya"} }, 
            { {"description", "Liechtenstein"} }, { {"description", "Lithuania"} }, 
            { {"description", "Luxembourg"} }, { {"description", "Macao SAR"} }, 
            { {"description", "Macedonia, Former Yugoslav Republic of"} }, { {"description", "Madagascar"} }, 
            { {"description", "Malawi"} }, { {"description", "Malaysia"} }, 
            { {"description", "Maldives"} }, { {"description", "Mali"} }, 
            { {"description", "Malta"} }, { {"description", "Marshall Islands"} }, 
            { {"description", "Martinique"} }, { {"description", "Mauritania"} }, 
            { {"description", "Mauritius"} }, { {"description", "Mayotte"} }, 
            { {"description", "Mexico"} }, { {"description", "Micronesia"} }, 
            { {"description", "Moldova"} }, { {"description", "Monaco"} }, 
            { {"description", "Mongolia"} }, { {"description", "Montserrat"} }, 
            { {"description", "Morocco"} }, { {"description", "Mozambique"} }, 
            { {"description", "Myanmar"} }, { {"description", "Namibia"} }, 
            { {"description", "Nauru"} }, { {"description", "Nepal"} }, 
            { {"description", "Netherlands"} }, { {"description", "Netherlands Antilles"} }, 
            { {"description", "New Caledonia"} }, { {"description", "New Zealand"} }, 
            { {"description", "Nicaragua"} }, { {"description", "Niger"} }, 
            { {"description", "Nigeria"} }, { {"description", "Niue"} }, 
            { {"description", "Norfolk Island"} }, { {"description", "North Korea"} }, 
            { {"description", "Northern Mariana Islands"} }, { {"description", "Norway"} }, 
            { {"description", "Oman"} }, { {"description", "Pakistan"} }, 
            { {"description", "Palau"} }, { {"description", "Panama"} }, 
            { {"description", "Papua New Guinea"} }, { {"description", "Paraguay"} }, 
            { {"description", "Peru"} }, { {"description", "Philippines"} }, 
            { {"description", "Pitcairn Islands"} }, { {"description", "Poland"} }, 
            { {"description", "Portugal"} }, { {"description", "Puerto Rico"} }, 
            { {"description", "Qatar"} }, { {"description", "Reunion"} }, 
            { {"description", "Romania"} }, { {"description", "Russia"} }, 
            { {"description", "Rwanda"} }, { {"description", "Samoa"} }, 
            { {"description", "San Marino"} }, { {"description", "São Tomé and Príncipe"} }, 
            { {"description", "Saudi Arabia"} }, { {"description", "Senegal"} }, 
            { {"description", "Serbia and Montenegro"} }, { {"description", "Seychelles"} }, 
            { {"description", "Sierra Leone"} }, { {"description", "Singapore"} }, 
            { {"description", "Slovakia"} }, { {"description", "Slovenia"} }, 
            { {"description", "Solomon Islands"} }, { {"description", "Somalia"} }, 
            { {"description", "South Africa"} }, { {"description", "South Georgia and the South Sandwich Islands"} }, 
            { {"description", "Spain"} }, { {"description", "Sri Lanka"} }, 
            { {"description", "St. Helena"} }, { {"description", "St. Kitts and Nevis"} }, 
            { {"description", "St. Lucia"} }, { {"description", "St. Pierre and Miquelon"} }, 
            { {"description", "St. Vincent and the Grenadines"} }, { {"description", "Sudan"} }, 
            { {"description", "Suriname"} }, { {"description", "Svalbard and Jan Mayen"} }, 
            { {"description", "Swaziland"} }, { {"description", "Sweden"} }, 
            { {"description", "Switzerland"} }, { {"description", "Syria"} }, 
            { {"description", "Taiwan"} }, { {"description", "Tajikistan"} }, 
            { {"description", "Tanzania"} }, { {"description", "Thailand"} }, 
            { {"description", "Togo"} }, { {"description", "Tokelau"} }, 
            { {"description", "Tonga"} }, { {"description", "Trinidad and Tobago"} }, 
            { {"description", "Tunisia"} }, { {"description", "Turkey"} }, 
            { {"description", "Turkmenistan"} }, { {"description", "Turks and Caicos Islands"} }, 
            { {"description", "Tuvalu"} }, { {"description", "Uganda"} }, 
            { {"description", "Ukraine"} }, { {"description", "United Arab Emirates"} }, 
            { {"description", "United Kingdom"} }, { {"description", "United States"} }, 
            { {"description", "United States Minor Outlying Islands"} }, { {"description", "Uruguay"} }, 
            { {"description", "Uzbekistan"} }, { {"description", "Vanuatu"} }, 
            { {"description", "Vatican City"} }, { {"description", "Venezuela"} }, 
            { {"description", "Viet Nam"} }, { {"description", "Virgin Islands &#40;British&#41;"} }, 
            { {"description", "Virgin Islands"} }, { {"description", "Wallis and Futuna"} }, 
            { {"description", "Yemen"} }, { {"description", "Zambia"} }, 
            { {"description", "Zimbabwe"} }, 
        }; 

        addTable(Constants.TABLE_COUNTRY, countryColumns,
                countryData, countryIntColumns, false);
                
        //PROMOTION
        String promotionColumns[] = {
            "id", "item_id", "code", "notes", "once", "since", "until"
        };
        String promotionData[][] = null;
        addTable(Constants.TABLE_PROMOTION, promotionColumns, promotionData, 
                false);

        //PAYMENT_AUTHORIZATION
        String paymentAuthorizationColumns[] = {
            "id", "payment_id", "processor", "code1", "code2", "code3", "approval_code",
            "avs", "transaction_id", "md5", "card_code"
        };
        String paymentAuthorizationData[][] = null;
        addTable(Constants.TABLE_PAYMENT_AUTHORIZATION, 
                paymentAuthorizationColumns, paymentAuthorizationData, 
                false);

        // CURRENCY_EXCHANGE
        String currencyExchangeColumns[] =
        {
            "id",
            "entity_id",
            "currency_id",
            "rate", // how many X do you need for 1 US$?
            "create_datetime"
        };
        // do not try to use null for defaults, it won't work 
        // sql generated by entity beans will fail!
        String currencyExchangeData[][] = {
            {"0", "2", "1.325", "2004-03-09"} ,   // we use 0 for default
            {"0", "3", "0.8118","2004-03-09"} ,  
            {"0", "4", "111.4", "2004-03-09"} ,  
            {"0", "5", "0.5479","2004-03-09"} ,  
            {"0", "6", "1171",  "2004-03-09"} ,
            {"0", "7", "1.23",  "2004-07-06"} ,
			{"0", "8", "7.47",  "2004-07-06"} ,
            {"1", "2", "1.5",   "2004-03-09"} ,  
            {"1", "5", "0.75",  "2004-03-09"} ,  
        };
        addTable(Constants.TABLE_CURRENCY_EXCHANGE, 
                currencyExchangeColumns, currencyExchangeData, false);

        // CURRENCY_ENTITY_MAP
        String currencyEntityMapColumns[] =
        { "entity_id", "currency_id", };
        String currencyEntityMapData[][] = { 
            { "1", "1", }, 
            { "1", "2", }, 
            { "1", "5", }, 
        };
        addTable(Constants.TABLE_CURRENCY_ENTITY_MAP, currencyEntityMapColumns,
                currencyEntityMapData, false);

        //AGEING_ENTITY_STEP
        String ageingEntityStepColumns[] =
                {"id", "entity_id", "status_id", "days"};
        String ageingEntityStepMessageData[][] = {
            {"1", "1", "0"}, // active (for the welcome message) 
            {"1", "2", "3"}, // ov 1
            {"1", "3", "1"}, // ov 2
            {"1", "5", "2"}, // sus 1
            {"1", "7", "30"},// sus 3
            {"1", "8", "0"}, // del
        };
        String ageingEntityStepIntColumns[][][] =
        { 
            { { "welcome_message", "Welcome to sapienter billing" }, }, // act
            { { "welcome_message", "You have overdue payments. Please submit a payment to your latest invoice." }, }, // ov1
            { { "welcome_message", "Please proceed to pay you latest invoice or your account will be suspended." }, }, // ov2
            { { "welcome_message", "Your account has been recently suspended due to overdue payments. Please pay your latest invoice for immediate reactivation." }, { "failed_login_message", "Your account is suspended. Proceed to the billing menu for reactivation." } }, // sus 1
            { { "welcome_message", "Your accoutn is suspended and will be permanently removed if no payment is recieved soon." }, { "failed_login_message", "Your account is suspended. Proceed to the billing menu for reactivation." } }, // sus 3
            { { "welcome_message", "none" },  }, // del
        };
        addTable(Constants.TABLE_AGEING_ENTITY_STEP, 
                ageingEntityStepColumns, 
                ageingEntityStepMessageData, 
                ageingEntityStepIntColumns, false);

        // PARTNER_PAYOUT
        String partnerPayoutColumns[] =
        {
            "id",
            "starting_date",
            "ending_date",
            "payments_amount",
            "refunds_amount", // how many X do you need for 1 US$?
            "balance_left",
            "payment_id",
            "partner_id"
        };
        // do not try to use null for defaults, it won't work 
        // sql generated by entity beans will fail!
        String partnerPayoutData[][] = {
            {"2004-01-15","2004-02-15", "2000", "15", "1000", "1", "1"} ,
            {"2004-02-19","2004-03-01", "100", "15", "50", "2", "3"} ,
        };
        addTable(Constants.TABLE_PARTNER_PAYOUT, 
                partnerPayoutColumns, partnerPayoutData, false);

        //REPORT_USER
        String reportUserColumns[] =
        {
            "id",
            "user_id",
            "report_id",
            "create_datetime",
            "title"
        };
        String reportUserData[][] = {
            {"1", "1", "2003-06-01", "From InitializeDataBase"}
        };
        addTable(Constants.TABLE_REPORT_USER, 
                reportUserColumns, 
                reportUserData, false);

        //REPORT_ENTITY_MAP
        String reportEntityColumns[] =
        {
            "entity_id",
            "report_id",
        };
        String reportEntityData[][] = {
            {"1","1"},
            {"1","2"},
            {"1","3"},
            {"1","4"},
            {"1","5"},
            {"1","6"},            
            {"1","7"},            
            {"1","8"},            
            {"1","9"},
            {"1","10"},
            {"1","11"},
            {"1","12"},
            {"1","13"},
            {"1","14"},
            {"1","15"},
            {"1","16"},
            {"1","17"},
            {"1","18"},

        };
        addTable(Constants.TABLE_REPORT_ENTITY_MAP, 
                reportEntityColumns, 
                reportEntityData, false);
        //REPORT_TYPE
        String reportTypeColumns[] = { "id", "showable" };
        String reportTypeData[][] = { {"1"}, {"1"}, {"1"}, {"1"}, {"1"}, {"1"}, 
                {"0"}, {"1"} };

        String reportTypeIntColumns[][][] = {
            { { "description", "Order" } },   // 1
            { { "description", "Invoice" } }, // 2
            { { "description", "Payment" } }, // 3
            { { "description", "Refund" } },  // 4
            { { "description", "Customer" } },// 5
            { { "description", "Partner" } }, // 6
            { { "description", "Partner selected" } }, // 7 (not showable in list)
            { { "description", "User" } }, // 8
        };

        addTable(Constants.TABLE_REPORT_TYPE, reportTypeColumns, reportTypeData, 
                reportTypeIntColumns, false);

        //REPORT_TYPE_MAP
        String reportTypeMapColumns[] =
        {
            "report_id",
            "type_id",
        };
        String reportTypeMapData[][] = {
            {"1","1"},
            {"2","2"},
            {"3","3"},
            {"4","1"},
            {"5","4"},
            {"6","2"},            
            {"7","3"},            
            {"8","4"},            
            {"9","1"},
            {"10","2"},
            {"10","5"},
            {"11","2"},
            {"11","5"},
            {"12","7"},
            {"13","7"},
            {"14","7"},
            {"15","6"},
            {"16","6"},
            {"17","2"},
            {"18","8"},
            
        };
        addTable(Constants.TABLE_REPORT_TYPE_MAP, 
                reportTypeMapColumns, 
                reportTypeMapData, false);

        
    }    
}
