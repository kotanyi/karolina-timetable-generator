#!/bin/bash
#the script iterates over all levels one by one, setting each as levelsToForce and running the java program $1 times.
levels=( 'SD_MS_VYUKA' 'SD_MS_TANCOVANIE' 'SD_PLUS_VYUKA' 'SD_PLUS_TANCOVANIE' 'SD_A1_VYUKA' 'SD_A1_TANCOVANIE' )
sed -i "s/forceLevels=.\+/forceLevels=true/" parameters.properties

for level in "${levels[@]}"; do
	sed -i "s/levelsToForce=.\+/levelsToForce=$level/" parameters.properties
	echo "levelsToForce=$level"

	for (( i=1; i<=$1; i++ )); do
	    echo $i
	    java -jar target/timetable-0.0.1-SNAPSHOT.jar Main | tail -1 >> out
	done
done

sort -u out | sort -nr > out_unique
