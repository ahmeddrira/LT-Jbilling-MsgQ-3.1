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

package com.sapienter.jbilling.server.report;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportEntityLocal;
import com.sapienter.jbilling.interfaces.ReportEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportFieldEntityLocal;
import com.sapienter.jbilling.interfaces.ReportFieldEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportTypeEntityLocal;
import com.sapienter.jbilling.interfaces.ReportTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.ReportUserEntityLocal;
import com.sapienter.jbilling.interfaces.ReportUserEntityLocalHome;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.DTOFactory;
import com.sapienter.jbilling.server.util.Util;


public class ReportBL extends ResultList {
    
    Logger log = null;
    ReportUserEntityLocalHome reportUserHome = null;
    ReportUserEntityLocal report = null;
    ReportEntityLocalHome reportHome = null;
    ReportFieldEntityLocalHome reportFieldHome = null;
    ReportTypeEntityLocalHome reportTypeHome = null;

    JNDILookup EJBFactory = null;
    
    public ReportBL() throws NamingException {
        log = Logger.getLogger(ReportBL.class);
        EJBFactory = JNDILookup.getFactory(false);

        reportUserHome =
                (ReportUserEntityLocalHome) EJBFactory.lookUpLocalHome(
                ReportUserEntityLocalHome.class,
                ReportUserEntityLocalHome.JNDI_NAME);

        reportHome =
                (ReportEntityLocalHome) EJBFactory.lookUpLocalHome(
                ReportEntityLocalHome.class,
                ReportEntityLocalHome.JNDI_NAME);

        reportFieldHome =
                (ReportFieldEntityLocalHome) EJBFactory.lookUpLocalHome(
                ReportFieldEntityLocalHome.class,
                ReportFieldEntityLocalHome.JNDI_NAME);

        reportTypeHome =
               (ReportTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
               ReportTypeEntityLocalHome.class,
               ReportTypeEntityLocalHome.JNDI_NAME);

    }
    
    private String parseSQL(ReportDTOEx report) 
            throws SessionInternalError {
        StringBuffer select = new StringBuffer("select ");
        StringBuffer from = new StringBuffer("from " + report.getTables());
        StringBuffer where = new StringBuffer("where " + report.getWhere());
        StringBuffer orderBy = new StringBuffer("order by ");
        StringBuffer groupBy = new StringBuffer("group by ");
        String selectSeparator = "";
        String whereSeparator;
        String orderBySeparator = "";
        String groupBySeparator = "";
        String columnHelper[] = new String[report.getFields().size()];
        int orderedColumns = 0;
        boolean isGrouped = false;
        int firstField = 0; // normaly 0, but if is agregated and 
                            // with id column it has to skip the first
        
        // the first column has to be always the id column, due to the
        // insertRowTag. For agregated reports or those that don't have one,
        // a static 1 is added.
        if (report.getIdColumn().intValue() != 1 || 
                report.getAgregated().booleanValue()) {
            select.append("1,"); 
        }
        
        // reports with id column that are run agregated have to loose their
        // id column
        firstField = report.getFirstFieldIndex();
        
        for (int f = firstField; f < report.getFields().size(); f++) {
            Field field = (Field) report.getFields().get(f);
            
            // add the column to the select 
            if (field.getIsShown().intValue() == 1) {
                
                if (field.getFunction() != null) {
                    columnHelper[f] = field.getFunction() + "(" +
                            field.getTable() + "." + field.getColumn() + ")";
                } else {
                    columnHelper[f] = field.getTable() + "." + 
                            field.getColumn();
                }
                select.append(selectSeparator + columnHelper[f]);
                selectSeparator = ",";
            }
            
            // add the condition to the where
            if (report.getWhere() != null &&
                    report.getWhere().length() > 0) {
                whereSeparator = " and ";
            } else {
                whereSeparator = "";
            }
            if (field.getWhereValue() != null &&
                    field.getWhereValue().length() > 0) {
                // now we can only state the where, we can't put the value
                // or we would clogg the database cache
                if (field.getDataType().equals(Field.TYPE_INTEGER) &&
                        field.getWhereValue().indexOf(',') >= 0) {
                    String oper;
                    if (field.getOperator().equals(Field.OPERATOR_EQUAL)) {
                        oper = " in";
                    } else if (field.getOperator().equals(
                            Field.OPERATOR_DIFFERENT)) {
                        oper = " not in";
                    } else {
                        throw new SessionInternalError("operator mismatch for " +
                                "null value");
                    }
                    where.append(whereSeparator + field.getTable() + "." + 
                            field.getColumn() + oper + "(");
                    StringTokenizer tok = new StringTokenizer(
                            field.getWhereValue().toString(), ",");
                    for (int ff = 0; ff < tok.countTokens(); ff++) {
                        where.append("?");
                        if (ff + 1 < tok.countTokens()) {
                            where.append(",");
                        }
                    }
                    where.append(")");
                } else {
                    String oper = field.getOperator();
                    if (field.getWhereValue().equalsIgnoreCase("null")) {
                        
                        if (field.getOperator().equals(Field.OPERATOR_EQUAL)) {
                            oper = "is";
                        } else if (field.getOperator().equals(
                                Field.OPERATOR_DIFFERENT)) {
                            oper = "is not";
                        } else {
                            throw new SessionInternalError("operator mismatch for " +
                                    "null value");
                        }
                    } 
                    where.append(whereSeparator + field.getTable() + "." + 
                            field.getColumn() + " " +
                            oper + " ?");
                }
                whereSeparator = " and ";
            }
            
            // the order can only be marked, as it has to follow a particular order
            if (field.getOrderPosition() != null) {
                orderedColumns++;
            }
            
            // the group by
            if (field.getIsGrouped().intValue() == 1) {
                isGrouped = true;
                groupBy.append(groupBySeparator + field.getTable() + "." + 
                        field.getColumn());
                groupBySeparator = ",";
            }
        }
        
        // now take care of the order by
        orderBySeparator = "";
        if (orderedColumns > 0) {
            for (int done = 1; done <= orderedColumns; done++) {
                int f;
                for (f = 0; f < report.getFields().size(); f++) {
                    Field field = (Field) report.getFields().get(f);
                    if (field.getOrderPosition() != null && 
                            field.getOrderPosition().intValue() == done) {
                        String orderByStr = null;
                        if (field.getIsShown().intValue() == 1) {
                            orderByStr = columnHelper[f];
                        } else {
                            orderByStr = field.getTable() + "." +
                                    field.getColumn();
                        }
                        log.debug("Adding to orderby " + orderBySeparator + orderByStr);
                        orderBy.append(orderBySeparator + orderByStr);    
                        orderBySeparator = ",";
                        break;
                    }
                }
                if (f >= report.getFields().size()) {
                    throw new SessionInternalError("The ordered fields are" +
                            "inconsistent. Can't find field " + done);                }            
            }       
        }

        // construct the query string
        // first the select - from - where        
        StringBuffer completeQuery = new StringBuffer(select + " " + from + 
                " " + where);  
        // then the group by 
        if (isGrouped) {
            completeQuery.append(" " + groupBy);
        }
        // last the order by 
        if (orderedColumns > 0) {
            completeQuery.append(" " + orderBy);
        }
        log.debug("Generated:[" + completeQuery.toString() + "]");
        return completeQuery.toString();
    }
    
    protected CachedRowSet execute(ReportDTOEx reportDto) 
            throws SQLException, NamingException, SessionInternalError,
            Exception {
        int index = 1;
        int dynamicVariableIndex = 0;
        // make the preparation 
        prepareStatement(parseSQL(reportDto));
       
        // set all the variables of the query
        for (int f=0; f < reportDto.getFields().size(); f++) {
            Field field = (Field) reportDto.getFields().get(f);
            if (field.getWhereValue() != null &&
                    field.getWhereValue().length() > 0) {
                if (field.getWhereValue().equals("?")) {
                    // this is a dynamic variable, the value it's not known in the field
                    // record nor is entered by the user
                    field.setWhereValue(reportDto.getDynamicParameter(
                            dynamicVariableIndex));
                    dynamicVariableIndex++;
                } 

                log.debug("Setting " + field.getColumn() + " index " + index +
                        " value " + field.getWhereValue());
                // see if this is just a null 
                if (field.getWhereValue().equalsIgnoreCase("null")) {
                    cachedResults.setNull(index, toSQLType(
                            field.getDataType()));
                } else {
                    if (field.getDataType().equals(Field.TYPE_DATE) ||
                            field.getDataType().equals(Field.TYPE_STRING)) {
                        cachedResults.setString(index, field.getWhereValue());
                    } else if (field.getDataType().equals(Field.TYPE_INTEGER)) {
                        if (field.getWhereValue().indexOf(',') >= 0) {
                            // it is an 'in' with many values
                            StringTokenizer tok = new StringTokenizer(
                                    field.getWhereValue(), ",");
                            int ff = 0;
                            for (;tok.hasMoreElements(); ff++) {
                                log.debug("    in(...) value. index " + 
                                        (index + ff));
                                cachedResults.setInt(index + ff, Integer.valueOf(
                                        tok.nextToken()).intValue());
                            }
                            index += ff - 1;
                        } else {
                            cachedResults.setInt(index, Integer.valueOf(
                                    field.getWhereValue()).intValue());
                        }
                    } else if (field.getDataType().equals(Field.TYPE_FLOAT)) {
                        cachedResults.setFloat(index, Util.string2float(
                                field.getWhereValue(), 
                                reportDto.getLocale()).floatValue());
                    }
                }
                
                index++;
            }
        }
        
        execute();
        conn.close();
        return cachedResults;        
    }
    
   
    public ReportDTOEx getReport(Integer reportId, Integer entityId) 
            throws NamingException, FinderException, SessionInternalError {
        log.debug("Getting report " + reportId + " for entity " + entityId);
        return DTOFactory.getReportDTOEx(reportId, entityId);
    }

    public ReportDTOEx getReport(Integer userReportId) 
            throws NamingException, FinderException, SessionInternalError {
        log.debug("Getting user report " + userReportId);
        ReportDTOEx reportDto = null;
        
        ReportUserEntityLocal reportUser = reportUserHome.findByPrimaryKey(
                userReportId);
        
        // create the initial report dto from the relationship
        UserBL user = new UserBL(reportUser.getUser());
        reportDto = DTOFactory.getReportDTOEx(reportUser.getReport(), user.getLocale());
        reportDto.setUserReportId(userReportId);
        
        // find this user's saved fields
        Collection fields = reportUser.getFields();
        
        // convert these entities to dtos
        for (Iterator it = fields.iterator(); it.hasNext(); ){
            ReportFieldEntityLocal field = (ReportFieldEntityLocal) it.next();
            reportDto.addField(DTOFactory.getFieldDTO(field));
        }

        return reportDto;
    }
    
    public Collection getList(Integer entityId) 
            throws SQLException, Exception{
        EntityEntityLocalHome entityHome =
                (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                EntityEntityLocalHome.class,
                EntityEntityLocalHome.JNDI_NAME);

        EntityEntityLocal entity = entityHome.findByPrimaryKey(entityId);
        
        Collection reports = entity.getReports();
        return DTOFactory.reportEJB2DTOEx(reports, true);

    }
    
    public Collection getListByType(Integer typeId) 
            throws FinderException {
        ReportTypeEntityLocal type = reportTypeHome.findByPrimaryKey(
                typeId);
        return DTOFactory.reportEJB2DTOEx(type.getReports(), false);
    }
    
    public void save(ReportDTOEx report, Integer userId, String title) 
            throws NamingException, FinderException, CreateException {
        
        ReportEntityLocal reportRow = reportHome.findByPrimaryKey(
                report.getId());
        // create the report user row
        ReportUserEntityLocal reportUser = reportUserHome.create(title,
                reportRow, userId);
        
        // now all the fields rows
        for (int f = 0; f < report.getFields().size(); f++) {
            Field field = (Field) report.getFields().get(f);
            
            ReportFieldEntityLocal fieldRow = reportFieldHome.create(
                    field.getPosition(), field.getTable(), field.getColumn(),
                    field.getIsGrouped(), field.getIsShown(), 
                    field.getDataType(), field.getFunctionable(),
                    field.getSelectable(), field.getOrdenable(),
                    field.getOperatorable(), field.getWherable());
            
            fieldRow.setOrderPosition(field.getOrderPosition());
            fieldRow.setWhereValue(field.getWhereValue());
            fieldRow.setTitleKey(field.getTitleKey());
            fieldRow.setFunction(field.getFunction());
            fieldRow.setOperator(field.getOperator());
            
            reportUser.getFields().add(fieldRow);
        }
    }
    
    public void delete(Integer userReportId) 
            throws NamingException, FinderException, RemoveException {
        report = reportUserHome.findByPrimaryKey(
                userReportId);
        report.remove();
    }
    
    public Collection getUserList(Integer report, Integer userId) 
            throws FinderException {
        Collection reports = reportUserHome.findByTypeUser(report, userId);
        return DTOFactory.reportUserEJB2DTO(reports);
    }
    
    private static int toSQLType(String type) 
            throws SessionInternalError {
        if (type.equals(Field.TYPE_DATE)) {
            return Types.DATE;
        } else if (type.equals(Field.TYPE_FLOAT)) {
            return Types.FLOAT;
        } else if (type.equals(Field.TYPE_INTEGER)) {
            return Types.INTEGER;
        } else if (type.equals(Field.TYPE_STRING)) {
            return Types.VARCHAR;
        } else {
            throw new SessionInternalError(type + " is not a supported type");
        }
    }
}
