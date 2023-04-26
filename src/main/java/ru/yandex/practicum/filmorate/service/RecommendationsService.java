package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.InputDataForRecommendation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationsService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private static Map<Film, Map<Film, Double>> diff = new HashMap<>();
    private static Map<Film, Map<Film, Integer>> freq = new HashMap<>();
    private static Map<User, HashMap<Film, Double>> inputData;
    private static Map<User, HashMap<Film, Double>> outputData = new HashMap<>();

    public List<Film> getFilmRecommendations(long userId) throws UserNotFoundException {
        User checkUser = userStorage.findById(userId);
        inputData = InputDataForRecommendation.initializeData(numberOfUsers);
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
        System.out.println("\nSlope One - With Predictions\n");
        predict(inputData);
        return null;
    }


    private static void buildDifferencesMatrix(Map<User, HashMap<Film, Double>> data) {
        for (HashMap<Film, Double> user : data.values()) {
            //Рассчитываются взаимосвязь между элементами, а также число вхождений элементов.
            //Для каждого пользователя мы проверяем его лайки фильмам   .
            for (Map.Entry<Film, Double> e : user.entrySet()) {

                //Проверяем, существует ли данный элемент в наших матрицах.
                //Если это первое появление, мы создаем новую запись в мапах
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Film, Double>());
                    freq.put(e.getKey(), new HashMap<Film, Integer>());
                }
                //сравнение лайков по всем фильмам
                for (Map.Entry<Film, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }

        //вычисляем показатели сходства внутри матриц
        for (Film j : diff.keySet()) {
            for (Film i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data);
    }

    private static void predict(Map<User, HashMap<Film, Double>> data) {
        HashMap<Film, Double> uPred = new HashMap<Film, Double>();
        HashMap<Film, Integer> uFreq = new HashMap<Film, Integer>();
        for (Film j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        //Спрогнозируем все недостающие лайки на основе существующих данных.
        // Для этого нам нужно сравнить лайки <User, Like> с матрицей различий,
        // рассчитанной в buildDifferencesMatrix
        for (Map.Entry<User, HashMap<Film, Double>> e : data.entrySet()) {
            for (Film j : e.getValue().keySet()) {
                for (Film k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Film, Double> clean = new HashMap<Film, Double>();
            for (Film j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (Film j : InputData.films) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }

        //Хитрость, которую следует учитывать при работе с большим набором данных, состоит в том,
        // чтобы использовать только те записи элементов, которые имеют большое значение частоты (например, > 1).
        // Если прогноз невозможен, его значение будет равно -1.
        printData(outputData);
    }

    private static void printData(Map<User, HashMap<Film, Double>> data) {
        for (User user : data.keySet()) {
            System.out.println(user.getName() + ":");
            print(data.get(user));
        }
    }

    private static void print(HashMap<Film, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (Film j : hashMap.keySet()) {
            System.out.println(" " + j.getName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

}
