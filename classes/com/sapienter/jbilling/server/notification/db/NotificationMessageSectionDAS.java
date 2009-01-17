package com.sapienter.jbilling.server.notification.db;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class NotificationMessageSectionDAS extends
        AbstractDAS<NotificationMessageSectionDTO> {

    public NotificationMessageSectionDTO create(Integer section) {
        NotificationMessageSectionDTO nms = new NotificationMessageSectionDTO();
        nms.setSection(section);
        return save(nms);
    }

}
