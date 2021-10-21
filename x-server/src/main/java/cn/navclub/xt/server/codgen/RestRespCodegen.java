package cn.navclub.xt.server.codgen;

import cn.navclub.xt.server.api.CommonResult;
import cn.navclub.xt.server.api.RestResponse;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 *
 * Rest response codegen
 *
 */
public class RestRespCodegen implements MessageCodec<CommonResult<?>, CommonResult<?>> {
    @Override
    public void encodeToWire(Buffer buffer, CommonResult commonResult) {
        buffer.appendBuffer(commonResult.toBuffer());
    }

    @Override
    public CommonResult<?> decodeFromWire(int pos, Buffer buffer) {
        return RestResponse.success();
    }

    @Override
    public CommonResult<?> transform(CommonResult commonResult) {
        return commonResult;
    }

    @Override
    public String name() {
        return "CommonResult";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
