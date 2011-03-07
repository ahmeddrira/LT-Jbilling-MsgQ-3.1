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

package com.sapienter.jbilling.server.report.db;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.AbstractDescription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.File;
import java.io.Serializable;

/**
 * Report
 *
 * @author Brian Cowdery
 * @since 07/03/11
 */
@Entity
@Table(name = "report")
@TableGenerator(
    name = "report_GEN",
    table = "jbilling_seqs",
    pkColumnName = "name",
    valueColumnName = "next_id",
    pkColumnValue = "report",
    allocationSize = 10
)
public class ReportDTO extends AbstractDescription implements Serializable {

    private static final String BASE_PATH = Util.getSysProp("base_dir") + File.separator + "reports";

    private int id;
    private ReportTypeDTO type;
    private String name;
    private String fileName;
    private Integer versionNum;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "report_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    public ReportTypeDTO getType() {
        return type;
    }

    public void setType(ReportTypeDTO type) {
        this.type = type;
    }

    @Column(name = "name", updatable = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "file_name", updatable = true, nullable = false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Transient
    public String getReportFilePath() {
        return BASE_PATH + File.separator + getFileName();
    }

    @Transient
    public File getReportFile() {
        return fileName != null ? new File(getReportFilePath()) : null;
    }

    @Version
    @Column(name = "OPTLOCK")
    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    @Transient
    protected String getTable() {
        return Constants.TABLE_REPORT;
    }

    @Override
    public String toString() {
        return "Report{"
               + "id=" + id
               + ", type=" + (type != null ? type.getName() : null)
               + ", fileName='" + fileName + '\''
               + '}';
    }
}
