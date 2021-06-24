# cfg-2 - 另一种配置格式与它的 Parser 和 API

与 XML 不同的是，cfg-2 不支持任何消化器规则（Digester-Rule），只支持将配置文件解析成 _fly.dream.cfg.Config_ 对象，将消化的工作留给了程序员。


## 格式

### 铅板

```
# 注释

string1 "字符串"

map1
{
  string1 "字符串"

  map1
  {
    ...
  }

  list1
  [
    ...
  ]
  ...
}

# 列表（List）可以是映射图列表（Map List）、字符串列表（String List）或列表列表（List List）
# 列表中的元件（Element）必须是匿名的，即没有键（Key）的。
mapList1
[
  {
    ...
  }
  {
    ...
  }
  ...
]

stringList1
[
  "字符串1"
  "字符串2"
  ...
]

listList1
[
  [
    ...
  ]
  [
    ...
  ]
  ...
]
```


### 值的类型

* String - 由英文双引号环绕（例如 "..."）
* Map - 由英文花括号环绕（例如 {...}）
* List - 由英文方括号环绕（例如 \[...\]）


### 规则

* 一个配置（Configuration）的根节点必须是一个没有花括号的映射图（Map）。
* 映射图（Map）的每个条目（Entry）必须是键（Key）值（Value）对。
* 列表（List）的每个元件（Element）必须且只能是同类型的值（Value）。
* 字符串（String）必须由英文双引号环绕。


## 使用方法

1. 作为 [第三方 JARs 安装](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) 进你的 Maven 库。

2. 添加至项目的 pom.xml 中。

```xml
<dependency>
    <groupId>fly.dream</groupId>
    <artifactId>cfg</artifactId>
    <version>2</version>
</dependency>
```

3. Java 代码

```java
Path file = ...
try
{
    Loader loader = new Loader(file);
    Config config = loader.load();
    ...
}
catch (IOException | ConfigException e)
{
    ...
}
```


## API

### _fly.dream.cfg.Loader_

可用其实例（Instance）将指定文件中的配置数据加载为 _fly.dream.cfg.Config_ 对象。


#### Constructor

* _Loader(Path path)_ - 构造一个指定了文件路径的加载器。


#### Method

* _Config load()_ - 返回由指定文件解析出来的 _fly.dream.cfg.Config_ 对象。


### _fly.dream.cfg.Config_

代表配置的根元件，同时是个匿名映射图。


#### Methods

* _boolean contains(String key)_ - 如果含有指定 key 对应的条目，则返回 _true_。
* _Item get(String key)_ - 如果含有指定 key 对应的 _fly.dream.cfg.Item_ 对象，则将该对象返回，否则返回 _null_。

### _fly.dream.cfg.Item_

提供以下方法：


#### Methods

* _ItemType getType()_ - 返回该实例的类型。
* _String getString()_ - 如果该实例的类型为 STRING，则返回字符串值，否则抛出运行时异常。
* _Map<String, Item> getMap()_ - 如果该实例的类型为 MAP，则返回映射图，否则抛出运行时异常。
* _List<Item> getList()_ - 如果该实例的类型为 LIST，则返回列表，否则抛出运行时异常。

### _fly.dream.cfg.ItemType_

包括以下类型：

* _STRING_
* _MAP_
* _LIST_
* _PROPERTY_ - 只在解析过程出现，解析结果中已被消除。


## 消化示例### _server-config.cfg_

```
tmp.dir "custom-temp-dir"

hosts
{
  localhost
  {
    app.base "web-apps"
  }
}

connectors
[
  {
    port "8080"
    upgrade.insecure.request "1"
    upgrade.port "8443"
  }

  {
    port "8443"
    ssl "on"
    ssl.context.protocol "TLS"
    keystore.file ".keystore"
    passphrase "Change it"
    keystore.type "JKS"
    keystore.algorithm "PKIX"
    truststore.type "JKS"
    truststore.algorithm "PKIX"
    compression "on"
  }
]
```


### _Configuration.java_

```java
import ...

class Configuration
{
    private final String tmpDir;
    private final Map<String, Host> hosts;
    private final List<Connector> connectors;

    // 可在构造函数中进行
    // 可将 Exception 换成一些更合适的子类
    Configuration(Config config) throws Exception
    {
        String key = "tmp.dir";
        if (config.contains(key))
        {
            Item item = config.get(key);
            if (item.getType() != STRING)
            {
                throw new Exception("Type of '" + key + "' must be STRING");
            }
            else
            {
                this.tmpDir = item.getString();
            }
        }
        else
        {
            this.tmpDir = System.getProperty("user.dir") + File.separator + "temp";
        }

        key = "hosts";
        if (!config.contains(key))
        {
            throw new Exception("'" + key + "' is required");
        }
        else
        {
            Item item = config.get(key);
            if (item.getType() != MAP)
            {
                throw new Exception("Type of '" + key + "' must be MAP");
            }
            else
            {
                hosts = new HashMap<String, Host>(1);
                for (Map.Entry<String, Item> entry : item.getMap().entrySet())
                {
                    hosts.put(entry.getKey(), new Host(entry.getValue()));
                }
            }
        }

        key = "connectors";
        if (!config.contains(key))
        {
            throw new Exception("'" + key + "' is required");
        }
        else
        {
            Item item = config.get(key);
            if (item.getType() != LIST)
            {
                throw new Exception("Type of '" + key + "' must be LIST");
            }
            else
            {
                connectors = new ArrayList<Connector>(2);
                for (Item i : item.getList())
                {
                    connectors.add(new Connector(i));
                }
            }
        }
    }

    // Getters 不再赘述
}
```


### _Host.java_ 和 _Connector.java_ 不再赘述


## 许可

Apache 2.0 许可. 见 LICENSE 文件.
