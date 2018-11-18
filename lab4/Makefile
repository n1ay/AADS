CC=g++
CFLAGS=-Wall -Werror -pedantic -std=c++11
OPTFLAG=-O2
DEBUGFLAGS=-g3 -O0

DEPS=
LIBS=-larmadillo -lpthread
OBJ=main.o

main: $(OBJ)
	$(CC) -o $@ $^ $(LIBS)

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS) $(OPTFLAG)

.PHONY: clean

clean:
	rm -rf *.o
	rm -rf main
