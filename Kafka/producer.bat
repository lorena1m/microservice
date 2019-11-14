setlocal
set KAFKA_TOPIC=%1

if defined KAFKA_TOPIC (
    D:\kafka_2.11-1.0.0\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic %KAFKA_TOPIC%
) else (
    echo Please pass the topic name as argument.
)
endlocal