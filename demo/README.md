# Мини-примеры к слайдам

Маленькие Dockerfile для демонстрации отдельных тем вебинара.
Каждый собирается за секунды и показывает ровно один эффект.

| Файл | Слайд | Что показывает |
|---|---|---|
| `Dockerfile.layers-bad` | 18, 22 | Отдельный `RUN rm` не уменьшает размер образа |
| `Dockerfile.layers-good` | 18, 22 | Объединение шагов через `&&` в один слой |
| `Dockerfile.cmd` | 20 | CMD по отдельности |
| `Dockerfile.entrypoint` | 20 | ENTRYPOINT по отдельности |
| `Dockerfile.entrypoint-cmd` | 20 | ENTRYPOINT + CMD совместно |
| `Dockerfile.shell` | 21 | Shell-форма записи, два процесса внутри |
| `Dockerfile.exec` | 21 | Exec-форма записи, приложение с PID 1 |
