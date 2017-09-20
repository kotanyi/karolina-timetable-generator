#!/bin/bash
#for each level, the script runs the java program $1 times. do not forget to set forceLevel to true.
levels=( 'SD_MS_VYUKA' 'SD_MS_TANCOVANIE' 'SD_PLUS_VYUKA' 'SD_PLUS_TANCOVANIE' 'SD_A1_VYUKA' 'SD_A1_TANCOVANIE' )

for level in "${levels[@]}"; do
	sed -i "s/levelToForce=.\+/levelToForce=$level/" parameters.properties
	echo "levelToForce=$level"

	for (( i=1; i<=$1; i++ )); do
	    echo $i
	    java -jar target/timetable-0.0.1-SNAPSHOT.jar Main | tail -1 >> out
	done
done

sort -u out | sort -nr > out_unique
