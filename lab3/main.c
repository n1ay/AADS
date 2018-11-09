#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include "bm.h"

#define BUFSIZE 8096

array read_file(char* filename);

int main(int argc, char* argv[]) {


    if (argc != 3) {
        printf("Usage: %s pattern filename\n", argv[0]);
        exit(-1);
    }

    array file_info = read_file(argv[2]);
    array table_info = get_occ_table(file_info.data, file_info.length, argv[1]);

    for(int i = 0; i < table_info.length; i++) {
        printf("%d\n", ((unsigned int*)table_info.data)[i]);
    }

    free(table_info.data);
    free(file_info.data);
    return 0;
}

array read_file(char* filename) {
    int fd = open(filename, O_RDONLY);
    int file_size = 65536;
    char* file_contents = malloc(file_size * sizeof(char));
    int content_size = 0;
    int read_bytes = 0;

    char* buf = malloc(BUFSIZE);

    while((read_bytes = read(fd, buf, BUFSIZE))) {
        memcpy(file_contents + content_size, buf, read_bytes);
        content_size += read_bytes;
        if (content_size + BUFSIZE > file_size) {
            file_size *= 2;
            file_contents = realloc(file_contents, file_size);
        }
    }
    array file_info;
    file_info.data = file_contents;
    file_info.length = content_size;

    free(buf);
    return file_info;
}
