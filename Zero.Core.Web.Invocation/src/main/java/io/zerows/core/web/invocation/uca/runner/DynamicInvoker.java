package io.zerows.core.web.invocation.uca.runner;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.zerows.core.web.model.commune.Envelop;

import java.lang.reflect.Method;

public class DynamicInvoker extends AbstractInvoker {

    @Override
    public void ensure(final Class<?> returnType, final Class<?> paramCls) {
        // Verify
        final boolean valid =
            (void.class != returnType && Void.class != returnType);
        InvokerUtil.verify(!valid, returnType, paramCls, this.getClass());
    }

    @Override
    public void invoke(final Object proxy,
                       final Method method,
                       final Message<Envelop> message) {
        final Envelop envelop = message.body();


        // LOG
        this.logger().info(
            INFO.MSG_DIRECT,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );


        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        // The returnValue type could not be Future
        message.reply(Envelop.success(returnValue));
    }

    @Override
    @SuppressWarnings("all")
    public <I, O> void handle(final Object proxy, final Method method, final I input, final Handler<AsyncResult<O>> handler) {
        final Envelop envelop = this.invokeWrap(input);


        // LOG
        this.logger().info(
            INFO.MSG_HANDLE,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );


        final Object resultValue = this.invokeInternal(proxy, method, envelop);
        // The returnValue type could not be Future
        handler.handle(Future.succeededFuture((O) resultValue));
    }

    @Override
    public void next(final Object proxy,
                     final Method method,
                     final Message<Envelop> message,
                     final Vertx vertx) {
        final Envelop envelop = message.body();


        // LOG
        this.logger().info(
            INFO.MSG_RPC,
            this.getClass(),
            method.getReturnType(),
            method.getName(),
            method.getDeclaringClass()
        );


        final Object returnValue = this.invokeInternal(proxy, method, envelop);
        this.nextEnvelop(vertx, method, Envelop.success(returnValue))
            .onComplete(this.invokeHandler(message));
    }
}
