# cfg-2 - Another configuration format with parser and API

Unlike XML, cfg-2 does not support any digester-rule. It only supports parsing a configuration file into a
fly.dream.cfg.Config object. Digesting jobs are left to the programmers.

### Format

    # Comment
    
    key1 "value"

    map1
    {
      key1 "value"
    
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
    
    # List can be map list, value list and list list.
    # Elements of list should be anonymous (not key specified).
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
    
    valueList1
    [
      value1
      value2
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

### How to use

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

### License

Apache 2.0 License. See LICENSE file.
