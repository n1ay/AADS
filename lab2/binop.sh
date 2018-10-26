case $1 in
    save) perl -lpe '$_=pack"B*",$_' bin.txt > enc.bin ;;
    read) perl -lpe '$_=unpack"B*"' enc.bin > bin.txt ;;
    ls) ls -l $2 | awk '{ print $5 }' ;;
    *)
esac
