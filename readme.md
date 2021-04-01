# Java SPI 技术测试

> 示例环境为 JDK 1.8_261 + IDEA 2020.1
> 阅读本文默认您熟悉IDEA的各类操作

## 操作步骤

1. 创建Empty Project

2. 依次创建3个Module于不同目录下, 分别命名为`base`, `impl1`, `impl2`

3. 在Project Structure中分别调整`impl1`和`impl2`的Dependencies
   添加`base`作为Module Dependency
   设定其Scope为Provided

4. 为`impl1`和`impl2`创建Artifacts, 类型为`Jar: from modules with dependencies`

5. 在`base`Module中创建代码如下

* `firok.spi.IService`

```java
package firok.spi;

public interface IService
{
    void run();
}
```

* `firok.spi.Main`

```java
package firok.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Main
{
    public static void main(String[] args)
    {
        ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
        Iterator<IService> iter = loader.iterator();

        int count = 0;
        while(iter.hasNext())
        {
            IService service = iter.next();
            System.out.println("\nfound one: "+service.getClass()+"\n");
            service.run();

            count++;
        }
        System.out.println("\ncount: "+count);
    }
}
```

6. 在`impl1`Module中创建代码如下

* `firok.spi.Impl1`

```java
package firok.spi;

public class Impl1 implements IService
{
    @Override
    public void run()
    {
        System.out.println("impl1 run!");
    }
}
```

7. 在`impl1`Module中创建文件夹`resources/META-INF/services`, 并将`resources`文件夹Mark as Resources Root

8. 在`impl1`的`resources/META-INF/services`目录中创建文件`firok.spi.IService`, 内容如下

```
firok.spi.Impl1
```

9.  在`impl2`Module中创建代码如下

* `firok.spi.Impl2`

```java
package firok.spi;

public class Impl2 implements IService
{
    @Override
    public void run()
    {
        System.out.println("impl2 run!");
    }
}
```
10. 在`impl2`Module中创建文件夹`resources/META-INF/services`, 并将`resources`文件夹Mark as Resources Root

11. 在`impl2`的`resources/META-INF/services`目录中创建文件`firok.spi.IService`, 内容如下

```
firok.spi.Impl2
```

12. 为`impl1`和`impl2`Build Artifacts, 默认情况下会在`/out/artifacts/**/`下生成对应的jar包文件

13. 在Project Structure中为`base`Module新增Libraries, 选中上一步里生成的`impl1.jar`和`impl2.jar`

14. 运行`firok.spi.Main#main`, 可得到如下控制台输出

```

found one: class firok.spi.Impl2

impl2 run!

found one: class firok.spi.Impl1

impl1 run!

count: 2

```

能看到通过SPI找到了两个服务实现类

## 解释

* 步骤3中需要为`impl1`和`imple2`设定`base`的依赖, 否则无法找到`firok.spi.IService`
* 步骤13中需要为`base`添加两个jar包依赖, 否则`base`运行时, classpath上下文中不包含SPI配置文件和相关的具体实现类, 执行结果会是`count: 0`
* 步骤8和步骤11中创建的SPI配置文件的文件名全名就是`firok.spi.IService`
* 步骤7和步骤10中被Marked as Resources Root的目录是两个`resources`目录而不是子目录, 如果开启了Compact Middle Packages功能, 这一步可能会选错, 请注意
