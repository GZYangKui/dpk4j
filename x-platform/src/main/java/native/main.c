#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "include/dpk4j/flzo.h"
#include "include/FileArchive.h"

int main(void) {
//    const char *srcPath = "/home/yangkui/study/dkp4j/test.txt";
//    const char *dsPath = "/home/yangkui/study/dkp4j/test.txt.dkp4j";
//    do_compress(srcPath,dsPath);

//    lzo_bytep test = (lzo_bytep) malloc(1024*1024*10);

//    while ((*test) < 2048) {
//        test++;
////        printf("%p\n",test);
//    }
//    printf("%d\n", *(test + 1025));

//    FILE *file = fopen(dsPath, "rb+");
//    if (file == NULL) {
//        printf("打开文件失败!");
//        exit(1);
//    }
//    uint len = 1052692;
//    lzo_bytep byte;
//    lzo_bytep buffer = NULL;
//    lzo_uint buf_len;
//
//    byte = (lzo_bytep)malloc(len);
//    lzo_memset(byte,0,len);
//
//    uint code = fread(byte, sizeof(char), len, file);
//    if (code==0){
//        uint errno = ferror(file);
//        if (errno!=0){
//            printf("文件读取失败:%d\n",errno);
//            exit(1);
//        }
//    }
//
//    for (int i = 0; i <len ; ++i) {
//        if (i%1000 == 0){
//            printf("\n");
//        }
//        printf("%d",*(byte+i));
//    }
//
//
//    uint rs = dkp4j_decompress(byte, len, &buffer, &buf_len);
//
//    if (rs!=C_D_SUCCESS){
//        printf("解码失败:%d\n",rs);
//    }

    lzo_uint out_len = 0;
    lzo_bytep out = NULL;
    u_char in[] = {1, 2, 3, 5, 6, 7, 8};
    dkp4j_compress(in, &out, 7, &out_len);

    for (int i = 0; i <out_len ; ++i) {
        printf("%d",*(out+i));
    }
    lzo_uint rs = dkp4j_decompress(in, out_len, &out, &out_len);

    printf("rs=%lu\n", rs);

}
