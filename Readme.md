# cfg-2 - Another configuration format with parser and API

Unlike XML, cfg-2 does not support any digester-rule. It only supports parsing a configuration file into a
_fly.dream.cfg.Config_ object. Digesting jobs are left to the programmers.

## Format

### Stereotype

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

1. Install it as a 3rd part jar to your maven repository.
2. Add it to your project via pom.xml.

### Java code

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

* _ItemType getType()_
* _String getString()_
* _Map<String, Item> getMap()_
* _List<Item> getList()_

## License

Apache 2.0 License. See LICENSE file.
