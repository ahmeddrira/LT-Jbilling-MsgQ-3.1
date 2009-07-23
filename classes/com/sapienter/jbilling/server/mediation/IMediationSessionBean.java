/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.mediation;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.sapienter.jbilling.common.InvalidArgumentException;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationRecordDTO;
import com.sapienter.jbilling.server.mediation.db.MediationRecordLineDTO;
import com.sapienter.jbilling.server.mediation.task.IMediationProcess;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.TaskException;

/**
 *
 * @author emilc
 **/
public interface IMediationSessionBean {

    public void trigger();

    /**
     * Needs to be in its own transaction, so it gets created right away
     */
    public MediationProcess createProcessRecord(MediationConfiguration cfg);

    public List<MediationProcess> getAll(Integer entityId);

    public List<MediationConfiguration> getAllConfigurations(Integer entityId);

    public void createConfiguration(MediationConfiguration cfg);

    public List updateAllConfiguration(Integer executorId,
            List<MediationConfiguration> configurations)
            throws InvalidArgumentException;

    public void delete(Integer executorId, Integer cfgId);

    public boolean isBeenProcessed(MediationProcess process, 
            Vector<Record> thisGroup);

    public void normalizeRecordGroup(IMediationProcess processTask, 
            Integer executorId, MediationProcess process, 
            Vector<Record> thisGroup, Integer entityId,
            MediationConfiguration cfg) throws TaskException;

    public void saveEventRecordLines(Vector<OrderLineDTO> newLines, 
            MediationRecordDTO record, Date eventDate, String description);

    public List<MediationRecordLineDTO> getEventsForOrder(Integer orderId);
}
