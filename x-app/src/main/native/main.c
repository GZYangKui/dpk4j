#include <stdlib.h>
#include <stdio.h>
#include "include/FileArchive.h"
int main(void) {
    const char *srcPath = "/home/yangkui/study/dkp4j/README.md";
    const char *dsPath = "/home/yangkui/study/dkp4j/test.txt.dkp4j";
    do_compress(srcPath,dsPath);
}
