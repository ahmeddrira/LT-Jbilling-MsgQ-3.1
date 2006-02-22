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
import com.sapienter.jbilling.server.report.Field;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author emilc
 * file:/C:/bop/tools/jboss-3.2.1_tomcat-4.1.24/server/default/deploy/betty-ejb.jar 
 * file:/C:/bop/tools/jboss-3.2.1_tomcat-4.1.24/server/default/deploy/betty.war 
 */
public final class InitializeReports {

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
                processTable(table, tableIdx);    
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

        //REPORT
        String reportColumns[] =
        {
            "id",
            "titleKey",
            "instructionsKey",
            "tables",
            "where_str",
            "id_column",
            "link"
        };
        String reportData[][] = {
            {"report.general_orders.title", "report.general_orders.instr", 
             "base_user, purchase_order, order_period, order_billing_type, " +             "international_description id, betty_table bt, " + 
             "international_description id2, betty_table bt2, " +
             "contact_map cm, contact , betty_table bt3" ,
             "bt3.name = 'base_user' " +
             "and bt3.id = cm.table_id " +
             "and cm.foreign_id = base_user.id " +
             "and contact.id = cm.contact_id " +
             "and base_user.id = purchase_order.user_id " +
             "and bt.name = 'order_period' " +
             "and bt2.name = 'order_billing_type' " +
             "and id.table_id = bt.id " +
             "and id2.table_id = bt2.id " +
             "and id.foreign_id = order_period.id " +
             "and id2.foreign_id = order_billing_type.id " +
             "and purchase_order.period_id = order_period.id " +             "and purchase_order.billing_type_id = order_billing_type.id " +
             "and id.language_id = id2.language_id", // to avoid to dyn variables 
             "1", "/orderMaintain.do?action=view"  },

            // 2 - INVOICES
            {"report.general_invoices.title", "report.general_invoices.instr", 
             "base_user, invoice, currency", 
             "base_user.id = invoice.user_id " +             "and invoice.currency_id = currency.id", "1",
             "/invoiceMaintain.do"  },

            {"report.general_payments.title", "report.general_payments.instr", 
             "base_user, payment, payment_method, payment_result, " +
             "international_description id, betty_table bt, " + 
             "international_description id2, betty_table bt2" , 
             "base_user.id = payment.user_id " +             "and bt.name = 'payment_method' " +             "and bt2.name = 'payment_result' " +             "and id.table_id = bt.id " +             "and id2.table_id = bt2.id " +             "and id.foreign_id = payment_method.id " +             "and id2.foreign_id = payment_result.id " +             "and payment.method_id = payment_method.id " +             "and payment.result_id = payment_result.id " +
             "and id.language_id = id2.language_id", // to avoid to dyn variables 
             "1", "/paymentMaintain.do?action=view"  },

            {"report.general_order_line.title", "report.general_order_line.instr", 
             "base_user, purchase_order, order_line, order_line_type," +             "international_description id, betty_table bt", 
             "base_user.id = purchase_order.user_id " +
             "and purchase_order.id = order_line.order_id " +             "and bt.name = 'order_line_type' " +             "and id.table_id = bt.id " +             "and id.foreign_id = order_line_type.id " +             "and order_line.type_id = order_line_type.id", 
             "1", "/orderMaintain.do?action=view"  },

            {"report.general_refunds.title", "report.general_refunds.instr", 
             "base_user, payment, payment_method, payment_result, " +
             "international_description id, betty_table bt, " + 
             "international_description id2, betty_table bt2" , 
             "base_user.id = payment.user_id " +
             "and bt.name = 'payment_method' " +
             "and bt2.name = 'payment_result' " +
             "and id.table_id = bt.id " +
             "and id2.table_id = bt2.id " +
             "and id.foreign_id = payment_method.id " +
             "and id2.foreign_id = payment_result.id " +
             "and payment.method_id = payment_method.id " +
             "and payment.result_id = payment_result.id " +
             "and id.language_id = id2.language_id", // to avoid to dyn variables 
             "1", "/paymentMaintain.do?action=view"  },

            // 6 - Total invoiced by date range
            {"report.invoices_total.title", "report.invoices_total.instr", 
             "base_user, invoice", 
             "base_user.id = invoice.user_id", "0", null },

            // 7 - Total in payments received by date range
            {"report.payments_total.title", "report.payments_total.instr", 
             "base_user, payment" , 
             "base_user.id = payment.user_id " +
             "and payment.result_id in (1,4) ", // this has to be in synch with the results types
             "0", null  },

            // 8 - Total in refunds by date range
            {"report.refunds_total.title", "report.refunds_total.instr", 
             "base_user, payment" , 
             "base_user.id = payment.user_id " +
             "and payment.result_id in (1,4) ", // this has to be in synch with the results types
             "0", null  },

            // 9 - Order total by date range
            {"report.orders_total.title", "report.orders_total.instr", 
             "base_user, purchase_order, order_line", 
             "base_user.id = purchase_order.user_id " +
             "and purchase_order.id = order_line.order_id " +
             "and purchase_order.deleted = 0 " +             "and order_line.deleted = 0",
             "0", null  },

            // 10 - Customers with overdue invoices
            {"report.customers_overdue.title", "report.customers_overdue.instr", 
             "base_user, invoice", 
             "base_user.id = invoice.user_id " +             "and invoice.deleted = 0 " +             "and to_process = 1", "1",
             "/invoiceMaintain.do"  },

            // 11 - Customers with invoices with carried balance
            {"report.customers_carring.title", "report.customers_carring.instr", 
             "base_user, invoice, currency", 
             "base_user.id = invoice.user_id " +             "and invoice.currency_id = currency.id " +
             "and invoice.deleted = 0 " +
             "and to_process = 1", "1",
             "/invoiceMaintain.do"  },

            // 12 - Selected partner - active orders
            {"report.partners_orders.title", "report.partners_orders.instr", 
             "base_user, purchase_order, order_period, " +
             "international_description id, betty_table bt, customer ", 
             "base_user.id = purchase_order.user_id " +
             "and bt.name = 'order_period' " +
             "and id.table_id = bt.id " +
             "and base_user.id = customer.user_id " +
             "and id.foreign_id = order_period.id " +
             "and purchase_order.status_id = " + Constants.ORDER_STATUS_ACTIVE + " " +
             "and purchase_order.deleted = 0 " +
             "and purchase_order.period_id = order_period.id ",
             "1", "/orderMaintain.do?action=view"  },

            // 13 - Selected partner - customers payments
            {"report.partners_payments.title", "report.partners_payments.instr", 
             "base_user, payment, payment_method, payment_result, " +
             "international_description id, betty_table bt, customer , " + 
             "international_description id2, betty_table bt2" , 
             "base_user.id = payment.user_id " +
             "and payment.deleted = 0 " +
             "and payment.is_refund = 0 " +
             "and bt.name = 'payment_method' " +
             "and bt2.name = 'payment_result' " +
             "and base_user.id = customer.user_id " +
             "and id.table_id = bt.id " +
             "and id2.table_id = bt2.id " +
             "and id.foreign_id = payment_method.id " +
             "and id2.foreign_id = payment_result.id " +
             "and payment.method_id = payment_method.id " +
             "and payment.result_id = payment_result.id " +
             "and id.language_id = id2.language_id", // to avoid to dyn variables 
             "1", "/paymentMaintain.do?action=view"  },
 
            // 14 - Selected partner - customers refunds
            {"report.partners_refunds.title", "report.partners_refunds.instr", 
             "base_user, payment, payment_method, payment_result, " +
             "international_description id, betty_table bt, customer , " + 
             "international_description id2, betty_table bt2" , 
             "base_user.id = payment.user_id " +
             "and payment.deleted = 0 " +
             "and payment.is_refund = 1 " +
             "and bt.name = 'payment_method' " +
             "and bt2.name = 'payment_result' " +
             "and base_user.id = customer.user_id " +
             "and id.table_id = bt.id " +
             "and id2.table_id = bt2.id " +
             "and id.foreign_id = payment_method.id " +
             "and id2.foreign_id = payment_result.id " +
             "and payment.method_id = payment_method.id " +
             "and payment.result_id = payment_result.id " +
             "and id.language_id = id2.language_id", // to avoid to dyn variables 
             "1", "/paymentMaintain.do?action=view"  },
            
            // 15 - General Partners 
            {"report.partners.title", "report.partners.instr",
             "partner, base_user, period_unit pu, " +             "international_description id, betty_table bt ",
             "partner_payout.partner_id = partner.id " +
             "and id.table_id = bt.id " +
             "and bt.name = 'period_unit' " +
             "and id.foreign_id = pu.id " +
             "and partner.period_unit_id = pu.id " +
             "and id.psudo_column = 'description' " +
             "and base_user.id = partner.user_id " +
             "and base_user.deleted = 0", "1",
             "/partnerMaintain.do?action=view"},

            // 16 - General Payouts 
            {"report.payouts.title", "report.payouts.instr",
             "partner, partner_payout, base_user, payment, payment_result," +             "international_description id, betty_table bt",
             "partner_payout.partner_id = partner.id " +
             "and base_user.id = partner.user_id " +
             "and partner.id = partner_payout.partner_id " +
             "and payment.id = partner_payout.payment_id " +
             "and bt.name = 'payment_result' " +
             "and bt.id = id.table_id " +
             "and payment_result.id = payment.result_id " +             "and payment_result.id = id.foreign_id " +
             "and base_user.deleted = 0", "1",
             "/payout.do?action=view"},
             
             // 17 - Invoice Lines
             {"report.invoice_line.title", "report.invoice_line.instr", 
              "base_user, invoice, currency, invoice_line, invoice_line_type", 
              "base_user.id = invoice.user_id " +
              "and invoice.currency_id = currency.id " +
              "and invoice.id = invoice_line.invoice_id " +
              "and invoice_line.type_id = invoice_line_type.id " +
              "and invoice_line.deleted = 0 " +
              "and invoice.is_review = 0", "1",
              "/invoiceMaintain.do"  },

             // 18 - Users
             {"report.user.title", "report.user.instr",
              "base_user, contact_map, contact, betty_table bt1, user_status, " +
              "user_role_map, betty_table bt2, betty_table bt3, " +
              "international_description it1, international_description it2, " +
              "language, international_description it3, country, betty_table bt4 ",
              "base_user.id = contact_map.foreign_id " +
              "and bt1.name = 'base_user' " +
              "and bt1.id = contact_map.table_id " +
              "and contact_map.contact_id = contact.id " +
              "and contact_map.type_id = 1 " +
              "and base_user.id = user_role_map.user_id " +
              "and base_user.status_id = user_status.id " +
              "and bt2.name = 'user_status' " +
              "and bt3.name = 'role' " +
              "and it1.table_id = bt2.id " +
              "and it1.foreign_id = user_status.id " +
              "and it1.psudo_column = 'description' " +
              "and it2.table_id = bt3.id " +
              "and it2.foreign_id = user_role_map.role_id " +
              "and it2.psudo_column = 'title' " +
              "and it2.language_id = it1.language_id " + // to avoid two dyn variables
              "and base_user.deleted = 0 " + 
              "and base_user.language_id = language.id " +
              "and contact.country_code = country.code " +
              "and country.id = it3.foreign_id " +
              "and it3.language_id = it2.language_id " +
              "and it3.table_id = bt4.id " +
              "and bt4.name = 'country' " +
              "and it3.psudo_column = 'description' ",
              "1", "/userMaintain.do?action=setup"
             }
        };
        addTable(Constants.TABLE_REPORT, 
                reportColumns, 
                reportData, false);


        //REPORT_FIELD
        String reportFieldColumns[] =
        {
            "id",
            "report_id",
            "report_user_id",
            "position",
            "table_name",
            "column_name",
            "order_position",
            "where_value",
            "title_key",
            "function",
            "is_grouped",
            "is_shown",
            "data_type",
            "operator",
            "functionable",
            "selectable",
            "ordenable",
            "operatorable",
            "whereable",
        };
        String reportFieldData[][] = {
            // GENERAL ORDERS
            {"1", // report id
             null, // user_id
             "1", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"1", // report id
             null, // user_id
             "1", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "report.prompt.base_user.user_name", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
             {"1", // report id
              null, // user_id
              "1", // position
              "contact", // table
              "organization_name", // column name
              null, // order position
              null, // where value
              "contact.prompt.organizationName", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_STRING, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "0", // operatorable
              "0"  // wherable
             },
             {"1", // report id
              null, // user_id
              "1", // position
              "contact", // table
              "first_name", // column name
              null, // order position
              null, // where value
              "contact.prompt.firstName", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_STRING, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "0", // operatorable
              "0"  // wherable
             },
             {"1", // report id
             null, // user_id
             "1", // position
             "contact", // table
             "last_name", // column name
             null, // order position
             null, // where value
             "contact.prompt.lastName", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"1", // report id
                null, // user_id
                "1", // position
                "contact", // table
                "phone_phone_number", // column name
                null, // order position
                null, // where value
                "contact.prompt.phoneNumber", // title key
                null, // function
                "0", // is grouped
                "1", // is shown
                Field.TYPE_STRING, // data type
                null, // operator
                "1", // functionable
                "1", // selectable
                "1", // ordenable
                "0", // operatorable
                "0"  // wherable
            },
            {"1", // report id
             null, // user_id
             "1", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "3", // position
             "purchase_order", // table
             "period_id", // column name
             null, // order position
             null, // where value
             "order.prompt.periodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "4", // position
             "purchase_order", // table
             "billing_type_id", // column name
             null, // order position
             null, // where value
             "order.prompt.billingTypeId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "5", // position
             "purchase_order", // table
             "active_since", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_since", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "5", // position
             "purchase_order", // table
             "active_since", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_since", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "6", // position
             "purchase_order", // table
             "active_until", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_until", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "6", // position
             "purchase_order", // table
             "active_until", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_until", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "7", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "7", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "8", // position
             "purchase_order", // table
             "created_by", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.created_by", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "9", // position
             "purchase_order", // table
             "status_id", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.status", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "next_billable_day", // column name
             null, // order position
             null, // where value
             "order.prompt.nextBillableDay", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "next_billable_day", // column name
             null, // order position
             null, // where value
             "order.prompt.nextBillableDay", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"1", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"1", // report id
             null, // user_id
             "11", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             // this field is only used when executing the report internally
            {"1", // report id  
             null, // user_id
             "11", // position
             "base_user", // table
             "id", // column name
             null, // order position
             null , // where value 
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"1", // report id
             null, // user_id
             "12", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"1", // report id
             null, // user_id
             "3", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "order.prompt.period", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"1", // report id
             null, // user_id
             "4", // position
             "id2", // table
             "content", // column name
             null, // order position
             null, // where value
             "order.prompt.billingType", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             {"1", // report id
              null, // user_id
              "11", // position
              "purchase_order", // table
              "notify", // column name
              null, // order position
              null, // where value
              "report.prompt.purchase_order.notify", // title key
              null, // function
              "0", // is grouped
              "0", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
             },
             {"1", // report id
              null, // user_id
              "12", // position
              "purchase_order", // table
              "last_notified", // column name
              null, // order position
              null, // where value
              "report.prompt.purchase_order.last_notification", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_DATE, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
             }, 
             {"1", // report id
              null, // user_id
              "13", // position
              "purchase_order", // table
              "last_notified", // column name
              null, // order position
              null, // where value
              "report.prompt.purchase_order.last_notification", // title key
              null, // function
              "0", // is grouped
              "0", // is shown
              Field.TYPE_DATE, // data type
              null, // operator
              "0", // functionable
              "0", // selectable
              "0", // ordenable
              "1", // operatorable
              "1"  // wherable
             }, 
             {"1", // report id
              null, // user_id
              "14", // position
              "purchase_order", // table
              "notification_step", // column name
              null, // order position
              null, // where value
              "order.prompt.notificationStep", // title key
              null, // function
              "0", // is grouped
              "0", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
             },
             
             // this is a saved report 
            {null, // report id
             "1", // user_id
             "1", // position
             "purchase_order", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             }, 
             
            /*
             *  GENERAL INVOICES
             */
            {"2", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"2", // report id
             null, // user_id
             "2", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             "invoice.id.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
             {"2", // report id
              null, // user_id
              "2", // position
              "invoice", // table
              "public_number", // column name
              null, // order position
              null, // where value
              "invoice.number.prompt", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_STRING, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
            },
            {"2", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            // this field is only used when executing the report internally
           {"2", // report id  
            null, // user_id
            "99", // position
            "base_user", // table
            "id", // column name
            null, // order position
            null , // where value 
            null, // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "0", // operatorable
            "0"  // wherable
            },
            {"2", // report id
             null, // user_id
             "4", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
             // this is the date again to allow 'between'
            {"2", // report id
             null, // user_id
             "5", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "6", // position
             "invoice", // table
             "billing_process_id", // column name
             null, // order position
             null, // where value
             "process.external.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "7", // position
             "invoice", // table
             "delegated_invoice_id", // column name
             null, // order position
             null, // where value
             "invoice.delegated.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "8", // position
             "invoice", // table
             "due_date", // column name
             null, // order position
             null, // where value
             "invoice.dueDate.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            // this is the date again to allow 'between'
            {"2", // report id
             null, // user_id
             "9", // position
             "invoice", // table
             "due_date", // column name
             null, // order position
             null, // where value
             "invoice.dueDate.prompt", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "10", // position
             "currency", // table
             "symbol", // column name
             null, // order position
             null, // where value
             "currency.external.prompt.name", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "0", // functionable
             "1", // selectable
             "1", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"2", // report id
             null, // user_id
             "11", // position
             "invoice", // table
             "total", // column name
             null, // order position
             null, // where value
             "invoice.total.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "12", // position
             "invoice", // table
             "payment_attempts", // column name
             null, // order position
             null, // where value
             "invoice.attempts.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "13", // position
             "invoice", // table
             "to_process", // column name
             null, // order position
             null, // where value
             "invoice.is_payable", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "14", // position
             "invoice", // table
             "balance", // column name
             null, // order position
             null, // where value
             "invoice.balance.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "15", // position
             "invoice", // table
             "carried_balance", // column name
             null, // order position
             null, // where value
             "invoice.carriedBalance.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"2", // report id
             null, // user_id
             "16", // position
             "invoice", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"2", // report id
             null, // user_id
             "17", // position
             "invoice", // table
             "is_review", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"2", // report id
             null, // user_id
             "18", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             *  GENERAL PAYMENT
             */
            {"3", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             "payment.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            // this field is only used when executing the report internally
           {"3", // report id  
            null, // user_id
            "11", // position
            "base_user", // table
            "id", // column name
            null, // order position
            null , // where value 
            null, // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "0", // operatorable
            "0"  // wherable
            },
            {"3", // report id   
             null, // user_id
             "4", // position
             "payment", // table
             "attempt", // column name
             null, // order position
             null, // where value
             "payment.attempt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "5", // position
             "payment", // table
             "result_id", // column name
             null, // order position
             null, // where value
             "payment.resultId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "6", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "method_id", // column name
             null, // order position
             null, // where value
             "payment.methodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id
             null, // user_id
             "12", // position
             "payment", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"3", // report id
             null, // user_id
             "12", // position
             "payment", // table
             "is_refund", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             // now all the fields for the description of the
             // method and the result in the proper language
            {"3", // report id
             null, // user_id
             "12", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"3", // report id
             null, // user_id
             "14", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"3", // report id
             null, // user_id
             "12", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.method", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"3", // report id
             null, // user_id
             "13", // position
             "id2", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.result", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

             /*
              * Order lines
              */
            {"4", // report id
             null, // user_id
             "0", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"4", // report id
             null, // user_id
             "1", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"4", // report id
             null, // user_id
             "2", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             "order.external.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "3", // position
             "order_line", // table
             "item_id", // column name
             null, // order position
             null, // where value
             "item.prompt.number", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "4", // position
             "order_line", // table
             "type_id", // column name
             null, // order position
             null, // where value
             "order.line.prompt.typeid", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "5", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "order.line.prompt.type", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "6", // position
             "order_line", // table
             "description", // column name
             null, // order position
             null, // where value
             "order.line.prompt.description", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "7", // position
             "order_line", // table
             "amount", // column name
             null, // order position
             null, // where value
             "order.line.prompt.amount", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "8", // position
             "order_line", // table
             "quantity", // column name
             null, // order position
             null, // where value
             "order.line.prompt.quantity", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "9", // position
             "order_line", // table
             "price", // column name
             null, // order position
             null, // where value
             "order.line.prompt.price", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"4", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
             {"4", // report id
              null, // user_id
              "11", // position
              "purchase_order", // table
              "status_id", // column name
              null, // order position
              null, // where value
              "report.prompt.purchase_order.status", // title key
              null, // function
              "0", // is grouped
              "0", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "0", // functionable
              "0", // selectable
              "0", // ordenable
              "1", // operatorable
              "1"  // wherable
            }, 
            {"4", // report id
             null, // user_id
             "0", // position
             "purchase_order", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"4", // report id
             null, // user_id
             "0", // position
             "order_line", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"4", // report id
             null, // user_id
             "0", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"4", // report id
             null, // user_id
             "1", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             *  GENERAL REFUND
             */
            {"5", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             "payment.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            // this field is only used when executing the report internally
           {"5", // report id  
            null, // user_id
            "11", // position
            "base_user", // table
            "id", // column name
            null, // order position
            null , // where value 
            null, // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "0", // operatorable
            "0"  // wherable
            },
            {"5", // report id   
             null, // user_id
             "4", // position
             "payment", // table
             "attempt", // column name
             null, // order position
             null, // where value
             "payment.attempt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "5", // position
             "payment", // table
             "result_id", // column name
             null, // order position
             null, // where value
             "payment.resultId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "6", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "method_id", // column name
             null, // order position
             null, // where value
             "payment.methodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id   
             null, // user_id
             "10", // position
             "payment", // table
             "payout_id", // column name
             null, // order position
             null, // where value
             "payout.external.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"5", // report id
             null, // user_id
             "12", // position
             "payment", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"5", // report id
             null, // user_id
             "12", // position
             "payment", // table
             "is_refund", // column name
             null, // order position
             "1", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             // now all the fields for the description of the
             // method and the result in the proper language
            {"5", // report id
             null, // user_id
             "12", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"5", // report id
             null, // user_id
             "14", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"5", // report id
             null, // user_id
             "12", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.method", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"5", // report id
             null, // user_id
             "13", // position
             "id2", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.result", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             * Total invoiced by date range
             */
            {"6", // report id
             null, // user_id
             "1", // position
             "invoice", // table
             "total", // column name
             null, // order position
             null, // where value
             "invoice.total.prompt", // title key
             Field.FUNCTION_SUM, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"6", // report id
             null, // user_id
             "1", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_GR_EQ, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
             // this is the date again to allow 'between'
            {"6", // report id
             null, // user_id
             "2", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_SMALLER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
            {"6", // report id
             null, // user_id
             "0", // position
             "invoice", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"6", // report id
             null, // user_id
             "0", // position
             "invoice", // table
             "is_review", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"6", // report id
             null, // user_id
             "0", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             * Payment total by date range
             */
            {"7", // report id   
             null, // user_id
             "1", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             Field.FUNCTION_SUM, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"7", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_GR_EQ, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
            {"7", // report id   
             null, // user_id
             "3", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_SMALLER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
            {"7", // report id
             null, // user_id
             "0", // position
             "payment", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"7", // report id
             null, // user_id
             "0", // position
             "payment", // table
             "is_refund", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             // now all the fields for the description of the
             // method and the result in the proper language
            {"7", // report id
             null, // user_id
             "14", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             * Refund total by date range
             */
            {"8", // report id   
             null, // user_id
             "1", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             Field.FUNCTION_SUM, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"8", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_GR_EQ, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
            {"8", // report id   
             null, // user_id
             "3", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_SMALLER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             },
            {"8", // report id
             null, // user_id
             "0", // position
             "payment", // table
             "deleted", // column name
             null, // order position
             "0", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"8", // report id
             null, // user_id
             "0", // position
             "payment", // table
             "is_refund", // column name
             null, // order position
             "1", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"8", // report id
             null, // user_id
             "14", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             * Total ordered by date range
             */
            {"9", // report id
             null, // user_id
             "1", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_GR_EQ, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             }, 
            {"9", // report id
             null, // user_id
             "2", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_SMALLER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "1"  // wherable
             }, 
            {"9", // report id
             null, // user_id
             "1", // position
             "order_line", // table
             "amount", // column name
             null, // order position
             null, // where value
             "order.line.prompt.amount", // title key
             Field.FUNCTION_SUM, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             }, 
            {"9", // report id
             null, // user_id
             "0", // position, it can be 0 because it's not id linked
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             *  Overdue Invoices
             */
            {"10", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"10", // report id
             null, // user_id
             "2", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             "invoice.number", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             {"10", // report id
              null, // user_id
              "2", // position
              "invoice", // table
              "public_number", // column name
              null, // order position
              null, // where value
              "invoice.number.prompt", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_STRING, // data type
              null, // operator
              "0", // functionable
              "0", // selectable
              "0", // ordenable
              "0", // operatorable
              "0"  // wherable
             },
             
            {"10", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"10", // report id
             null, // user_id
             "4", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"10", // report id
             null, // user_id
             "5", // position
             "invoice", // table
             "total", // column name
             null, // order position
             null, // where value
             "invoice.total.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"10", // report id
             null, // user_id
             "10", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"10", // report id
             null, // user_id
             "11", // position
             "invoice", // table
             "due_date", // column name
             null, // order position
             "?", // where value
             "invoice.due_date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_SMALLER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

            /*
             *  Invoices with a carried balance
             */
            {"11", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "2", // position
             "invoice", // table
             "id", // column name
             null, // order position
             null, // where value
             "invoice.number", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "4", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.create_date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "5", // position
             "currency", // table
             "symbol", // column name
             null, // order position
             null, // where value
             "currency.external.prompt.name", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "6", // position
             "invoice", // table
             "total", // column name
             null, // order position
             null, // where value
             "invoice.total.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "99", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"11", // report id
             null, // user_id
             "99", // position
             "invoice", // table
             "carried_balance", // column name
             null, // order position
             "0", // where value
             "invoice.due_date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             Field.OPERATOR_GREATER, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             
            /*
             *  12 Partner selected : active orders from customers
             */
            {"12", // report id
             null, // user_id
             "1", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"12", // report id
             null, // user_id
             "2", // position
             "base_user", // table
             "id", // column name
             null, // order position
             null, // where value
             "customer.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"12", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "report.prompt.base_user.user_name", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"12", // report id
             null, // user_id
             "4", // position
             "purchase_order", // table
             "id", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "5", // position
             "purchase_order", // table
             "period_id", // column name
             null, // order position
             null, // where value
             "order.prompt.periodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "6", // position
             "purchase_order", // table
             "active_since", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_since", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "7", // position
             "purchase_order", // table
             "active_since", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_since", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "8", // position
             "purchase_order", // table
             "active_until", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_until", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "9", // position
             "purchase_order", // table
             "active_until", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.active_until", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "10", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             Field.OPERATOR_EQUAL, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "11", // position
             "purchase_order", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.purchase_order.create_datetime", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             }, 
            {"12", // report id
             null, // user_id
             "12", // position
             "customer", // table
             "partner_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"12", // report id
             null, // user_id
             "13", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"12", // report id
             null, // user_id
             "14", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "order.prompt.period", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             
             // 13 Partner selected - customer's payments
            {"13", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             "payment.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"3", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
           {"13", // report id  
            null, // user_id
            "4", // position
            "base_user", // table
            "id", // column name
            null, // order position
            null , // where value 
            "customer.prompt.id", // title key
            null, // function
            "0", // is grouped
            "1", // is shown
            Field.TYPE_INTEGER, // data type
            null, // operator
            "0", // functionable
            "1", // selectable
            "1", // ordenable
            "1", // operatorable
            "1"  // wherable
            },
            {"13", // report id   
             null, // user_id
             "5", // position
             "payment", // table
             "attempt", // column name
             null, // order position
             null, // where value
             "payment.attempt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "6", // position
             "payment", // table
             "result_id", // column name
             null, // order position
             null, // where value
             "payment.resultId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id
             null, // user_id
             "7", // position
             "id2", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.result", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             
            {"13", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "10", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "11", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "12", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id   
             null, // user_id
             "13", // position
             "payment", // table
             "method_id", // column name
             null, // order position
             null, // where value
             "payment.methodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"13", // report id
             null, // user_id
             "14", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.method", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"13", // report id
             null, // user_id
             "15", // position
             "customer", // table
             "partner_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"13", // report id
             null, // user_id
             "16", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
             
            /*
             *  14 Partner selected - customer refunds
             */
            {"14", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "2", // position
             "payment", // table
             "id", // column name
             null, // order position
             null, // where value
             "payment.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
           {"14", // report id  
            null, // user_id
            "4", // position
            "base_user", // table
            "id", // column name
            null, // order position
            null , // where value 
            "customer.prompt.id", // title key
            null, // function
            "0", // is grouped
            "1", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "1", // selectable
            "1", // ordenable
            "1", // operatorable
            "1"  // wherable
            },
            {"14", // report id   
             null, // user_id
             "5", // position
             "payment", // table
             "attempt", // column name
             null, // order position
             null, // where value
             "payment.attempt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "6", // position
             "payment", // table
             "result_id", // column name
             null, // order position
             null, // where value
             "payment.resultId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id
             null, // user_id
             "7", // position
             "id2", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.result", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "8", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payment.amount", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "10", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payment.createDate", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "11", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "12", // position
             "payment", // table
             "payment_date", // column name
             null, // order position
             null, // where value
             "payment.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id   
             null, // user_id
             "13", // position
             "payment", // table
             "method_id", // column name
             null, // order position
             null, // where value
             "payment.methodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"14", // report id
             null, // user_id
             "14", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.method", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"14", // report id
             null, // user_id
             "15", // position
             "customer", // table
             "partner_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"14", // report id
             null, // user_id
             "16", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
              
            /*
             * 15 - Partners
             */
            {"15", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "base_user", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "2", // position
             "partner", // table
             "id", // column name
             null, // order position
             null, // where value
             "partner.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "4", // position
             "partner", // table
             "balance", // column name
             null, // order position
             null, // where value
             "partner.prompt.balance", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "5", // position
             "partner", // table
             "total_payments", // column name
             null, // order position
             null, // where value
             "partner.prompt.totalPayments", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "6", // position
             "partner", // table
             "total_refunds", // column name
             null, // order position
             null, // where value
             "partner.prompt.totalRefunds", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "7", // position
             "partner", // table
             "total_payouts", // column name
             null, // order position
             null, // where value
             "partner.prompt.totalPayouts", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "8", // position
             "partner", // table
             "percentage_rate", // column name
             null, // order position
             null, // where value
             "partner.prompt.rate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "9", // position
             "partner", // table
             "referral_fee", // column name
             null, // order position
             null, // where value
             "partner.prompt.fee", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "10", // position
             "partner", // table
             "one_time", // column name
             null, // order position
             null, // where value
             "partner.prompt.onetime", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "11", // position
             "partner", // table
             "period_unit_id", // column name
             null, // order position
             null, // where value
             "partner.prompt.periodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "12", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "partner.prompt.period", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "13", // position
             "partner", // table
             "period_value", // column name
             null, // order position
             null, // where value
             "partner.prompt.periodValue", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "14", // position
             "partner", // table
             "next_payout_date", // column name
             null, // order position
             null, // where value
             "partner.prompt.nextPayout", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "15", // position
             "partner", // table
             "next_payout_date", // column name
             null, // order position
             null, // where value
             "partner.prompt.nextPayout", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "16", // position
             "partner", // table
             "automatic_process", // column name
             null, // order position
             null, // where value
             "partner.prompt.process", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id   
             null, // user_id
             "17", // position
             "partner", // table
             "related_clerk", // column name
             null, // order position
             null, // where value
             "partner.prompt.clerk", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"15", // report id
             null, // user_id
             "18", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"15", // report id
             null, // user_id
             "19", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

             
             /*
              * 16 - General Payouts
              */
            {"16", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "partner_payout", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "2", // position
             "partner", // table
             "id", // column name
             null, // order position
             null, // where value
             "partner.external.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "4", // position
             "partner_payout", // table
             "starting_date", // column name
             null, // order position
             null, // where value
             "payout.prompt.startDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "5", // position
             "partner_payout", // table
             "ending_date", // column name
             null, // order position
             null, // where value
             "payout.prompt.endDate", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "6", // position
             "partner_payout", // table
             "payments_amount", // column name
             null, // order position
             null, // where value
             "payout.prompt.payments", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "7", // position
             "partner_payout", // table
             "refunds_amount", // column name
             null, // order position
             null, // where value
             "payout.prompt.refunds", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "8", // position
             "partner_payout", // table
             "balance_left", // column name
             null, // order position
             null, // where value
             "payout.prompt.balance", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "9", // position
             "payment", // table
             "amount", // column name
             null, // order position
             null, // where value
             "payout.prompt.paid", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_FLOAT, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "10", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payout.prompt.date", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "11", // position
             "payment", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "payout.prompt.date", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id   
             null, // user_id
             "12", // position
             "payment", // table
             "method_id", // column name
             null, // order position
             null, // where value
             "payment.methodId", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
            {"16", // report id
             null, // user_id
             "13", // position
             "id", // table
             "content", // column name
             null, // order position
             null, // where value
             "payment.method", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"16", // report id
             null, // user_id
             "14", // position
             "base_user", // table
             "entity_id", // column name
             null, // order position
             "?", // where value - Dynamic: not known now, not known by the user
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },
            {"16", // report id
             null, // user_id
             "15", // position
             "id", // table
             "language_id", // column name
             null, // order position
             "?", // where value
             null, // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_INTEGER, // data type
             Field.OPERATOR_EQUAL, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
             },

             /*
              *  INVOICE LINES
              */
             {"17", // report id   // This is the id for the link
              null, // user_id
              "1", // position
              "invoice", // table
              "id", // column name
              null, // order position
              null, // where value
              null, // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "0", // functionable
              "0", // selectable
              "0", // ordenable
              "0", // operatorable
              "0"  // wherable
              },
             {"17", // report id   // This is the id for the link
              null, // user_id
              "2", // position
              "invoice", // table
              "id", // column name
              null, // order position
              null, // where value
              "invoice.id.prompt", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
             {"17", // report id   // This is the id for the link
              null, // user_id
              "2", // position
              "invoice", // table
              "public_number", // column name
              null, // order position
              null, // where value
              "invoice.number.prompt", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
             {"17", // report id
              null, // user_id
              "2", // position
              "invoice_line", // table
              "type_id", // column name
              null, // order position
              null, // where value
              "report.prompt.invoice_line.type_id", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
              {"17", // report id
               null, // user_id
               "3", // position
               "invoice_line_type", // table
               "description", // column name
               null, // order position
               null, // where value
               "report.prompt.invoice_line.type", // title key
               null, // function
               "0", // is grouped
               "1", // is shown
               Field.TYPE_STRING, // data type
               null, // operator
               "1", // functionable
               "1", // selectable
               "0", // ordenable
               "0", // operatorable
               "0"  // wherable
             },
             {"17", // report id
              null, // user_id
              "4", // position
              "invoice_line", // table
              "item_id", // column name
              null, // order position
              null, // where value
              "report.prompt.invoice_line.item_id", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
            {"17", // report id  
             null, // user_id
             "5", // position
             "invoice_line", // table
             "description", // column name
             null, // order position
             null , // where value 
             "report.prompt.invoice_line.description", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
             },
             {"17", // report id
              null, // user_id
              "6", // position
              "invoice_line", // table
              "amount", // column name
              null, // order position
              null, // where value
              "report.prompt.invoice_line.amount", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_FLOAT, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
             {"17", // report id
              null, // user_id
              "7", // position
              "invoice_line", // table
              "quantity", // column name
              null, // order position
              null, // where value
              "report.prompt.invoice_line.quantity", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_INTEGER, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
             {"17", // report id
              null, // user_id
              "8", // position
              "invoice_line", // table
              "price", // column name
              null, // order position
              null, // where value
              "report.prompt.invoice_line.price", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_FLOAT, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "1", // ordenable
              "1", // operatorable
              "1"  // wherable
              },
             {"17", // report id
              null, // user_id
              "9", // position
              "currency", // table
              "symbol", // column name
              null, // order position
              null, // where value
              "currency.external.prompt.name", // title key
              null, // function
              "0", // is grouped
              "1", // is shown
              Field.TYPE_STRING, // data type
              null, // operator
              "1", // functionable
              "1", // selectable
              "0", // ordenable
              "0", // operatorable
              "0"  // wherable
              },
            {"17", // report id  
             null, // user_id
             "10", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.createDateTime.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"17", // report id  
             null, // user_id
             "11", // position
             "invoice", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "invoice.createDateTime.prompt", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"17", // report id  
             null, // user_id
             "12", // position
             "invoice", // table
             "user_id", // column name
             null, // order position
             null, // where value
             "invoice.userId.prompt", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
             {"17", // report id
              null, // user_id
              "99", // position
              "base_user", // table
              "entity_id", // column name
              null, // order position
              "?", // where value - Dynamic: not known now, not known by the user
              null, // title key
              null, // function
              "0", // is grouped
              "0", // is shown
              Field.TYPE_INTEGER, // data type
              Field.OPERATOR_EQUAL, // operator
              "0", // functionable
              "0", // selectable
              "0", // ordenable
              "0", // operatorable
              "0"  // wherable
              },
             
           /*
            * USERS
            */
            {"18", // report id   // This is the id for the link
             null, // user_id
             "1", // position
             "base_user", // table
             "id", // column name
             null, // order position
             null, // where value
             null, // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"18", // report id 
             null, // user_id
             "2", // position
             "base_user", // table
             "id", // column name
             null, // order position
             null, // where value
             "user.prompt.id", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "3", // position
             "base_user", // table
             "user_name", // column name
             null, // order position
             null, // where value
             "user.prompt.username", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "4", // position
             "base_user", // table
             "language_id", // column name
             null, // order position
             null, // where value
             "report.prompt.user.languageCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "4", // position
             "language", // table
             "description", // column name
             null, // order position
             null, // where value
             "user.prompt.language", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "5", // position
             "base_user", // table
             "status_id", // column name
             null, // order position
             null, // where value
             "report.prompt.user.statusCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "6", // position
             "it1", // table
             "content", // column name
             null, // order position
             null, // where value
             "user.prompt.status", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "7", // position
             "base_user", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.user.create", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "8", // position
             "base_user", // table
             "create_datetime", // column name
             null, // order position
             null, // where value
             "report.prompt.user.create", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "9", // position
             "base_user", // table
             "last_status_change", // column name
             null, // order position
             null, // where value
             "report.prompt.user.status_change", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "10", // position
             "base_user", // table
             "last_status_change", // column name
             null, // order position
             null, // where value
             "report.prompt.user.status_change", // title key
             null, // function
             "0", // is grouped
             "0", // is shown
             Field.TYPE_DATE, // data type
             null, // operator
             "0", // functionable
             "0", // selectable
             "0", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "11", // position
             "user_role_map", // table
             "role_id", // column name
             null, // order position
             null, // where value
             "report.prompt.user.roleCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "12", // position
             "it2", // table
             "content", // column name
             null, // order position
             null, // where value
             "report.prompt.user.role", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "13", // position
             "contact", // table
             "organization_name", // column name
             null, // order position
             null, // where value
             "contact.prompt.organizationName", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "14", // position
             "contact", // table
             "street_addres1", // column name
             null, // order position
             null, // where value
             "contact.prompt.address1", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "15", // position
             "contact", // table
             "street_addres2", // column name
             null, // order position
             null, // where value
             "contact.prompt.address2", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "16", // position
             "contact", // table
             "city", // column name
             null, // order position
             null, // where value
             "contact.prompt.city", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "17", // position
             "contact", // table
             "state_province", // column name
             null, // order position
             null, // where value
             "contact.prompt.stateProvince", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "18", // position
             "contact", // table
             "postal_code", // column name
             null, // order position
             null, // where value
             "contact.prompt.postalCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "19", // position
             "contact", // table
             "country_code", // column name
             null, // order position
             null, // where value
             "report.prompt.user.countryCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "20", // position
             "it3", // table
             "content", // column name
             null, // order position
             null, // where value
             "contact.prompt.countryCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "0", // ordenable
             "0", // operatorable
             "0"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "21", // position
             "contact", // table
             "last_name", // column name
             null, // order position
             null, // where value
             "contact.prompt.lastName", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "22", // position
             "contact", // table
             "first_name", // column name
             null, // order position
             null, // where value
             "contact.prompt.firstName", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "23", // position
             "contact", // table
             "phone_country_code", // column name
             null, // order position
             null, // where value
             "contact.prompt.phoneCountryCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "24", // position
             "contact", // table
             "phone_area_code", // column name
             null, // order position
             null, // where value
             "contact.prompt.phoneAreaCode", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_INTEGER, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "25", // position
             "contact", // table
             "phone_phone_number", // column name
             null, // order position
             null, // where value
             "contact.prompt.phoneNumber", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
            {"18", // report id  
             null, // user_id
             "26", // position
             "contact", // table
             "email", // column name
             null, // order position
             null, // where value
             "contact.prompt.email", // title key
             null, // function
             "0", // is grouped
             "1", // is shown
             Field.TYPE_STRING, // data type
             null, // operator
             "1", // functionable
             "1", // selectable
             "1", // ordenable
             "1", // operatorable
             "1"  // wherable
            },
           {"18", // report id
            null, // user_id
            "27", // position
            "base_user", // table
            "entity_id", // column name
            null, // order position
            "?", // where value - Dynamic: not known now, not known by the user
            null, // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "0", // operatorable
            "0"  // wherable
           },
           {"18", // report id
            null, // user_id
            "28", // position
            "it1", // table
            "language_id", // column name
            null, // order position
            "?", // where value - Dynamic: not known now, not known by the user
            null, // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_INTEGER, // data type
            Field.OPERATOR_EQUAL, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "0", // operatorable
            "0"  // wherable
           },
           {"18", // report id  
            null, // user_id
            "29", // position
            "base_user", // table
            "last_login", // column name
            null, // order position
            null, // where value
            "user.prompt.lastLogin", // title key
            null, // function
            "0", // is grouped
            "1", // is shown
            Field.TYPE_DATE, // data type
            null, // operator
            "1", // functionable
            "1", // selectable
            "1", // ordenable
            "1", // operatorable
            "1"  // wherable
           },
           {"18", // report id  
            null, // user_id
            "30", // position
            "base_user", // table
            "last_login", // column name
            null, // order position
            null, // where value
            "user.prompt.lastLogin", // title key
            null, // function
            "0", // is grouped
            "0", // is shown
            Field.TYPE_DATE, // data type
            null, // operator
            "0", // functionable
            "0", // selectable
            "0", // ordenable
            "1", // operatorable
            "1"  // wherable
           },
             
        };
        addTable(Constants.TABLE_REPORT_FIELD, 
                reportFieldColumns, 
                reportFieldData, false);

    }    
}
