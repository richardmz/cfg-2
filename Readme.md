# cfg-2 - Another configuration format with parser and API

Unlike XML, cfg-2 does not support any digester-rule. It only supports parsing a configuration file into a
_fly.dream.cfg.Config_ object. Digesting jobs are left to the programmers.

## Format

### Stereotype

```editorconfig
# Comment

string1 "string value"

map1
{
  string1 "string value"

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

# A list can either be a map list, a string list or a list list.
# Elements of a list must be anonymous, that they're not key specified.
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
  "string1"
  "string2"
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

### Rules

* The root of a configuration file is a map without braces.
* Each entry of a map is a key-value pair.
* Each element of a list must only be same type of value.
* String must be surrounded by quotes.

### Value types

* String - "..."
* Map - {...}
* List - \[...\]

## How to use

1. [Install it as 3rd party JARs](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) to your maven repository.

2. Add it to your project via pom.xml.

```xml
<dependency>
    <groupId>fly.dream</groupId>
    <artifactId>cfg</artifactId>
    <version>2</version>
</dependency>
```

3. Java code

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

* _ItemType getType()_ - Returns the type (_ItemType_) of this item.
* _String getString()_ - Returns the string value of this item if it's type of _STRING_, or throws a runtime exception.
* _Map<String, Item> getMap()_ - Returns the map value of this item if it's type of _STRING_, or throws a runtime exception.
* _List<Item> getList()_ - Returns the list value of this item if it's type of _STRING_, or throws a runtime exception.

### _fly.dream.cfg.ItemType_

Includes following types:

* STRING
* MAP
* LIST
* PROPERTY - Only appears in parsing process. Eliminated in the parse result.

## Digest example

### _server-config.cfg_

```editorconfig
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

        // tmp.dir
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

        // hosts
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

        // connectors
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

## License

Apache 2.0 License. See LICENSE file.
