# otus-msa

**Сергей Прощаев**

[![Email](https://img.shields.io/badge/sproshchaev%40gmail.com-red?logo=gmail&logoColor=white)](mailto:sproshchaev@gmail.com)
[![Website](https://img.shields.io/badge/prosoft.pages.dev-blue?logo=googlechrome&logoColor=white)](https://prosoft.pages.dev)

---

# hello-service

Демонстрационный сервис для вебинара **«Основы работы с Docker»** курса *Microservice Architecture* (OTUS).

Небольшое приложение на **Java 21 / Spring Boot 3.5** с одним REST-контроллером. В самом коде нет ничего, связанного с Docker, — весь смысл вебинара в том, чтобы обычное Spring Boot приложение упаковать в образ и разобрать, как оно живёт внутри контейнера: namespaces, cgroups, слои, сети, тома.

## Содержание

- [Структура проекта](#структура-проекта)
- [Быстрый старт](#быстрый-старт)
  - [Запуск локально](#запуск-локально)
  - [Запуск в контейнере](#запуск-в-контейнере)
- [API сервиса](#api-сервиса)
  - [Эндпоинты](#эндпоинты)
  - [Переменные окружения](#переменные-окружения)
- [Демонстрация на вебинаре](#демонстрация-на-вебинаре)
  - [Подготовка до начала](#подготовка-до-начала)
  - [LIVE 1. Сборка образа и запуск контейнера](#live-1-сборка-образа-и-запуск-контейнера)
  - [LIVE 2. Под капотом](#live-2-под-капотом)
  - [Мини-примеры к слайдам](#мини-примеры-к-слайдам)
  - [Карта соответствия слайдам](#карта-соответствия-слайдам)
  - [Запасной план](#запасной-план)

---

## Структура проекта

```
otus-msa/
├── pom.xml                     # Maven, Spring Boot 3.5, Java 21
├── Dockerfile                  # сборка образа из готового jar
├── Dockerfile.multistage       # сборка jar внутри Docker (multi-stage)
├── .dockerignore
├── demo/                       # мини-Dockerfile'ы к отдельным слайдам
│   ├── README.md
│   ├── Dockerfile.layers-bad   # слои: отдельный RUN rm не уменьшает образ
│   ├── Dockerfile.layers-good  # слои: объединение шагов через &&
│   ├── Dockerfile.cmd          # CMD по отдельности
│   ├── Dockerfile.entrypoint   # ENTRYPOINT по отдельности
│   ├── Dockerfile.entrypoint-cmd  # ENTRYPOINT + CMD совместно
│   ├── Dockerfile.shell        # shell-форма записи, два процесса
│   └── Dockerfile.exec         # exec-форма записи, приложение с PID 1
└── src/main/
    ├── java/ru/otus/hello/
    │   ├── HelloServiceApplication.java
    │   └── HelloController.java
    └── resources/application.yml
```

---

## Быстрый старт

### Запуск локально

```bash
mvn clean package -DskipTests
java -jar target/hello-service.jar
```

Сервис поднимется на `http://localhost:8080`. Папку для файла с сообщениями удобно задать явно:

```bash
DATA_DIR=/tmp/hello-data java -jar target/hello-service.jar
```

### Запуск в контейнере

```bash
mvn clean package -DskipTests
docker build -t hello-service:1.0 .
docker run -d --name hello -p 8080:8080 hello-service:1.0
```

Сборка образа целиком внутри Docker, без локального Maven (multi-stage):

```bash
docker build -f Dockerfile.multistage -t hello-service:full .
```

---

## API сервиса

### Эндпоинты

| Метод | Путь | Назначение | Тема слайда |
|---|---|---|---|
| GET  | `/`                   | проверка, что сервис поднялся      | запуск контейнера |
| GET  | `/hello?name=Otus`    | приветствие, текст из `GREETING`   | ENV |
| GET  | `/info`               | hostname, pid, память, ядра        | namespaces, cgroups |
| POST | `/data?message=...`   | запись строки в файл               | volumes |
| GET  | `/data`               | чтение записанных строк            | volumes, эфемерность |

Примеры:

```bash
curl http://localhost:8080/
curl "http://localhost:8080/hello?name=Otus"
curl http://localhost:8080/info
curl -X POST "http://localhost:8080/data?message=test"
curl http://localhost:8080/data
```

### Переменные окружения

| Переменная    | По умолчанию | Назначение |
|---|---|---|
| `GREETING`    | `Hello`      | текст приветствия в `/hello` |
| `DATA_DIR`    | `/data`      | папка для файла `messages.log` |
| `SERVER_PORT` | `8080`       | порт приложения |

Один образ ведёт себя по-разному в зависимости от переменных:

```bash
docker run -d --name hello-ru -p 8081:8080 -e GREETING="Привет" hello-service:1.0
curl "http://localhost:8081/hello?name=Otus"   # {"message":"Привет, Otus!", ...}
```

---

## Демонстрация на вебинаре

Проект: `hello-service` — Java 21, Spring Boot 3.5, один контроллер.
Демонстрация разбита на два блока LIVE в соответствии с картой вебинара.

### Подготовка до начала

Выполнить заранее, чтобы не тратить эфирное время на скачивание.

```bash
# базовые образы уже должны лежать локально
docker pull eclipse-temurin:21-jre-alpine
docker pull maven:3.9-eclipse-temurin-21
docker pull alpine:3.20

# jar собран
mvn clean package -DskipTests
ls -lh target/hello-service.jar
```

Проверить, что всё чисто:

```bash
docker ps -a
docker images | head
```

### LIVE 1. Сборка образа и запуск контейнера

*Слайд 23.*

#### Шаг 1.1. Показать приложение

```bash
cat src/main/java/ru/otus/hello/HelloController.java
```

Обычное Spring Boot приложение с одним контроллером. Никакого кода, связанного с Docker, внутри нет.

#### Шаг 1.2. Показать Dockerfile

```bash
cat Dockerfile
```

Пройти построчно и связать со слайдом «Команды»:

- `FROM eclipse-temurin:21-jre-alpine` — базовый образ, версия указана явно
- `LABEL` — метаданные образа
- `WORKDIR /app` — рабочая папка внутри образа
- `ENV GREETING="Hello"` — переменная окружения со значением по умолчанию
- `COPY target/hello-service.jar app.jar` — кладём собранный артефакт внутрь
- `EXPOSE 8080` — документация о порте
- `ENTRYPOINT ["java", "-jar", "/app/app.jar"]` — команда запуска в exec-форме

#### Шаг 1.3. Сборка образа

```bash
docker build -t hello-service:1.0 .
```

Обратить внимание в выводе на:
- строки вида `[1/6] FROM ...`, `[2/6] ...` — каждая инструкция выполняется отдельным шагом
- `exporting layers` — фиксация слоёв
- итоговый размер: `docker images hello-service`

```bash
docker images hello-service
```

#### Шаг 1.4. Запуск контейнера

```bash
docker run -d --name hello -p 8080:8080 hello-service:1.0
docker ps
```

Разобрать флаги:
- `-d` — фоновый режим
- `--name hello` — имя контейнера
- `-p 8080:8080` — проброс порта хоста внутрь контейнера

Проверка:

```bash
curl http://localhost:8080/
curl http://localhost:8080/hello?name=Otus
```

#### Шаг 1.5. Логи в STDOUT

*Слайд «Контейнеризация».*

```bash
docker logs hello
docker logs -f hello
```

Приложение пишет логи в консоль, движок их перехватывает. Никакой настройки путей к файлам логов не потребовалось.

#### Шаг 1.6. Переменные окружения (инструкция ENV)

Значение из образа переопределяется при запуске:

```bash
docker run -d --name hello-ru -p 8081:8080 \
  -e GREETING="Привет" \
  hello-service:1.0

curl http://localhost:8081/hello?name=Otus
```

Один образ — два контейнера с разным поведением.

#### Шаг 1.7. Один образ — много контейнеров

```bash
docker run -d --name hello-2 -p 8082:8080 hello-service:1.0
docker run -d --name hello-3 -p 8083:8080 hello-service:1.0
docker ps

curl http://localhost:8082/info
curl http://localhost:8083/info
```

Обратить внимание: `hostname` в ответах разный, а образ один и тот же.

#### Шаг 1.8. Кэш слоёв

*Слайд «Docker-образ».*

Повторная сборка без изменений:

```bash
docker build -t hello-service:1.0 .
```

В выводе — `CACHED` почти на всех шагах, сборка занимает доли секунды.

Теперь меняем код и пересобираем:

```bash
# поменять текст в HelloController.java, например "is running" -> "is running v2"
mvn clean package -DskipTests
docker build -t hello-service:1.1 .
```

Слои до `COPY` взяты из кэша, пересобрано только то, что после него.

#### Шаг 1.9. История слоёв

```bash
docker history hello-service:1.1
```

Показать: каждая инструкция Dockerfile превратилась в строку. У инструкций `ENV`, `LABEL`, `EXPOSE` размер 0 B, у `COPY` — вес jar.

#### Шаг 1.10. Сборка внутри Docker (multi-stage)

```bash
cat Dockerfile.multistage
docker build -f Dockerfile.multistage -t hello-service:full .
docker images | grep hello-service
```

Maven на машине больше не требуется, сборка идёт внутри контейнера. Итоговый образ содержит только JRE и jar, инструменты сборки в него не попали.

#### Шаг 1.11. Реестр

*Слайд «Docker-registry».*

```bash
# тег под конкретный реестр
docker tag hello-service:1.1 <логин>/hello-service:1.1

# публикация
docker login
docker push <логин>/hello-service:1.1
```

Показать образ на Docker Hub в браузере. Далее любая машина поднимет сервис одной командой:

```bash
docker run -d -p 8080:8080 <логин>/hello-service:1.1
```

#### Шаг 1.12. Уборка перед вторым блоком

```bash
docker rm -f hello hello-ru hello-2 hello-3
```

### LIVE 2. Под капотом

*Слайд 35.*

#### Шаг 2.1. CMD и ENTRYPOINT

*Слайд 20.*

```bash
cd demo

# только CMD
docker build -f Dockerfile.cmd -t demo-cmd .
docker run --rm demo-cmd

# CMD переопределяется аргументом команды запуска
docker run --rm demo-cmd echo "аргумент заменил CMD"

# только ENTRYPOINT — ведёт себя так же
docker build -f Dockerfile.entrypoint -t demo-entrypoint .
docker run --rm demo-entrypoint

# ENTRYPOINT + CMD
docker build -f Dockerfile.entrypoint-cmd -t demo-both .
docker run --rm demo-both          # выполнится: ls /usr /var
docker run --rm demo-both /etc     # выполнится: ls /etc
```

Вывод: аргумент из CMD заменяется свободно, программа из ENTRYPOINT остаётся.

#### Шаг 2.2. Shell и exec формы

*Слайд 21.*

```bash
# shell-форма
docker build -f Dockerfile.shell -t demo-shell .
docker run -d --name sh-form demo-shell
docker exec sh-form ps -ef
```

В списке два процесса: `/bin/sh` с PID 1 и `ping` как дочерний.

```bash
# exec-форма
docker build -f Dockerfile.exec -t demo-exec .
docker run -d --name exec-form demo-exec
docker exec exec-form ps -ef
```

В списке один процесс: `ping` сразу с PID 1.

Проверка остановки:

```bash
time docker stop sh-form     # ждёт таймаут, затем принудительное завершение
time docker stop exec-form   # останавливается сразу

docker rm sh-form exec-form
cd ..
```

#### Шаг 2.3. Namespaces: PID

*Слайд 27.*

```bash
docker run -d --name hello -p 8080:8080 hello-service:1.1
docker exec hello ps -ef
```

Внутри контейнера — короткий список, процесс `java` с PID 1.

```bash
curl http://localhost:8080/info
```

Поле `pid` в ответе равно 1: приложение считает себя первым процессом системы.

Тот же процесс со стороны хоста:

```bash
ps -ef | grep hello-service
```

Процесс один и тот же, номер снаружи другой.

#### Шаг 2.4. Namespaces: UTS (hostname)

```bash
docker exec hello hostname
hostname
```

У контейнера собственное имя машины. Оно же приходит в поле `hostname` ответа `/info`.

#### Шаг 2.5. Namespaces: Mnt (файловая система)

```bash
docker exec hello ls /
docker exec hello ls /app
```

Внутри — содержимое образа, файлы хост-машины недоступны.

#### Шаг 2.6. Namespaces: Net (сеть)

```bash
docker exec hello ip addr
ip addr
```

У контейнера собственный интерфейс `eth0` со своим адресом из диапазона 172.17.

Порт внутри контейнера и порт на хосте независимы:

```bash
docker run -d --name hello-b hello-service:1.1
docker exec hello-b netstat -tlnp 2>/dev/null || docker exec hello-b ss -tlnp
```

Оба контейнера слушают 8080 внутри себя и друг другу не мешают.

#### Шаг 2.7. Cgroups: ограничение памяти

*Слайд 28.*

```bash
docker run -d --name hello-limit -p 8090:8080 \
  --memory=256m --cpus=1 \
  hello-service:1.1

curl http://localhost:8090/info
```

Сравнить с ответом от контейнера без ограничений:

```bash
curl http://localhost:8080/info
```

Поля `maxMemoryMb` и `availableProcessors` отличаются: JVM внутри контейнера видит квоту, выданную cgroups, и настраивает размер кучи по ней.

Потребление в реальном времени:

```bash
docker stats --no-stream
```

Колонка `MEM USAGE / LIMIT` показывает заданный предел.

#### Шаг 2.8. UnionFS: слои и эфемерность

*Слайды 29–31.*

Смотрим слои образа:

```bash
docker image inspect hello-service:1.1 --format '{{json .RootFS.Layers}}' | tr ',' '\n'
```

Создаём файл внутри работающего контейнера:

```bash
docker exec hello sh -c "echo test > /tmp/created-inside.txt"
docker exec hello ls -l /tmp/created-inside.txt
```

Смотрим, что изменилось в слое контейнера:

```bash
docker diff hello
```

Символ `A` означает добавленный файл, `C` — изменённый. Это содержимое того самого тонкого слоя записи.

Проверяем эфемерность:

```bash
docker rm -f hello
docker run -d --name hello -p 8080:8080 hello-service:1.1
docker exec hello ls /tmp/created-inside.txt   # файла больше нет
```

Слои образа остались нетронутыми, слой контейнера исчез вместе с контейнером.

#### Шаг 2.9. Сеть: мосты

*Слайд 32.*

Смотрим сети по умолчанию:

```bash
docker network ls
docker network inspect bridge --format '{{json .Containers}}'
```

Создаём собственную сеть и подключаем к ней два контейнера:

```bash
docker network create my_bridge

docker run -d --name svc-a --network my_bridge hello-service:1.1
docker run -d --name svc-b --network my_bridge hello-service:1.1
```

Проверяем связность по имени контейнера:

```bash
docker exec svc-a wget -qO- http://svc-b:8080/hello?name=svc-a
```

Внутри своей сети контейнеры находят друг друга по имени.

Проверяем изоляцию сетей:

```bash
docker run -d --name svc-c hello-service:1.1     # сеть по умолчанию
docker exec svc-c wget -qO- --timeout=3 http://svc-b:8080/ || echo "недоступен — сети изолированы"
```

#### Шаг 2.10. Volumes: bind mount

*Слайд 34.*

```bash
mkdir -p ~/docker-demo-data

docker run -d --name vol-bind -p 8091:8080 \
  -v ~/docker-demo-data:/data \
  hello-service:1.1

curl -X POST "http://localhost:8091/data?message=first"
curl -X POST "http://localhost:8091/data?message=second"
curl http://localhost:8091/data
```

Файл виден на хост-машине:

```bash
cat ~/docker-demo-data/messages.log
```

#### Шаг 2.11. Volumes: именованный том

```bash
docker volume create hello-data
docker volume ls

docker run -d --name vol-named -p 8092:8080 \
  -v hello-data:/data \
  hello-service:1.1

curl -X POST "http://localhost:8092/data?message=persistent"
curl http://localhost:8092/data
```

Удаляем контейнер и поднимаем заново с тем же томом:

```bash
docker rm -f vol-named

docker run -d --name vol-named-2 -p 8092:8080 \
  -v hello-data:/data \
  hello-service:1.1

curl http://localhost:8092/data
```

Данные на месте: том пережил удаление контейнера.

Для сравнения — контейнер без тома:

```bash
docker run -d --name no-vol -p 8093:8080 hello-service:1.1
curl -X POST "http://localhost:8093/data?message=lost"
curl http://localhost:8093/data

docker rm -f no-vol
docker run -d --name no-vol -p 8093:8080 hello-service:1.1
curl http://localhost:8093/data     # пусто
```

#### Шаг 2.12. Volumes: tmpfs

```bash
docker run -d --name vol-tmpfs -p 8094:8080 \
  --tmpfs /data \
  hello-service:1.1

curl -X POST "http://localhost:8094/data?message=in-memory"
curl http://localhost:8094/data
docker exec vol-tmpfs df -h /data
```

В выводе `df` видно, что `/data` смонтирован в памяти.

#### Шаг 2.13. Уборка

```bash
docker rm -f $(docker ps -aq)
docker volume rm hello-data
docker network rm my_bridge
rm -rf ~/docker-demo-data
```

### Мини-примеры к слайдам

Маленькие Dockerfile в папке [`demo/`](demo/) для демонстрации отдельных тем вебинара. Каждый собирается за секунды и показывает ровно один эффект.

| Файл | Слайд | Что показывает |
|---|---|---|
| `Dockerfile.layers-bad`    | 18, 22 | Отдельный `RUN rm` не уменьшает размер образа |
| `Dockerfile.layers-good`   | 18, 22 | Объединение шагов через `&&` в один слой |
| `Dockerfile.cmd`           | 20 | CMD по отдельности |
| `Dockerfile.entrypoint`    | 20 | ENTRYPOINT по отдельности |
| `Dockerfile.entrypoint-cmd`| 20 | ENTRYPOINT + CMD совместно |
| `Dockerfile.shell`         | 21 | Shell-форма записи, два процесса внутри |
| `Dockerfile.exec`          | 21 | Exec-форма записи, приложение с PID 1 |

### Карта соответствия слайдам

| Слайд | Тема | Шаги демонстрации |
|---|---|---|
| 11 | Как это работает | 1.3, 1.4, 1.11 |
| 12 | Контейнеризация | 1.5, 2.8 |
| 14 | Docker-registry | 1.11 |
| 17–18 | Dockerfile | 1.2, 1.8, 1.10 |
| 19 | Команды | 1.2, 1.6 |
| 20 | CMD vs ENTRYPOINT | 2.1 |
| 21 | Shell vs EXEC | 2.2 |
| 22 | Docker-образ | 1.8, 1.9 |
| 27 | Namespaces | 2.3, 2.4, 2.5, 2.6 |
| 28 | Cgroups | 2.7 |
| 29–31 | UnionFS | 1.9, 2.8 |
| 32–33 | Docker network | 2.6, 2.9 |
| 34 | Docker volumes | 2.10, 2.11, 2.12 |

