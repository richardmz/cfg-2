# cfg-2 - 另一种配置格式与它的 Parser 和 API

与 XML 不同的是，cfg-2 不支持任何消化器规则（Digester-Rule）。它只支持将配置文件解析成 fly.dream.cfg.Config 对象。消化的工作留给了程序员。

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

# 列表（List）可以是映射列表（Map List）、字符串列表（String List）或列表列表（List List）
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

### Rules

* 一个配置（Configuration）的根节点必须是一个没有花括号的映射图（Map）。
* 图（Map）的每个条目（Entry）必须是键（Key）值（Value）对。
* 列表（List）的每个元件（Element）必须且只能是同类型的值（Value）。
* 字符串（String）必须由英文双引号环绕。

## How to use

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

This class provides instances to load configuration from a specified file into a _fly.dream.cfg.Config_ object.

#### Constructor

* _Loader(Path path)_ - Constructs a loader with the specified path of file.
  
#### Method

* _Config load()_ - Returns the _fly.dream.cfg.Config_ object parsed from the specified file.

### _fly.dream.cfg.Config_

The root element of the configuration, and it's an anonymous map.

#### Methods

* _boolean contains(String key)_ - Returns true if it contains an entry with the specified key.
* _Item get(String key)_ - Returns a _fly.dream.cfg.Item_ object to which the specified key is mapped, or _null_ if it
contains no mapping for the key.

### _fly.dream.cfg.Item_

An interface provides methods below:

#### Methods

* _ItemType getType()_ - Returns the type of this item.
* _String getString()_ - Returns the string value of this item if it's type of _STRING_, or throws a runtime exception.
* _Map<String, Item> getMap()_ - Returns the map value of this item if it's type of _STRING_, or throws a runtime exception.
* _List<Item> getList()_ - Returns the list value of this item if it's type of _STRING_, or throws a runtime exception.

### _fly.dream.cfg.ItemType_

Includes following types:

* _STRING_
* _MAP_
* _LIST_
* _PROPERTY_ - Only appears in the parsing process. Eliminated in the parse result.

## Digest example

### _server-config.cfg_

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

    // You can change Exception to a more suitable one
    Configuration(Config config) throws Exception
    {
        // Digest in its constructor

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

    // Getters are omitted
}
```

### _Host.java_ and _Connector.java_ are omitted

## 许可

Apache 2.0 许可. 见 LICENSE 文件.
