optional的语言插件的支持
optional的操作可以避免大量的判断null的语言的操作执行需要的。
很多的插件的mybatis的数据判断，对应的都是需要相关的null的判断的。
存在很多的问题和很多需要优化以及解决的问题的。
mybatis的很多的代码都是进行了重构支持optional组件的。


#下面是测试泛型的使用和理解操作实现
public class MyStudent<T,S,W>
