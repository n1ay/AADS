#!/bin/bash
INPUT='matrices-2.txt'
OUTPUT='data.txt'
OUTPUT_BASH='data_bash.txt'
sed '/Matrix/d' "$INPUT" | sed 's/\[//g' | sed 's/]]//g' | sed 's/]/;/g' | tr '\r\n' ' ' | sed 's/-------------------------/\n/g' | sed -E 's/ +/ /g' | sed 's/^ //g' | sed 's/ $//g' | sed 's/ ;/;/g' > "$OUTPUT"

sed '/Matrix/d' "$INPUT" | sed 's/\[//g' | sed 's/]]//g' | sed 's/]/;/g' | tr '\r\n' ' ' | sed 's/-------------------------/\n/g' | sed -E 's/ +/ /g' | sed 's/^ //g' | sed 's/ $//g' | sed 's@e-01@/10@g' | sed 's@e-02@/100@g' | sed 's@e-03@/1000@g' | sed 's@e-04@/10000@g' | sed 's@e-05@/100000@g' | sed 's@e-06@/1000000@g' | sed 's@e-07@/10000000@g' | sed 's@e-08@/100000000@g' | sed 's@e-09@/1000000000@g' | sed 's/ ;/;/g'  > "$OUTPUT_BASH"
