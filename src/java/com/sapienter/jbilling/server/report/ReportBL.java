/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.report;

import com.sapienter.jbilling.server.report.db.ReportDAS;
import com.sapienter.jbilling.server.report.db.ReportDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Context;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

/**
 * ReportBL
 *
 * @author Brian Cowdery
 * @since 08/03/11
 */
public class ReportBL {

    private static final Logger LOG = Logger.getLogger(ReportBL.class);

    public static final String PARAMETER_SUBREPORT_DIR = "SUBREPORT_DIR";

    private ReportDTO report;
    private Locale locale;

    private ReportDAS reportDas;

    public ReportBL() {
        _init();
    }

    public ReportBL(Integer id, Integer userId) {
        _init();
        set(id);
        setLocale(userId);
    }

    public ReportBL(ReportDTO report, Locale locale) {
        _init();
        this.report = report;
        this.locale = locale;
    }

    private void _init() {
        this.reportDas = new ReportDAS();
    }

    public void set(Integer id) {
        this.report = reportDas.find(id);
    }

    public void setLocale(Integer userId) {
        this.locale = new UserBL(userId).getLocale();
    }

    public ReportDTO getEntity() {
        return this.report;
    }

    /**
     * Render report as HTML to the given HTTP response stream.
     *
     * @param response response stream
     */
    public void renderHtml(HttpServletResponse response) {
        response.setContentType("text/html");

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            LOG.error("Exception occurred retrieving the print writer for the response stream.", e);
            return;
        }

        JasperPrint print = run();

        JRHtmlExporter exporter = new JRHtmlExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, writer);
        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");

        try {
            exporter.exportReport();
        } catch (JRException e) {
            LOG.error("Exception occurred exporting jasper report to HTML.", e);
        }
    }

    /**
     * Run this report.
     *
     * This method assumes that the report object contains parameters that have been populated
     * with a value to use when running the report.
     *
     * @return JasperPrint output file
     */
    public JasperPrint run() {
        return run(report.getReportFile(), report.getReportBaseDir(), report.getParameterMap(), locale);
    }

    /**
     * Run the given report design file with the given parameter list.
     *
     * @param report report design file
     * @param baseDir report base directory
     * @param parameters report parameters
     * @param locale user locale
     * @return JasperPrint output file
     */
    public static JasperPrint run(File report,String baseDir, Map<String, Object> parameters, Locale locale) {
        // add user locale and sub report directory
        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put(PARAMETER_SUBREPORT_DIR, baseDir);

        // get database connection
        DataSource dataSource = Context.getBean(Context.Name.DATA_SOURCE);
        Connection connection = DataSourceUtils.getConnection(dataSource);

        // run report
        FileInputStream inputStream = null;
        JasperPrint print = null;
        try {
            inputStream = new FileInputStream(report);
            print = JasperFillManager.fillReport(inputStream, parameters, connection);

        } catch (FileNotFoundException e) {
            LOG.error("Report design file " + report.getPath() + " not found.", e);

        } catch (JRException e) {
            LOG.error("Exception occurred generating jasper report.", e);

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    /* ignore*/
                }
            }
        }

        // release connection
        DataSourceUtils.releaseConnection(connection, dataSource);

        return print;
    }
}
