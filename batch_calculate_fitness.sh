#!/bin/bash
#calculates fitness for all timetables specified line-by-line in a file specified by $1.
sed -i "s/calculateFitnessOnly=.\+/calculateFitnessOnly=true/" parameters.properties

rm -f fitness
number_of_lines=$(wc -l "$1" | cut -f1 -d' ')
counter=0
echo -ne "$counter/$number_of_lines\r"

while IFS= read -r line; do
	sed -i "s/currentTimetable=.\+/currentTimetable=$line/" parameters.properties
	java -jar target/timetable-0.0.1-SNAPSHOT.jar Main >> fitness
	counter=$((counter+1))
	echo -ne "$counter/$number_of_lines\r"
done < "$1"

echo ''
