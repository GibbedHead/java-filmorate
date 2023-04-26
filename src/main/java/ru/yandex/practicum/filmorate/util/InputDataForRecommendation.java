package ru.yandex.practicum.filmorate.util;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

// принимает данные для их инициализации (initializeData)
@Data
public class InputDataForRecommendation {

    //Лист с Фильмами, которым ставили лайки (видимо все фильмы)
    private List<Film> films;

    public static Map<User, HashMap<Film, Double>> initializeData(int numberOfUsers) {

        //Мапа с Лайками - <Film, Like>
        Map<User, HashMap<Film, Double>> data = new HashMap<>();
        HashMap<Film, Double> newUser;
        Set<Film> newRecommendationSet;
        for (int i = 0; i < numberOfUsers; i++) {
            newUser = new HashMap<Film, Double>();
            newRecommendationSet = new HashSet<>();
            for (int j = 0; j < 3; j++) {
                newRecommendationSet.add(films.get((int) (Math.random() * 5)));
            }
            for (Film Film : newRecommendationSet) {
                newUser.put(Film, Math.random());
            }
            data.put(new User("User " + i), newUser);
        }
        return data;
    }
}
