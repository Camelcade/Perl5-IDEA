#!/usr/bin/bash

echo Waiting for $1 seconds to finish...
sleep $1

mapfile -t jvm_pids < <(jps -q)
for pid in "${jvm_pids[@]}"
do
    echo Checking process $pid
    info=$(jinfo $pid);
    if [[ "$info" =~ "idea.config.path" ]]; then
      echo Found stuck process $pid
      echo Writing jinfo.txt
      jinfo $pid > jinfo.txt
      echo Writing jstack.txt
      jstack -l -e $pid >jstack.txt
      echo Writing snapshot.hprof
      jmap -dump:format=b,file=snapshot.hprof $pid
      echo KILLING HANGED PROCESS $pid
      kill -9 $pid
      exit
    fi
done
