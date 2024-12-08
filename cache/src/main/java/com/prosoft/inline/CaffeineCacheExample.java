package com.prosoft.inline;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Использование Caffeine для inline cache (кэширование результатов)
 */
public class CaffeineCacheExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheExample.class);

    /** Время жизни записи в Cache */
    private static final int CACHE_EXPIRATION_MINUTES = 10;
    /** Максимальное количество записей в Cache */
    private static final int CACHE_MAXIMUM_SIZE = 100;

    /** Инициализация кэша с Caffeine в поле класса */
    private final Cache<Integer, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
            .maximumSize(CACHE_MAXIMUM_SIZE)
            .build();

    public static void main(String[] args) {
        CaffeineCacheExample example = new CaffeineCacheExample();
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
            String result = cache.get(id, this::fetchDataFromDatabase);
            long endTime = System.currentTimeMillis();
            LOGGER.info("Данные получены для ID {} за {} мс", id, (endTime - startTime));
            return result;
        } catch (Exception e) {
            LOGGER.error("Ошибка при получении данных для ID {}", id, e);
            return null;
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
        LOGGER.debug("Данные отсутствуют в кэше. Выполняется загрузка из базы данных для ID {}", id);
        try {
            Thread.sleep(1000); // Эмуляция долгой операции
        } catch (InterruptedException e) {
            LOGGER.error("Операция была прервана для ID {}", id, e);
            Thread.currentThread().interrupt();
        }
        String result = "Data for ID " + id;
        LOGGER.debug("Данные успешно загружены для ID {}: {}", id, result);
        return result;
    }
}
