package com.sapienter.jbilling.server.notification.db;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class NotificationMessageLineDAS extends
        AbstractDAS<NotificationMessageLineDTO> {

    public NotificationMessageLineDTO create(String line) {
        NotificationMessageLineDTO nml = new NotificationMessageLineDTO();
        nml.setContent(line);
        return save(nml);
    }
}
