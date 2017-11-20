package project.model.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;
import project.dao.message.MessageValidatorDao;
import project.model.ClientOperation;

import static org.springframework.util.StringUtils.isEmpty;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class MessageValidator implements Validator {

    private static final String NAME = "Сообщение";

    @Autowired
    private ServletWebRequest request;

    @Autowired
    private MessageValidatorDao dao;

    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Message entity = (Message) target;
        ClientOperation operation = ClientOperation.getClientOperation(request);

        ValidationUtils.rejectIfEmpty(errors, "id", "001", new String[]{NAME});
        ValidationUtils.rejectIfEmpty(errors, "text", "007", new String[]{NAME});

        String id = entity.getId();
        String text = entity.getText();

        if (operation == ClientOperation.CREATE) {
            if (!isEmpty(id) && dao.alreadyExists(id)) {
                errors.rejectValue("id", "003", new String[]{NAME}, null);
            }
            if (!isEmpty(text) && dao.sameTextAlreadyExists(null, text)) {
                errors.rejectValue("id", "008", new String[]{NAME}, null);
            }
        }

        if (operation == ClientOperation.UPDATE) {
            if (!isEmpty(id) && !isEmpty(text) && dao.sameTextAlreadyExists(id, text)) {
                errors.rejectValue("id", "008", new String[]{NAME}, null);
            }
        }

        if (operation == ClientOperation.DELETE) {
            ValidationUtils.rejectIfEmpty(errors, "commentary", "005", new String[]{NAME});

            if (!isEmpty(id) && dao.isUsed(id)) {
                errors.rejectValue("id", "006", new String[]{NAME}, null);
            }
        }
    }

}