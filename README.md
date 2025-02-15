# Основной сервис

## Инструкция

Сервис выполнен в виде приложения на Spring Boot, соответственно точка входа в программу метод main класса RtspListenerApplication

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
Example: /start-for-file?path=video.mkv&interval=10&frameCount=5
```

- Запрос на отображение конфигурации

```
GET /config
```

# P.S

Если этот раздел остался в readme, то учтите, что я так и не смог найти нормальный источник rtsp потока чтобы все нормально проверить. Он может и не работать ¯\_(ツ)_/¯ 

