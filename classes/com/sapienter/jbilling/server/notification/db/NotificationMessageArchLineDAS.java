package com.sapienter.jbilling.server.notification.db;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class NotificationMessageArchLineDAS extends
        AbstractDAS<NotificationMessageArchLineDTO> {

    public static final int CONTENT_MAX_LENGTH = 500;

    public NotificationMessageArchLineDTO create(String content, Integer section) {
        NotificationMessageArchLineDTO nmal = new NotificationMessageArchLineDTO();

        if (content.length() > CONTENT_MAX_LENGTH) {
            content = content.substring(0, CONTENT_MAX_LENGTH);
            Logger.getLogger(NotificationMessageArchLineDAS.class).warn(
                    "Trying to insert line too long. Truncating to "
                            + CONTENT_MAX_LENGTH);
        }

        nmal.setSection(section);
        nmal.setContent(content);

        return save(nmal);
    }

}
