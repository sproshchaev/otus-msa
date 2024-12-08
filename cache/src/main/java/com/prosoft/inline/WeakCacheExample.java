package com.prosoft.inline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Пример использования кэша на основе WeakHashMap.
 * <p>
 * В данном примере используется кэш, который сохраняет данные в WeakHashMap. Это означает, что
 * объекты в кэше могут быть удалены сборщиком мусора, если на них больше не существует сильных ссылок.
 * </p>
 */
public class WeakCacheExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeakCacheExample.class);

    /** Кэш с использованием WeakHashMap */
    private Map<Integer, String> cache = new WeakHashMap<>();

    public static void main(String[] args) {
        WeakCacheExample example = new WeakCacheExample();
        String result = example.getData(1);
        LOGGER.info("Результат: {}\n", result);
        result = example.getData(1);
        LOGGER.info("Результат: {}", result);
    }

    /**
     * Метод getData() получает данные из кэша или базы данных.
     * <p>
     * Если данные с указанным ID уже находятся в кэше, они будут возвращены из кэша.
     * В противном случае выполняется операция получения данных из базы данных,
     * и результат сохраняется в кэше для последующего использования.
     * </p>
     *
     * @param id уникальный идентификатор данных
     * @return строка с данными, соответствующими указанному ID
     */
    public String getData(int id) {
        long startTime = System.currentTimeMillis(); // Засекаем время начала операции
        LOGGER.debug("Запрос данных для ID {}", id);

        if (cache.containsKey(id)) {
            long endTime = System.currentTimeMillis(); // Засекаем время окончания операции
            LOGGER.info("Данные из кэша получены для ID {} за {} мс", id, (endTime - startTime));
            return cache.get(id);
        }

        // Если данных нет в кэше, получаем их из базы данных
        String result = fetchDataFromDatabase(id);
        cache.put(id, result); // Сохраняем данные в кэш
        long endTime = System.currentTimeMillis(); // Засекаем время окончания операции
        LOGGER.info("Данные из базы получены для ID {} за {} мс", id, (endTime - startTime));
        return result;
    }

    /**
     * Метод fetchDataFromDatabase() эмулирует получение данных из базы данных.
     * <p>
     * Метод задерживает выполнение на 1 секунду, чтобы смоделировать задержку,
     * которая может возникать при реальных запросах к внешним источникам данных.
     * </p>
     *
     * @param id уникальный идентификатор данных
     * @return строка с данными, связанными с указанным ID
     */
    private String fetchDataFromDatabase(int id) {
        try {
            // Эмуляция долгой операции
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("Ошибка при задержке выполнения потока для ID {}", id, e);
            Thread.currentThread().interrupt();
        }
        LOGGER.info("Данные успешно получены для ID {}", id);
        return "Data for ID " + id;
    }
}
