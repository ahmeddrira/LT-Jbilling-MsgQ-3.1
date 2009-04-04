/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.sapienter.jbilling.server.process.db;


import com.sapienter.jbilling.server.util.db.AbstractDAS;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Restrictions;

public class ProcessRunTotalDAS extends AbstractDAS<ProcessRunTotalDTO> {

	public ProcessRunTotalDTO create(ProcessRunDTO run, Float invoiced, Float notPaid,
			Float paid, Integer currencyId) {
		ProcessRunTotalDTO dto = new ProcessRunTotalDTO();
		dto.setTotalInvoiced(invoiced);
		dto.setTotalNotPaid(notPaid);
		dto.setTotalPaid(paid);
		dto.setCurrency(new CurrencyDAS().find(currencyId));
        dto.setProcessRun(run);
        dto = save(dto);
        run.getProcessRunTotals().add(dto);
		return dto;
	}

    /**
     * Returns the locked row, since payment processing updates this in parallel
     * @param run
     * @param currencyId
     * @return
     */
    public ProcessRunTotalDTO getByCurrency(ProcessRunDTO run, Integer currencyId) {
        Criteria criteria = getSession().createCriteria(ProcessRunTotalDTO.class)
				.createAlias("processRun", "r")
					.add(Restrictions.eq("r.id", run.getId()))
				.createAlias("currency", "c")
					.add(Restrictions.eq("c.id", currencyId)).setLockMode(LockMode.UPGRADE)
                .setComment("ProcessRunTotalDAS.getByCurrency");

		return (ProcessRunTotalDTO) criteria.uniqueResult();
    }

}
