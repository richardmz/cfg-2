# cfg-2 - Another configuration format with parser and API

Unlike XML, cfg-2 does not support any digester-rule. It only supports parsing a configuration file into a
_fly.dream.cfg.Config_ object. Digestions left to the programmers.


## Format

### Stereotype

```
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


### Value types

* String - Surrounded by quotes (e.g. "...")
* Map - Surrounded by braces (e.g. {...})
* List - Surrounded by brackets (e.g. \[...\])


### Rules

* The root of a configuration must be a map without braces.
* Each entry of a map must be a key-value pair.
* Elements of a list must be same type.
* String must be surrounded by quotes.


## API

### Class _fly.dream.cfg.Loader_

Its instances can be used to load configuration data from the specified file into a _fly.dream.cfg.Config_ object.


#### Constructor

Constructor | Description
----------- | -----------
_Loader(Path path)_ | Constructs a loader with the specified path of file.


#### Method

Type | Method | Description
---- | ------ | -----------
_Config_ | _load()_ | Returns the _fly.dream.cfg.Config_ object parsed from the specified file.


### Class _fly.dream.cfg.Config_

Represents the root element of the configuration, and it's an anonymous map.


#### Methods

Type | Method | Description
---- | ------ | -----------
_boolean_ | _contains(String key)_ | Returns _true_ if it contains an entry with the specified key.
_Item_ | _get(String key)_ | Returns a _fly.dream.cfg.Item_ object to which the specified key is mapped, or _null_ if it contains no mapping for the key.


### Interface _fly.dream.cfg.Item_

#### Methods

Type | Method | Description
---- | ------ | -----------
_ItemType_ | _getType()_ | Returns the type of this item.
_String_ | _getString()_ | Returns the string value of this item if its type is _STRING_, or throws a runtime exception.
_Map<String, Item>_ | _getMap()_ | Returns the map value of this item if its type is _MAP_, or throws a runtime exception.
_List\<Item>_ | _getList()_ | Returns the list value of this item if its type is _LIST_, or throws a runtime exception.


### Enum _fly.dream.cfg.ItemType_

Includes following types:

* _STRING_
* _MAP_
* _LIST_


## How to use

1. [Install it as 3rd party JARs](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) to your maven repository.

2. Add it to your project via pom.xml.

```xml
<dependency>
    <groupId>fly.dream</groupId>
    <artifactId>cfg-2</artifactId>
    <version>1</version>
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


## Digestion example

Shows how to write code (see the constructor of _Configuration.java_ below) to digest the
_fly.dream.cfg.Config_ instance loaded from the _server.cfg_ file below.


### _server.cfg_

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

    // Digestion can go in constructor
    // Exception can be substituted to more proper subclasses
    Configuration(Config config) throws Exception
    {
        this.tmpDir = getTmpDir(config);
        this.hosts = getHosts(config);
        this.connectors = getConnectors(config);
    }

    private String getTmpDir(Config config) throws Exception
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
                return item.getString();
            }
        }
        else
        {
            return System.getProperty("user.dir") + File.separator + "temp";
        }
    }

    private Map<String, Host> getHosts(Config config) throws Exception
    {
        String key = "hosts";
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
                Map<String, Host> hosts = new HashMap<>(1);
                for (Map.Entry<String, Item> entry : item.getMap().entrySet())
                {
                    hosts.put(entry.getKey(), new Host(entry.getValue()));
                }
                return hosts;
            }
        }
    }

    private List<Connector> getConnectors(Config config) throws Exception
    {
        String key = "connectors";
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
                List<Connector> connectors = new ArrayList<>(2);
                for (Item i : item.getList())
                {
                    connectors.add(new Connector(i));
                }
                return connectors;
            }
        }
    }

    // Getters are omitted
}
```


### _Host.java_ and _Connector.java_ are omitted


## License

Apache 2.0 License. See LICENSE file.
