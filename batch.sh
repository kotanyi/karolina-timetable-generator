#!/bin/bash
#run the java program $1 times, removing duplicit timetables and sorting them by fitness in descending order afterwards

for (( i=1; i<=$1; i++ )); do
    echo $i
    java -jar target/timetable-0.0.1-SNAPSHOT.jar Main | tail -1 >> out
done

sort -u out | sort -nr > out_unique
