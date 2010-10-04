#!/bin/sh
for i in `ls *.png`; do
	echo "resize $i"
	convert -resize 70% $i $i;
done
