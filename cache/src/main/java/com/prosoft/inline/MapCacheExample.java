package com.prosoft.inline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Использование Map для inline cache (кеширование результатов)
 *  <p>
 *  В этом примере данные могут быть извлечены либо из кэша, либо, если их нет в кэше, они будут загружены
 *  из базы данных (или эмулированы) и сохранены в кэше для последующего использования.
 *  Для отслеживания времени обработки запросов используется логирование.
 *  </p>
 */
public class MapCacheExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapCacheExample.class);
    private Map<Integer, String> cache = new HashMap<>();

    public static void main(String[] args) {
        MapCacheExample example = new MapCacheExample();
        String result = example.getData(1);
        LOGGER.info("Результат: {}\n", result);
        result = example.getData(1);
        LOGGER.info("Результат: {}", result);
    }

    /**
     * Метод getData() получает данные из базы данных или кэша.
     * <p>
     * Если данные для указанного ID уже находятся в кэше, они будут возвращены из кэша.
     * В противном случае выполняется операция получения данных из базы,
     * результат которой сохраняется в кэше для последующего использования.
     * </p>
     *
     * @param id уникальный идентификатор данных, которые нужно получить
     * @return строка с данными, соответствующими указанному ID
     */
    public String getData(int id) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Начало запроса данных для ID {}", id);
        if (cache.containsKey(id)) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Данные из кэша получены для ID {} за {} мс", id, (endTime - startTime));
            return cache.get(id);
        }
        String result = fetchDataFromDatabase(id);
        cache.put(id, result);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Данные из базы получены для ID {} за {} мс", id, (endTime - startTime));
        return result;
    }

    /**
     * Метод fetchDataFromDatabase() эмулирует долгую операцию получения данных из базы данных.
     * <p>
     * Метод задерживает выполнение потока на 1 секунду, чтобы
     * смоделировать задержку при обращении к внешнему хранилищу данных,
     * и возвращает строку с данными, соответствующими указанному ID.
     * </p>
     *
     * @param id уникальный идентификатор данных, которые нужно получить
     * @return строка с данными, связанными с указанным ID
     */
    private String fetchDataFromDatabase(int id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("Операция была прервана", e);
            Thread.currentThread().interrupt();
        }
        return "Data for ID " + id;
    }
}