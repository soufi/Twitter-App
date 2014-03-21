#/bin/sh
sort -n $1 > fichier.tmp && mv fichier.tmp $1

awk -F'\t' '{print $2"\t"$1}' $1 | sort -n - > inverse.txt