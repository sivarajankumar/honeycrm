#!/bin/sh
cd src/crm/ && find . -name "*.java" | xargs wc -l | sort -n
