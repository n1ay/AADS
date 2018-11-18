#!/bin/bash
INPUT='matrices-2.txt'
OUTPUT='data.txt'
sed '/Matrix/d' "$INPUT" | sed 's/\[//g' | sed 's/]]//g' | sed 's/]/;/g' | tr '\r\n' ' ' | sed 's/-------------------------/\n/g' | sed -E 's/ +/ /g' > "$OUTPUT"
