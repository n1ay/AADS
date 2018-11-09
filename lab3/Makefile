CC=gcc
CFLAGS=-Wall -Werror -pedantic -O2 -Wno-char-subscripts

DEPS=bm.h
LIBS=
OBJ=main.o bm.o

main: $(OBJ)
	$(CC) -o $@ $^

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

.PHONY: clean

clean:
	rm *.o
	rm main
