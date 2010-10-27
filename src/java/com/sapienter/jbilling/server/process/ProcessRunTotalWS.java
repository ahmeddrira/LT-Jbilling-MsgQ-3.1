/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.process;

import com.sapienter.jbilling.server.process.db.ProcessRunTotalDTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ProcessRunTotalWS
 *
 * @author Brian Cowdery
 * @since 25-10-2010
 */
public class ProcessRunTotalWS implements Serializable {

    private Integer id;
    private Integer processRunId;
    private Integer currencyId;
    private BigDecimal totalInvoiced;
    private BigDecimal totalPaid;
    private BigDecimal totalNotPaid;

    public ProcessRunTotalWS() {
    }

    public ProcessRunTotalWS(ProcessRunTotalDTO dto) {
        this.id = dto.getId();
        this.processRunId = dto.getProcessRun() != null ? dto.getProcessRun().getId() : null;
        this.currencyId = dto.getCurrency() != null ? dto.getCurrency().getId() : null;
        this.totalInvoiced = dto.getTotalInvoiced();
        this.totalPaid = dto.getTotalPaid();
        this.totalNotPaid = dto.getTotalNotPaid();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProcessRunId() {
        return processRunId;
    }

    public void setProcessRunId(Integer processRunId) {
        this.processRunId = processRunId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getTotalInvoiced() {
        return totalInvoiced;
    }

    public void setTotalInvoiced(BigDecimal totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalNotPaid() {
        return totalNotPaid;
    }

    public void setTotalNotPaid(BigDecimal totalNotPaid) {
        this.totalNotPaid = totalNotPaid;
    }

    @Override
    public String toString() {
        return "ProcessRunTotalWS{"
               + "id=" + id
               + ", processRunId=" + processRunId
               + ", currencyId=" + currencyId
               + ", totalInvoiced=" + totalInvoiced
               + ", totalPaid=" + totalPaid
               + ", totalNotPaid=" + totalNotPaid
               + '}';
    }
}
