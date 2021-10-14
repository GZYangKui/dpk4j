//
// Created by yangkui on 2021/10/14.
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "include/flzo.h"
#include "include/minilzo.h"

size_t cal_out_len(size_t in_len);

extern lzo_bytep compress(lzo_bytep src) {
    lzo_uint out_len;
    size_t in_len = strlen(src);
    size_t t_out_len = cal_out_len(in_len);
    lzo_bytep out = malloc(t_out_len);
    lzo_voidp wrkmem = (lzo_voidp) malloc(LZO1X_1_MEM_COMPRESS);

    memset(out, 0, t_out_len);

    if (out == NULL || wrkmem == NULL) {
        printf("Out of memory\n!");
        return NULL;
    }
    uint rs = lzo1x_1_compress(src, in_len, out, &out_len, wrkmem);
    if (rs == LZO_E_OK) {
        printf("compressed %lu bytes into %lu bytes\n",
               (unsigned long) in_len, (unsigned long) out_len);
    } else {
        /* this should NEVER happen */
        printf("internal error - compression failed: %d\n", rs);
        return NULL;
    }
    if (out_len >= in_len) {
        printf("This block contains incompressible data.\n");
    }
    return out;
}

extern lzo_bytep decompress(lzo_bytep in) {
    lzo_uint in_len = strlen(in);
    size_t out_len = cal_out_len(in_len);
    lzo_bytep out = malloc(out_len);

    lzo_uint new_len = in_len;

    int rs = lzo1x_decompress(in, in_len, out, &new_len, NULL);

    if (rs == LZO_E_OK && new_len == in_len) {
        printf("decompressed %lu bytes back into %lu bytes\n",
               (unsigned long) out_len, (unsigned long) in_len);
        return out;
    } else {
        printf("internal error - decompression failed: %d\n", rs);
        return NULL;
    }
}


size_t cal_out_len(size_t in_len) {
    return in_len + in_len / 16 + 64 + 3;
}