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

package com.sapienter.jbilling.server.mediation.db;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.util.db.AbstractDAS;
import org.hibernate.Query;

import java.util.List;

public class MediationProcessDAS extends AbstractDAS<MediationProcess> {

    private static final String FIND_ALL_BY_ENTITY_HQL =
        "select process " +
            " from MediationProcess process " +
            " where process.configuration.entityId = :entity " +
            " order by id desc";

    /**
     * Returns all processes for the given company id.
     *
     * @param entityId company id of mediation process
     * @return list of processes, empty if none found
     */
    @SuppressWarnings("unchecked")
    public List<MediationProcess> findAllByEntity(Integer entityId) {
        Query query = getSession().createQuery(FIND_ALL_BY_ENTITY_HQL);
        query.setParameter("entity", entityId);
        return query.list();
    }

    private static final String IS_PROCESS_RUNNING_HQL =
        "select process.id " +
            " from MediationProcess process " +
            " where process.configuration.entityId = :entityId " +
            " and process.endDatetime is null";

    /**
     * Returns true if there is a current MediationProcess running, false if
     * no running MediationProcess found.
     *
     * @param entityId company id of mediation process
     * @return true if process running, false if not
     */
    public boolean isProcessing(Integer entityId) {
        Query query = getSession().createQuery(IS_PROCESS_RUNNING_HQL);
        query.setParameter("entityId", entityId);

        return !query.list().isEmpty();
    }

    private static final String IS_CONFIGURATION_RUNNING_HQL =
        "select process.id " +
            " from MediationProcess process " +
            " where process.configuration.id = :cfgId " +
            " and process.endDatetime is null";

    /**
     * Returns true if there is a current MediationProcess running for specified configuration,
     * false if no running MediationProcess found.
     *
     * @param configurationId configuration id of mediation process
     * @return true if process running, false if not
     */
    public boolean isConfigurationProcessing(Integer configurationId) {
        Query query = getSession().createQuery(IS_CONFIGURATION_RUNNING_HQL);
        query.setParameter("cfgId", configurationId);

        return !query.list().isEmpty();
    }

    private static final String LATEST_MEDIATION_PROCESS_FOR_ENTITY_HQL =
        "select process " +
            " from MediationProcess process " +
            " where process.configuration.entityId = :entityId " +
            " order by process.startDatetime desc";

    /**
     * Search latest (by start time) mediation process for entity
     * @param entityId  company id of mediation process
     * @return mediation process or null if not found
     */
    public MediationProcess getLatestMediationProcess(Integer entityId) {
        Query query = getSession().createQuery(LATEST_MEDIATION_PROCESS_FOR_ENTITY_HQL);
        query.setParameter("entityId", entityId);
        query.setMaxResults(1);

        return (MediationProcess) query.uniqueResult();
    }

    private static final String IS_MEDIATION_PROCESS_FAILED_HQL =
        "select count(*) " +
            " from MediationRecordDTO record " +
            " where record.process.id = :processId and (" +
                "record.recordStatus.id = :errorDetected or record.recordStatus.id = :errorDeclared" +
            ")";

    /**
     * Checks mediation records failed status for process specified
     * @param mediationProcessId mediation process id for records to check
     * @return true if exist at least one failed (not done) mediation record
     *         false otherwise
     */
    public boolean isMediationProcessHasFailedRecords(Integer mediationProcessId) {
        Query query = getSession().createQuery(IS_MEDIATION_PROCESS_FAILED_HQL);
        query.setParameter("processId", mediationProcessId);
        query.setParameter("errorDetected", Constants.MEDIATION_RECORD_STATUS_ERROR_DETECTED);
        query.setParameter("errorDeclared", Constants.MEDIATION_RECORD_STATUS_ERROR_DECLARED);

        return (Long) query.list().get(0) > 0;
    }

    /**
     * Touches all processes in the given list, initializing lazy
     * loaded associations in each object.
     *
     * @param list processes to touch
     */
    public void touch(List<MediationProcess> list) {
        super.touch(list, "getOrdersAffected");
        for (MediationProcess proc : list) {
            proc.getConfiguration().getCreateDatetime();
        }
    }
}
