CC=gcc
CFLAGS=-Wall -Werror -pedantic -I/home/kamil/repos/libxsmm/include $(OPTFLAG)
OPTFLAG=-Ofast -march=native
DEBUGFLAGS=-g3 -O0

STATIC_DEPS=/home/kamil/repos/libxsmm/lib/libxsmm.a /home/kamil/repos/libxsmm/lib/libxsmmnoblas.a
DEPS=
LIBS=-lpthread -lm -ldl
OBJ=main.o

main: $(OBJ) $(STATIC_DEPS)
	$(CC) -o $@ $^ $(LIBS)

%.o: %.cpp $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

.PHONY: clean

clean:
	rm -rf *.o
	rm -rf main
