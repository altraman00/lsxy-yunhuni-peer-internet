package com.lsxy.framework.api.base;

/**
 * 实现此接口的对象可以在自己的内部定义一个接收自己proxy对象的属性，以便能在自己的内部调用到proxy对象，
 * setSelf(Object self)方法传入的就是这个对象的proxy对象，
 * 由于一些方法只有通过proxy对象调用自己的方法才能达到预期的目的，比如springCache的内部调用不起作用，所以定了此接口
 * 具体设置参考InjectBeanSelfProcessor
 * Created by liups on 2016/11/17.
 */
public interface BeanSelfAware {
    void setSelf(Object self);
}
