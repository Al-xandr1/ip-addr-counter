# Тестовое задание ip-addr-counter

Скрипт генерации тестовых файлов gen.sh:

    time ./gen.sh 1 255 255 255 5 ips_rpt.txt   #End: 16581375 uniq ips, 22.803 sec
    cat ips_rpt.txt | time shuf > ips_shuf.txt

Проверка на https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip показала, что:
* Реализация требует дополнительно до 2.4*size(inputFile) Gb места жёсткого диска для хранения временных файлов
* Время работы: 
    
        End. 2G file processed: 151000000 uniq ips, 237.414 sec. For 120G ~ 14244.84 sec.
        
Примечание: алгоритм подходит для подсчёта уникальных строк ЛЮбЫХ, не только IP адресов. 