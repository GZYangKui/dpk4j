//
// Created by yangkui on 2021/10/14.
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "../include/dpk4j/flzo.h"
#include "../include/dpk4j/minilzo.h"

size_t cal_out_len(size_t in_len);

extern size_t compress(lzo_bytep src, lzo_bytep*out, lzo_uint in_len, lzo_uint *out_len) {
    size_t t_out_len = cal_out_len(in_len);
    *out = (lzo_bytep) malloc(t_out_len);
    lzo_voidp wrkmem = (lzo_voidp) malloc(LZO1X_1_MEM_COMPRESS);

    memset(*out, 0, t_out_len);

    if (*out == NULL || wrkmem == NULL) {
        printf("Out of memory\n!");
        return OUT_OF_MEMORY;
    }
    uint rs = lzo1x_1_compress(src, in_len, *out, out_len, wrkmem);
    if (rs == LZO_E_OK) {
        printf("compressed %lu bytes into %lu bytes\n",
               (unsigned long) in_len, (unsigned long) *(out_len));
    } else {
        /* this should NEVER happen */
        printf("internal error - compression failed: %d\n", rs);
        return C_D_FAIL;
    }
    if ((size_t) out_len >= in_len) {
        printf("This block contains incompressible data.\n");
    }
    return C_D_SUCCESS;
}

extern size_t decompress(lzo_bytep in, lzo_uint in_len, lzo_bytep*out, lzo_uint *out_len) {
    *out = malloc(cal_out_len(in_len));

    if (*out == NULL) {
        printf("Out of memory!");
        return OUT_OF_MEMORY;
    }

    uint rs = lzo1x_decompress(in, in_len, *out, out_len, NULL);

    if (rs == LZO_E_OK) {
        printf("decompressed %lu bytes back into %lu bytes\n",
               (unsigned long) in_len, (unsigned long) *out_len);
    } else {
        printf("internal error - decompression failed: %d\n", rs);
    }
    return rs == LZO_E_OK ? C_D_SUCCESS : C_D_FAIL;
}


size_t cal_out_len(size_t in_len) {
    return in_len + in_len / 16 + 64 + 3;
}