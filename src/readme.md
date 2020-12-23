Скрипт генерации тестовых файлов gen.sh:

    time ./gen.sh 1 255 255 255 5 ips_rpt.txt   #End: 16581375 uniq ips, 22.803 sec
    cat ips_rpt.txt | time shuf > ips_shuf.txt
