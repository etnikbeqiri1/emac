package services;

import models.AuditItem;
import org.apache.commons.lang3.StringUtils;
import repositories.AuditRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;

public class AuditService {

    private AuditRepository auditRepository;

    public AuditService() {
        this.auditRepository = new AuditRepository();
    }

    public List<String> getAuditItems() {
        List<String> audits = new ArrayList<>();
        List<AuditItem> auditList = auditRepository.getAudits();

        auditList.forEach(item -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(item.getUser().getUsername());
            stringBuilder.append(SPACE);
            stringBuilder.append(item.getAction().toString());
            stringBuilder.append(SPACE);

            if (item.getAction().equals(AuditItem.AuditAction.CREATED)) {
                stringBuilder.append("[").append(item.getAfterContent()).append("]");
                stringBuilder.append(SPACE);
            }

            if (item.getAction().equals(AuditItem.AuditAction.UPDATED)) {
                stringBuilder.append("[").append(item.getBeforeContent()).append("]");
                stringBuilder.append(SPACE);
                stringBuilder.append("to");
                stringBuilder.append(SPACE);
                stringBuilder.append("[").append(item.getAfterContent()).append("]");
                stringBuilder.append(SPACE);
            }

            if (item.getAction().equals(AuditItem.AuditAction.DELETED)) {
                stringBuilder.append("[").append(item.getBeforeContent()).append("]");
                stringBuilder.append(SPACE);
            }
            stringBuilder.append("at");
            stringBuilder.append(SPACE);
            stringBuilder.append(item.getLocalDateTime());

            audits.add(stringBuilder.toString());
        });

        return audits;
    }
}
