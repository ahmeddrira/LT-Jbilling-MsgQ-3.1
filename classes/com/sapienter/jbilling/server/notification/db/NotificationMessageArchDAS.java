package com.sapienter.jbilling.server.notification.db;

import java.util.Date;
import java.util.HashSet;

import com.sapienter.jbilling.server.notification.MessageSection;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

/**
 * 
 * @author abimael
 * 
 */
public class NotificationMessageArchDAS extends
        AbstractDAS<NotificationMessageArchDTO> {
    private static int LINE_LENGTH = 500;

    public NotificationMessageArchDTO create(Integer id,
            MessageSection[] sections) {

        NotificationMessageArchLineDAS lineHome = new NotificationMessageArchLineDAS();

        HashSet<NotificationMessageArchLineDTO> newLines = new HashSet<NotificationMessageArchLineDTO>();
        for (int f = 0; f < sections.length; f++) {

            String content = sections[f].getContent();
            for (int index = 0; index < content.length(); index += LINE_LENGTH) {
                int end = (content.length() < index + LINE_LENGTH) ? content
                        .length() : index + LINE_LENGTH;
                NotificationMessageArchLineDTO line = lineHome.create(content
                        .substring(index, end), sections[f].getSection());
                newLines.add(line);
            }
        }

        NotificationMessageArchDTO nma = new NotificationMessageArchDTO();
        nma.setTypeId(id);
        nma.setCreateDatetime(new Date());
        nma.setNotificationMessageArchLines(newLines);
        return save(nma);
    }

}
