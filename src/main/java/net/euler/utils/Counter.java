package net.euler.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Counter<T> {
  private Map<T, Integer> map = Maps.newHashMap();

  /**
   * Increment count of key by 1.
   *
   * @param key Key to increment.
   */
  public void increment(final T key) {
    increment(key, 1);
  }

  /**
   * Increment count of key by amount.
   *
   * @param key       Key to increment.
   * @param increment Amount to increment.
   */
  public void increment(final T key, final int increment) {
    int value = (map.containsKey(key) ? map.get(key) : 0) + increment;
    if (value == 0) {
      map.remove(key);
    } else {
      map.put(key, value);
    }
  }

  /**
   * Decrement count of key by 1.
   *
   * @param key Key to decrement.
   */
  public void decrement(T key) {
    decrement(key, 1);
  }

  /**
   * Decrement count of key by 1.
   *
   * @param key       Key to decrement.
   * @param decrement Amount to decrement.
   */
  public void decrement(final T key, final int decrement) {
    increment(key, -decrement);
  }

  /**
   * Get count of key.
   *
   * @param key Key to get count of.
   * @return Count of key.
   */
  public int getCount(final T key) {
    return map.containsKey(key) ? map.get(key) : 0;
  }

  /**
   * Get total sum of counter values.
   *
   * @return Total sum of counter values.
   */
  public int getTotal() {
    int sum = 0;
    for (int count : map.values()) {
      sum += count;
    }
    return sum;
  }

  /**
   * Ascending sort by key.
   */
  public Set<Entry<T, Integer>> ascendingSortByKey() {
    return new TreeMap<>(map).entrySet();
  }

  /**
   * Decending sort by key.
   */
  public Set<Entry<T, Integer>> descendingSortByKey() {
    return new TreeMap<>(map).descendingMap().entrySet();
  }

  /**
   * Ascending sort by count.
   */
  public List<Entry<T, Integer>> ascendingSortByCount() {
    List<Entry<T, Integer>> entries = Lists.newArrayList(map.entrySet());
    Class<?> t = entries.get(0).getKey().getClass();

    Collections.sort(entries, new Comparator<Entry<T, Integer>>(){
      @Override
      @SuppressWarnings("unchecked")
      public int compare(Entry<T, Integer> entry1, Entry<T, Integer> entry2) {
        int result = entry1.getValue().compareTo(entry2.getValue());
        if (result == 0 && Comparable.class.isAssignableFrom(t)) {
          result = ((Comparable<? super T>)entry1.getKey()).compareTo(entry2.getKey());
        }
        return result;
      }
    });
    return entries;
  }

  /**
   * Descending sort by count.
   */
  public List<Entry<T, Integer>> descendingSortByCount() {
    List<Entry<T, Integer>> entries = Lists.newArrayList(map.entrySet());
    Class<?> t = entries.get(0).getKey().getClass();

    Collections.sort(entries, new Comparator<Entry<T, Integer>>(){
      @Override
      @SuppressWarnings("unchecked")
      public int compare(Entry<T, Integer> entry1, Entry<T, Integer> entry2) {
        int result = entry2.getValue().compareTo(entry1.getValue());
        if (result == 0 && Comparable.class.isAssignableFrom(t)) {
          result = ((Comparable<? super T>)entry1.getKey()).compareTo(entry2.getKey());
        }
        return result;
      }
    });

    return entries;
  }

  /**
   * Return string value of counter.
   *
   * @return String value of counter sorted by value.
   */
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for (Entry<T, Integer> entry : ascendingSortByCount()) {
      sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
    }
    return sb.toString();
  }
}

 
