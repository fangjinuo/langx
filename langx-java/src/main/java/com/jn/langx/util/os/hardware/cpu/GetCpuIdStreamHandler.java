package com.jn.langx.util.os.hardware.cpu;

import com.jn.langx.commandline.AbstractExecuteStreamHandler;

abstract class GetCpuIdStreamHandler extends AbstractExecuteStreamHandler {
    public abstract String getCpuId();
}
