package com.github.taoroot.taoiot.netty.tcp.core;

/**
 * @author : zhiyi
 * Date: 2020/2/7
 * <p>
 * 协议处理器
 */
public interface PackageHandler<T extends ProtocolBody> {

    void process(T basePackage);

    @SuppressWarnings("unchecked")
    default void process0(ProtocolBody basePackage) {
        process((T) basePackage);
    }
}
