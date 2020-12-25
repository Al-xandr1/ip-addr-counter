# Тестовое задание ip-addr-counter

Т.к. на моей машине мало места для ip_addresses.zip, сперва сделал скрипт генерации тестовых файлов gen.sh:

    time ./gen.sh 1 255 255 255 5 ips_rpt.txt   #End: 16581375 uniq ips, 22.803 sec
    cat ips_rpt.txt | time shuf > ips_shuf.txt

После проводилось тестирование на куске данных из ip_addresses.zip.

## Первая реализация (v1)
Решение задчи "в лоб", при предположении, что строки произвольные. Т.е. алгоритм подходит для подсчёта уникальных строк ЛЮбЫХ, не только IP адресов.
Запуск из коммандной строки:
    
    java -Xmx2g -cp $classpath:./ip-addr-counter-1.0-SNAPSHOT.jar v1.MainV1Kt $ips_file
     
Проверка на 2G куске от https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip показала, что:
* Реализация требует дополнительно до 2.4*size(inputFile) Gb места жёсткого диска для хранения временных файлов
* Время работы: 
    
        End. 2G file processed: 151000000 unique ips, 237.414 sec. For 120G ~ 14244.84 sec.
        

## Вторая реализация (v2)
Т.к. первая реализация показывала неудовлетворительные результаты, пришлось придумывать другую, в которой алгоритм предполагает
что строки являются IP адресами и только. Прочие строки только выводятся в лог.
Запуск из коммандной строки:
    
    java -Xmx2g -cp $classpath:./ip-addr-counter-1.0-SNAPSHOT.jar v2.MainV2Kt $ips_file
    
* Требований к потребляемой памяти жёсткого дистка нет; 
* Требования по ОЗУ - 2G, max по jmc - 1.42G; 
* Время работы:

        End. 9.97265625G file processed: 750000000 unique ips, 89.156 sec. For 120G ~ 1072.8054524089307 sec.
        
        
## Моменты к проработке
Ниже перечислены те пункты, которые, могут ускорить и уменьшить потребление памяти, но т.к. срок вышел - просто зафиксирую их:
* v2/MainV2.kt:30
* v2/IP.kt:26
* v2/BigBitSet.kt:10
* v2/BigBitSet.kt:23
