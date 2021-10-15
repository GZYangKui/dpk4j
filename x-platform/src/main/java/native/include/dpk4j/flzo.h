//
// Created by yangkui on 2021/10/14.
//

#ifndef DKG4J_FLZO_H
#define DKG4J_FLZO_H

#include "lzoconf.h"

#define C_D_SUCCESS    0
#define C_D_FAIL      (1)
#define OUT_OF_MEMORY (-1)


/**
 *
 * 压缩数据流
 *
 * @param src 待压缩字符流
 * @return 如果压缩失败返回<code>NULL</code,否者返回压缩后的数据流
 */
extern uint compress(lzo_bytep, lzo_bytep*, lzo_uint, lzo_uint *);

/**
 *
 * 还原被压缩的数据流
 *
 */
extern uint decompress(lzo_bytep, lzo_uint, lzo_bytep*, lzo_uint *);

#endif //DKG4J_FLZO_H
