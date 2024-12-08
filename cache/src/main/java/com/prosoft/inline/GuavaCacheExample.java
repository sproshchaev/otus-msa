package com.prosoft.inline;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Пример использования Guava Cache для inline cache (кэширование результатов).
 * <p>
 * В данном примере используется Guava Cache для кэширования данных, с ограничением по максимальному
 * количеству записей и временем жизни записи. Если данные отсутствуют в кэше, они извлекаются из
 * базы данных (или эмулируются с помощью метода) и сохраняются в кэш.
 * </p>
 */
public class GuavaCacheExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheExample.class);

    /** Инициализация кэша с Caffeine в поле класса */
    private final Cache<Integer, String> cache = CacheBuilder.newBuilder()
            .maximumSize(100) // Максимальное количество записей в кэше
            .expireAfterWrite(10, TimeUnit.MINUTES) // Время жизни записи
            .build();

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
        try {
            // Используем Guava Cache для извлечения или загрузки данных
            return cache.get(id, () -> {
                LOGGER.debug("Данные отсутствуют в кэше. Выполняется загрузка для ID {}", id);
                return fetchDataFromDatabase(id);
            });
        } catch (ExecutionException e) {
            LOGGER.error("Ошибка при загрузке данных для ID {}", id, e);
            return null;
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Обработка запроса для ID {} завершена за {} мс", id, (endTime - startTime));
        }
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
            Thread.sleep(1000); // Эмуляция задержки
        } catch (InterruptedException e) {
            LOGGER.error("Операция была прервана", e);
            Thread.currentThread().interrupt();
        }
        return "Data for ID " + id;
    }
}
