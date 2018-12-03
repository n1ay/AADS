#!/bin/bash
INPUT='matrices-2.txt'
OUTPUT='data.txt'

echo -n '' > $OUTPUT

sed 's/Matrix.*\[//g' matrices-2.txt | sed 's/\r//g' | sed 's/ x /\n/g' | sed 's/\[//g' | sed 's/\]//g' | sed 's/ /\n/g' | sed '/-----/d' | sed '/^[[:space:]]*$/d' >> $OUTPUT
