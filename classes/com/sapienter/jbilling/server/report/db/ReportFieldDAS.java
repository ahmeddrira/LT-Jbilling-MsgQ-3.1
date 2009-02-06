package com.sapienter.jbilling.server.report.db;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class ReportFieldDAS extends AbstractDAS<ReportFieldDTO> {
	
	public ReportFieldDTO create(Integer position, String table, String column,
            Integer isGrouped, Integer isShown, String type,
            Integer funFlag, Integer selFlag, Integer ordFlag, Integer opeFlag,
            Integer werFlag) {
		
		
		ReportFieldDTO newEntity = new ReportFieldDTO();
		newEntity.setPositionNumber(position);
		newEntity.setTableName(table);
		newEntity.setColumnName(column);
		newEntity.setIsGrouped(isGrouped);
		newEntity.setIsShown(isShown);
		newEntity.setDataType(type);
		newEntity.setFunctionable(funFlag);
		newEntity.setSelectable(selFlag);
		newEntity.setOrdenable(ordFlag);
		newEntity.setOperatorable(opeFlag);
		newEntity.setWhereable(werFlag);
		
		
		return save(newEntity);
		
	}

}
