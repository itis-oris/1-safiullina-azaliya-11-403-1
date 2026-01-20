package ru.itis.dis403.sw_quiz.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.repositories.UserRepository;

import java.util.List;

public class UserService {

    private UserRepository userRepository = new UserRepository();
    private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
    /*
    1.получаем исходный пароль из объекта юзер через getHashPassword()
    2.bCrypt.encode-генерирует случайную соль для этого пароля, хеширует пароль вместе с солью
    3.устанавливаем хешированный пароль обратно в объект
    4.сохраняем пользователя в бд через репозиторий
     */

    public void addUser(User user) throws Exception {
        user.setHashPassword(
                bCrypt.encode(user.getHashPassword()));

        userRepository.addUser(user);
    }

    public boolean registerUser(String username, String password, HttpServletRequest request) throws Exception {
        if (userRepository.userExists(username)) {//проверка на существование пользователя
            return false;
        }
        //создаем объект юзер с его параметрами
        User user = new User();
        user.setUsername(username);
        user.setHashPassword(password);//еще не хеширован
        user.setScore(0);
        user.setRole("USER");

        this.addUser(user);//хеширование пароля и сохранение
        if (request != null) {//автоматический логин после регистрации
            User loggedInUser = loginUser(username, password);
            if (loggedInUser != null) {//пользователь нашелся ли, пароль прошел проверку и объект юзера успешна сохранен
                setSessionUser(request, loggedInUser);
            }
        }
        return true;
    }

    public User loginUser(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);//поиск пользователя в бд
        if (user != null) {
            if (bCrypt.matches(password, user.getHashPassword())) {//сравнение паролей введенного и сохраненного в бд через бкрипт
                return user;//извлекает соль из сохраненного хеша, хеширует введенный пароль с этой же солью и сравнивает их
            }
        }
        return null;
    }

    public boolean authenticateUser(String username, String password, HttpServletRequest request) throws Exception {
        User user = loginUser(username, password);//проверяет пароль через предыдущий метод
        if (user != null) {//аутентификация успешна или нет
            setSessionUser(request, user);//создает сессию
            return true;//аутентификация успешна
        }
        return false;//не успешна
    }

    public void updateUserScore(Integer userId, Integer additionalScore) throws Exception {
        userRepository.updateUserScore(userId, additionalScore);
    }

    public void setSessionUser(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();//если сессия существует-вовзвращает ее, если нет-создает новую, HttpSession имеет уникальный JSESSIONID
        session.setAttribute("user", user);//сохраняет в сессии все параметры объекта юзер
    }

    public User getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (User) session.getAttribute("user");
    }

    public void logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.invalidate();
    }

    public List<User> getTopUsers(int limit) throws Exception {
        return userRepository.getTopUsers(limit);
    }

    public User getUserById(int userId) throws Exception {
        return userRepository.findById(userId);
    }
}
