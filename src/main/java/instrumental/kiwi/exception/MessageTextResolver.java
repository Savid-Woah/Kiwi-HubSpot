package instrumental.kiwi.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageTextResolver {

    private final MessageSource messageSource;

    public String getMessage(MsgCode msgCode) {

        return messageSource.getMessage(msgCode.getLanguageKey(), null, LocaleContextHolder.getLocale());
    }
}