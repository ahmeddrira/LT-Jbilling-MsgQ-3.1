package com.sapienter.jbilling.server.process.db;

import com.sapienter.jbilling.server.util.db.AbstractDAS;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author abimael
 *
 */
public class ProcessRunTotalPmDAS extends AbstractDAS<ProcessRunTotalPmDTO> {

    public ProcessRunTotalPmDTO create(Float total) {
        ProcessRunTotalPmDTO newEntity = new ProcessRunTotalPmDTO();
        newEntity.setTotal(total);
        return save(newEntity);
    }
    
     /**
     * Returns the locked row, since payment processing updates this in parallel
     * @param run
     * @param currencyId
     * @return
     */
    public ProcessRunTotalPmDTO getByMethod(Integer methodId, ProcessRunTotalDTO total) {
        Criteria criteria = getSession().createCriteria(ProcessRunTotalPmDTO.class)
				.createAlias("processRunTotal", "r")
					.add(Restrictions.eq("r.id", total.getId()))
				.createAlias("paymentMethod", "c")
					.add(Restrictions.eq("c.id", methodId)).setLockMode(LockMode.UPGRADE)
                .setComment("ProcessRunTotalPmDAS.getByMethod");

		return (ProcessRunTotalPmDTO) criteria.uniqueResult();
    }
   

}
