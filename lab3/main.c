#include <stdio.h>
#include "bm.h"

int main(int argc, char* argv[]) {


    if (!((argc == 3) || ((argc == 4) && ((!strcmp(argv[3], "-p")) || (!strcmp(argv[3], "--pos")))))) {
        printf("Usage: %s pattern filename [-p/--pos]\n", argv[0]);
        exit(-1);
    }

    int fd = open(argv[2], O_RDONLY);


    if ((argc == 4) && ((!strcmp(argv[3], "-p")) || (!strcmp(argv[3], "--pos")))) {
        array table_info = get_occ_table(fd, argv[1]);
        for(int i = 0; i < table_info.length; i++) {
            printf("%ld\n", ((unsigned long*)table_info.data)[i]);
        }

        free(table_info.data);

    } else {
        printf("matches: %ld\n", count_occurences(fd, argv[1]));
    }

    close(fd);
    return 0;
}
