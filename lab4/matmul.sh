#!/bin/bash

mul_matrix() {
    declare -A m1
    declare -A m2
    declare -A m3

    idx1=0
    for i in $(echo "$1" | tr ' ' '=' | sed 's/;=/ /g')
    do
        jdx1=0
        for j in $(echo "$i" | tr '=' ' ')
        do
            m1[$idx1,$jdx1]="$j"
            ((jdx1++))
        done
        ((idx1++))
    done

    idx2=0
    for i in $(echo "$2" | tr ' ' '=' | sed 's/;=/ /g')
    do
        jdx2=0
        for j in $(echo "$i" | tr '=' ' ')
        do
            m2[$idx2,$jdx2]="$j"
            ((jdx2++))
        done
        ((idx2++))
    done

    for((i=0;i<idx1;i++))
    do
        for((j=0;j<jdx2;j++))
        do
            m3[$i,$j]=0
        done
    done

    for((i=0;i<idx1;i++))
    do
        for((j=0;j<jdx2;j++))
        do
            for((k=0;k<jdx1;k++))
            do
                m3[$i,$j]=$(echo "${m3[$i,$j]}+${m1[$i,$k]}*${m2[$k,$j]}" | bc -sl)
            done
        done
    done

    idx3=$idx2
    jdx3=$jdx1

    out=''

    for((i=0;i<idx1;i++))
    do
        for((j=0;j<jdx2;j++))
        do
            out="$out ${m3[$i,$j]}"
        done
        out="${out};"
    done

    echo ${out:0:$((${#out}-1))}
}

calc_seq() {

    res="$(head -n 1 $1)"
    while IFS='' read -r mat
    do
        if [ "$res" == "$mat" ]
        then
            continue;
        fi
        res=$(mul_matrix "$res" "$mat")
    done < "$1"

    echo $res
}

get_lines() {
    tail -n +$2 "$1" | head -n $(($3-$2+1))
}

seq_worker() {
    touch "lock_$1"
    calc_seq "data_$1" > "output_$1"
    rm "lock_$1"
}

calc_parallel() {
    threads=$(cat /proc/cpuinfo| grep processor | wc -l)
    matrices=$(($(wc -l $1 | awk '{ print $1 }')+1))
    for((i=0; i<threads; i++))
    do
        if [ $i -eq $(($threads-1)) ]
        then
            get_lines $1 $((i*matrices/threads+1)) $matrices > "data_$i"
        else
            get_lines $1 $((i*matrices/threads+1)) $(((i+1)*matrices/threads)) > "data_$i"
        fi
        {
            seq_worker $i
        }&
    done

    sleep 5
    for((i=0; i<threads; i++))
    do
        while [ -f "lock_$i" ]
        do
            sleep 3
        done
        rm "data_$i"
        cat "output_$i" >> output
        rm "output_$i"
    done

    echo $(calc_seq output)
    rm output
}

time echo -e "Sequential: $(calc_seq $1)"
time echo -e "Parallel $(calc_parallel $1)"
