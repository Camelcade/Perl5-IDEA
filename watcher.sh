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
      jinfo $pid
      echo Writing jstack.txt
      jstack -l -e $pid >jstack.txt
      jstack -l -e $pid
      echo Writing snapshot.hprof
      jmap -dump:format=b,file=snapshot.hprof $pid
      echo KILLING HANGED PROCESS $pid
      kill -9 $pid
      # Wait briefly and verify termination
      sleep 1
      if kill -0 $pid 2>/dev/null; then
       echo "Process $pid still alive after SIGKILL, this shouldn't happen"
       # Log this unusual situation for investigation
       ps -p $pid -o pid,ppid,user,stat,pcpu,comm
      fi
    fi
done
