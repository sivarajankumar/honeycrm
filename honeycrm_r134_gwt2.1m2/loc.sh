#!/bin/sh
echo
echo "test code only:"
find src/honeycrm/server/test -name "*.java" | xargs wc -l | tail -n 1
echo 
echo "core system (everything except dtos, domain classes, tests):"
find src/ -name "*.java" | grep -v "client/dto" | grep -v "server/domain" | grep -v "server/test" | xargs wc -l | sort -n | tail -n 1
echo
echo "overall lines of code:"
find src/honeycrm/ -name "*.java" | xargs wc -l | sort -n | tail -n 1 
echo
