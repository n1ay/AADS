CC=gcc
CFLAGS=-Wall -Werror -pedantic -Wno-char-subscripts
OPTFLAG=-O2
DEBUGFLAGS=-g3 -O0

DEPS=bm.h
LIBS=
OBJ=main.o bm.o

main: $(OBJ)
	$(CC) -o $@ $^

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS) $(OPTFLAG)

.PHONY: clean

clean:
	rm -rf *.o
	rm -rf main
