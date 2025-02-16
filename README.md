# Основной сервис

## Инструкция

Сервис выполнен в виде приложения на Spring Boot, соответственно точка входа в программу метод main класса RtspListenerApplication

Команда для запуска из корня проекта:
```
./gradlew bootRun
```
### Конфиг:

В файл application.properties необходимо внести URL второго сервиса:

```
secondaryServiceUrl=YOUR_URL_TO_SECOND_SERVICE
```


### API:

Реализовано два (три) типа запросов для пользователя:

- Запрос на обработку rtsp потока

```
GET /start; params: rtspUrl, interval [ms], frameCount 
Example: GET /start?rtspUrl=rtsp://localhost/&interval=10&frameCount=5
``` 

- Запрос на обработку файла

```
GET /start-for-file; params: path, interval [ms], frameCount
Example: GET /start-for-file?path=video.mkv&interval=10&frameCount=5
```

- Запрос на отображение конфигурации

```
GET /config
```
## Использованные технологии

JavaCV и FFMPEG для обработки видеофайлов

Xuggler для обработки rtsp потока

Spring Boot для межсервисного взаимодействия

Работоспособность тестировалась на JDK Eclipse Temurin 17, файл video.mkv (в корне проекта)

Работоспособность rtsp функционала тестировалась на локальном rtsp потоке через vlc




