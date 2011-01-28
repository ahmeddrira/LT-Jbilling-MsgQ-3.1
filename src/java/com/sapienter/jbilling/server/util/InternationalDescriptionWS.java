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

package com.sapienter.jbilling.server.util;

import com.sapienter.jbilling.server.util.db.InternationalDescriptionDTO;

/**
 * InternationalDescriptionWS
 *
 * @author Brian Cowdery
 * @since 27/01/11
 */
public class InternationalDescriptionWS {

    private Integer tableId;
    private Integer foreignId;
    private String psudoColumn;
    private Integer languageId;
    private String content;

    public InternationalDescriptionWS() {
    }

    public InternationalDescriptionWS(Integer languageId, String content) {
        this.psudoColumn = "description";
        this.languageId = languageId;
        this.content = content;
    }

    public InternationalDescriptionWS(Integer tableId, Integer foreignId, String psudoColumn, Integer languageId, String content) {
        this.tableId = tableId;
        this.foreignId = foreignId;
        this.psudoColumn = psudoColumn;
        this.languageId = languageId;
        this.content = content;
    }

    public InternationalDescriptionWS(InternationalDescriptionDTO description) {
        if (description.getId() != null) {
            this.tableId = description.getId().getTableId();
            this.foreignId = description.getId().getForeignId();
            this.psudoColumn = description.getId().getPsudoColumn();
            this.languageId = description.getId().getLanguageId();
        }
        this.content = description.getContent();
    }


    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getForeignId() {
        return foreignId;
    }

    public void setForeignId(Integer foreignId) {
        this.foreignId = foreignId;
    }

    public String getPsudoColumn() {
        return psudoColumn;
    }

    public void setPsudoColumn(String psudoColumn) {
        this.psudoColumn = psudoColumn;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InternationalDescriptionWS{"
               + "tableId=" + tableId
               + ", foreignId=" + foreignId
               + ", psudoColumn='" + psudoColumn + '\''
               + ", languageId=" + languageId
               + ", content='" + content + '\''
               + '}';
    }
}
