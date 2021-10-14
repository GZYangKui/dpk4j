//
// Created by yangkui on 2021/10/14.
//

#ifndef DKG4J_FLZO_H
#define DKG4J_FLZO_H

#include "lzoconf.h"

/**
 *
 * 压缩数据流
 *
 * @param src 待压缩字符流
 * @return 如果压缩失败返回<code>NULL</code,否者返回压缩后的数据流
 */
extern lzo_bytep compress(lzo_bytep src);

/**
 *
 * 还原被压缩的数据流
 *
 */
extern lzo_bytep decompress(lzo_bytep src);

#endif //DKG4J_FLZO_H
