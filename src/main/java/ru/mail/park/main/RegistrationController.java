package ru.mail.park.main;

//импорты появятся автоматически, если вы выбираете класс из выпадающего списка или же после alt+enter
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

/**
 * Created by Solovyev on 06/09/16.
 */

//Метка по которой спринг находит контроллер
@RestController
public class RegistrationController {


    private final AccountService accountService;


    /**
     * Важное место. Мы не управляем жизненным циклом нашего класса. За нас это делает Spring. Аннотация говорит, что
     * зависимости должны быть разрешены с помощью спрингового контекста{@see ApplicationContext}(реестра классов). В нем могут присутствовать,
     * как наши сервисы(написанные нами), так и сервисы, предоставляемые спрингом.
     * @param accountService - подставляет наш синглтон
     */
    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Я ориентировался на {@see http://docs.technopark.apiary.io/} . В методе что-то сделано сильно не так, как в документации.
     * Что именно? Варианты ответа принимаются в slack {@see https://technopark-mail.slack.com/messages}
     * @param login - реквест параметр
     * @param password - =
     * @param email- =
     * @return - Возвращаем вместо id логин. Но это пока нормально.
     */
    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam(name = "login") String login,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "email") String email) {
        //Инкапсулированная проверка на null и на пустоту. Выглядит гораздо более читаемо
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{Error in parametrs}");
        }
        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{User already exists!}");
        }

        accountService.addUser(login, password, email);
        return ResponseEntity.ok(new SuccessResponse(login));
    }



    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestParam(name = "login") String login,
                                @RequestParam(name = "password") String password) {
        if(StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password) ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{Error in parametrs}");
        }
        final UserProfile user = accountService.getUser(login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{User doesn't exist!}");
        }
        if(user.getPassword().equals(password)) {
            return ResponseEntity.ok(new SuccessResponse(user.getLogin()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{Password is incorrect!}");
    }


    // объект класса будет автоматически преобразован в JSON при записи тела ответа
    private static final class SuccessResponse {
        private final String login;

        private SuccessResponse(String login) {
            this.login = login;
        }

        //Функция необходима для преобразования см  https://en.wikipedia.org/wiki/Plain_Old_Java_Object
        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
    }

}
