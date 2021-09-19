package net.sbkn.guns.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BidirectionalMap<KeyType, ValueType> {
  private final Map<KeyType, ValueType> keyToValueMap = new ConcurrentHashMap<>();
  private final Map<ValueType, KeyType> valueToKeyMap = new ConcurrentHashMap<>();
  
  public synchronized void put(KeyType key, ValueType value) {
    this.keyToValueMap.put(key, value);
    this.valueToKeyMap.put(value, key);
  }
  
  public synchronized ValueType removeByKey(KeyType key) {
    ValueType removedValue = this.keyToValueMap.remove(key);
    this.valueToKeyMap.remove(removedValue);
    return removedValue;
  }
  
  public synchronized KeyType removeByValue(ValueType value) {
    KeyType removedKey = this.valueToKeyMap.remove(value);
    this.keyToValueMap.remove(removedKey);
    return removedKey;
  }
  
  public boolean containsKey(KeyType key) {
    return this.keyToValueMap.containsKey(key);
  }
  
  public boolean containsValue(ValueType value) {
    return this.keyToValueMap.containsValue(value);
  }
  
  public KeyType getKey(ValueType value) {
    return this.valueToKeyMap.get(value);
  }
  
  public ValueType get(KeyType key) {
    return this.keyToValueMap.get(key);
  }
}
