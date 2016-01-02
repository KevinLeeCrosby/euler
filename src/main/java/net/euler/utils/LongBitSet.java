package net.euler.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Adapted from http://java-performance.info/bit-sets/
 *
 * @author Kevin Crosby
 */
public class LongBitSet implements Cloneable, java.io.Serializable {
  /**
   * Number of bits allocated to a value in an index
   */
  private static final int VALUE_BITS = 20; //1M values per bit set

  /**
   * Mask for extracting values
   */
  private static final long VALUE_MASK = (1L << VALUE_BITS) - 1;

  /**
   * Map from a value stored in high bits of a long index to a bit set mapped to the lower bits of an index.
   * Bit sets size should be balanced - not to long (otherwise setting a single bit may waste megabytes of memory)
   * but not too short (otherwise this map will get too big). Update value of {@code VALUE_BITS} for your needs.
   * In most cases it is ok to keep 1M - 64M values in a bit set, so each bit set will occupy 128Kb - 8Mb.
   */
  private final Map<Long, BitSet> m_sets;

  /**
   * Get long index by set index and bit index
   *
   * @param setIndex Set index
   * @return Global long index
   */
  private long getIndex(final long setIndex, final int bitIndex) {
    return (setIndex << VALUE_BITS) + bitIndex;
  }

  /**
   * Get set index by long index (extract bits 20-63)
   *
   * @param index Long index
   * @return Index of a bit set in the inner map
   */
  private long getSetIndex(final long index) {
    return index >> VALUE_BITS;
  }

  /**
   * Get index of a value in a bit set (bits 0-19)
   *
   * @param index Long index
   * @return Index of a value in a bit set
   */
  private int getBitIndex(final long index) {
    return (int) (index & VALUE_MASK);
  }

  /**
   * Helper method to get (or create, if necessary) a bit set for a given long index
   *
   * @param index Long index
   * @return A bit set for a given index (always not null)
   */
  private BitSet bitSet(final long index) {
    final long setIndex = getSetIndex(index);
    BitSet bitSet = m_sets.get(setIndex);
    if(bitSet == null) {
      bitSet = new BitSet(1024);
      m_sets.put(setIndex, bitSet);
    }
    return bitSet;
  }

  /**
   * Creates a new bit set. All bits are initially {@code false}.
   */
  public LongBitSet() {
    m_sets = Maps.newHashMapWithExpectedSize(20);
  }

  //  /**
  //   * Creates a bit set whose initial size is large enough to explicitly
  //   * represent bits with indices in the range {@code 0} through
  //   * {@code nbits-1}. All bits are initially {@code false}.
  //   *
  //   * @param nbits the initial size of the bit set
  //   * @throws NegativeArraySizeException if the specified initial size
  //   *                                    is negative
  //   */
  //  public LongBitSet(final long nbits) {
  //    TODO
  //  }

  //  /**
  //   * Returns a new bit set containing all the bits in the given long array.
  //   *
  //   * <p>More precisely,
  //   * <br>{@code LongBitSet.valueOf(longs).get(n) == ((longs[n/64] & (1L<<(n%64))) != 0)}
  //   * <br>for all {@code n < 64 * longs.length}.
  //   *
  //   * <p>This method is equivalent to
  //   * {@code LongBitSet.valueOf(LongBuffer.wrap(longs))}.
  //   *
  //   * @param longs a long array containing a little-endian representation
  //   *              of a sequence of bits to be used as the initial bits of the
  //   *              new bit set
  //   * @return a {@code LongBitSet} containing all the bits in the long array
  //   * @since 1.7
  //   */
  //  public static LongBitSet valueOf(final long[] longs) {
  //    TODO
  //  }

  //  /**
  //   * Returns a new bit set containing all the bits in the given long
  //   * buffer between its position and limit.
  //   *
  //   * <p>More precisely,
  //   * <br>{@code LongBitSet.valueOf(lb).get(n) == ((lb.get(lb.position()+n/64) & (1L<<(n%64))) != 0)}
  //   * <br>for all {@code n < 64 * lb.remaining()}.
  //   *
  //   * <p>The long buffer is not modified by this method, and no
  //   * reference to the buffer is retained by the bit set.
  //   *
  //   * @param lb a long buffer containing a little-endian representation
  //   *           of a sequence of bits between its position and limit, to be
  //   *           used as the initial bits of the new bit set
  //   * @return a {@code LongBitSet} containing all the bits in the buffer in the
  //   * specified range
  //   * @since 1.7
  //   */
  //  public static LongBitSet valueOf(final LongBuffer lb) {
  //    TODO
  //  }

  //  /**
  //   * Returns a new bit set containing all the bits in the given byte array.
  //   *
  //   * <p>More precisely,
  //   * <br>{@code LongBitSet.valueOf(bytes).get(n) == ((bytes[n/8] & (1<<(n%8))) != 0)}
  //   * <br>for all {@code n <  8 * bytes.length}.
  //   *
  //   * <p>This method is equivalent to
  //   * {@code LongBitSet.valueOf(ByteBuffer.wrap(bytes))}.
  //   *
  //   * @param bytes a byte array containing a little-endian
  //   *              representation of a sequence of bits to be used as the
  //   *              initial bits of the new bit set
  //   * @return a {@code LongBitSet} containing all the bits in the byte array
  //   * @since 1.7
  //   */
  //  public static LongBitSet valueOf(final byte[] bytes) {
  //    TODO
  //  }

  //  /**
  //   * Returns a new bit set containing all the bits in the given byte
  //   * buffer between its position and limit.
  //   *
  //   * <p>More precisely,
  //   * <br>{@code LongBitSet.valueOf(bb).get(n) == ((bb.get(bb.position()+n/8) & (1<<(n%8))) != 0)}
  //   * <br>for all {@code n < 8 * bb.remaining()}.
  //   *
  //   * <p>The byte buffer is not modified by this method, and no
  //   * reference to the buffer is retained by the bit set.
  //   *
  //   * @param bb a byte buffer containing a little-endian representation
  //   *           of a sequence of bits between its position and limit, to be
  //   *           used as the initial bits of the new bit set
  //   * @return a {@code LongBitSet} containing all the bits in the buffer in the
  //   * specified range
  //   * @since 1.7
  //   */
  //  public static LongBitSet valueOf(final ByteBuffer bb) {
  //    TODO
  //  }

  //  /**
  //   * Returns a new byte array containing all the bits in this bit set.
  //   *
  //   * <p>More precisely, if
  //   * <br>{@code byte[] bytes = s.toByteArray();}
  //   * <br>then {@code bytes.length == (s.length()+7)/8} and
  //   * <br>{@code s.get(n) == ((bytes[n/8] & (1<<(n%8))) != 0)}
  //   * <br>for all {@code n < 8 * bytes.length}.
  //   *
  //   * @return a byte array containing a little-endian representation
  //   * of all the bits in this bit set
  //   * @since 1.7
  //   */
  //  public byte[] toByteArray() {
  //    TODO
  //  }

  //  /**
  //   * Returns a new long array containing all the bits in this bit set.
  //   *
  //   * <p>More precisely, if
  //   * <br>{@code long[] longs = s.toLongArray();}
  //   * <br>then {@code longs.length == (s.length()+63)/64} and
  //   * <br>{@code s.get(n) == ((longs[n/64] & (1L<<(n%64))) != 0)}
  //   * <br>for all {@code n < 64 * longs.length}.
  //   *
  //   * @return a long array containing a little-endian representation
  //   * of all the bits in this bit set
  //   * @since 1.7
  //   */
  //  public long[] toLongArray() {
  //    TODO
  //  }

  /**
   * Sets the bit at the specified index to the complement of its
   * current value.
   *
   * @param bitIndex the index of the bit to flip
   */
  public void flip(final long bitIndex) {
    set(bitIndex, !get(bitIndex));
  }

  /**
   * Sets each bit from the specified {@code fromIndex} (inclusive) to the
   * specified {@code toIndex} (exclusive) to the complement of its current
   * value.
   *
   * @param fromIndex index of the first bit to flip
   * @param toIndex   index after the last bit to flip
   */
  public void flip(final long fromIndex, final long toIndex) {
    for(long bitIndex = fromIndex; bitIndex < toIndex; ++bitIndex) {
      flip(bitIndex);
    }
  }

  /**
   * Sets the bit at the specified index to {@code true}.
   *
   * @param bitIndex a bit index
   */
  public void set(final long bitIndex) {
    set(bitIndex, true);
  }

  /**
   * Sets the bit at the specified index to the specified value.
   *
   * @param bitIndex a bit index
   * @param value    a boolean value to set
   */
  public void set(final long bitIndex, final boolean value) {
    if(value) {
      bitSet(bitIndex).set(getBitIndex(bitIndex));
    } else {  //if value shall be cleared, check first if given partition exists
      final BitSet bitSet = m_sets.get(getSetIndex(bitIndex));
      if(bitSet != null) {
        bitSet.clear(getBitIndex(bitIndex));
      }
    }
  }

  /**
   * Sets the bits from the specified {@code fromIndex} (inclusive) to the
   * specified {@code toIndex} (exclusive) to {@code true}.
   *
   * @param fromIndex index of the first bit to be set
   * @param toIndex   index after the last bit to be set
   */
  public void set(final long fromIndex, final long toIndex) {
    for(long bitIndex = fromIndex; bitIndex < toIndex; ++bitIndex) {
      set(bitIndex);
    }
  }

  /**
   * Sets the bits from the specified {@code fromIndex} (inclusive) to the
   * specified {@code toIndex} (exclusive) to the specified value.
   *
   * @param fromIndex index of the first bit to be set
   * @param toIndex   index after the last bit to be set
   * @param value     value to set the selected bits to
   */
  public void set(final long fromIndex, final long toIndex, final boolean value) {
    for(long bitIndex = fromIndex; bitIndex < toIndex; ++bitIndex) {
      set(bitIndex, value);
    }
  }

  /**
   * Sets the bit specified by the index to {@code false}.
   *
   * @param bitIndex the index of the bit to be cleared
   * @throws IndexOutOfBoundsException if the specified index is negative
   */
  public void clear(final long bitIndex) {
    clear(bitIndex, bitIndex + 1);
  }

  /**
   * Sets the bits from the specified {@code fromIndex} (inclusive) to the
   * specified {@code toIndex} (exclusive) to {@code false}.
   *
   * @param fromIndex index of the first bit to be cleared
   * @param toIndex   index after the last bit to be cleared
   */
  public void clear(final long fromIndex, final long toIndex) {
    if(fromIndex >= toIndex) {
      return;
    }
    final long fromSetIndex = getSetIndex(fromIndex);
    final long toSetIndex = getSetIndex(toIndex);
    //remove all maps in the middle
    for(long i = fromSetIndex + 1; i < toSetIndex; ++i) {
      m_sets.remove(i);
    }
    //clean two corner sets manually
    final BitSet fromSet = m_sets.get(fromSetIndex);
    final BitSet toSet = m_sets.get(toSetIndex);
    ///are both ends in the same subset?
    if(fromSet != null && fromSet == toSet) {
      fromSet.clear(getBitIndex(fromIndex), getBitIndex(toIndex));
      return;
    }
    //clean left subset from left index to the end
    if(fromSet != null) {
      fromSet.clear(getBitIndex(fromIndex), fromSet.length());
    }
    //clean right subset from 0 to given index. Note that both checks are independent
    if(toSet != null) {
      toSet.clear(0, getBitIndex(toIndex));
    }
  }

  /**
   * Sets all of the bits in this LongBitSet to {@code false}.
   */
  public void clear() {
    m_sets.clear();
  }

  /**
   * Returns the value of the bit with the specified index. The value
   * is {@code true} if the bit with the index {@code bitIndex}
   * is currently set in this {@code LongBitSet}; otherwise, the result
   * is {@code false}.
   *
   * @param bitIndex the bit index
   * @return the value of the bit with the specified index
   */
  public boolean get(final long bitIndex) {
    final BitSet bitSet = m_sets.get(getSetIndex(bitIndex));
    return bitSet != null && bitSet.get(getBitIndex(bitIndex));
  }

  //  /**
  //   * Returns a new {@code LongBitSet} composed of bits from this {@code LongBitSet}
  //   * from {@code fromIndex} (inclusive) to {@code toIndex} (exclusive).
  //   *
  //   * @param fromIndex index of the first bit to include
  //   * @param toIndex   index after the last bit to include
  //   * @return a new {@code LongBitSet} from a range of this {@code LongBitSet}
  //   */
  //  public LongBitSet get(final long fromIndex, final long toIndex) {
  //    for(long bitIndex = fromIndex; bitIndex < toIndex; ++bitIndex) {
  //      TODO
  //    }
  //  }

  /**
   * Returns the index of the first bit that is set to {@code true}
   * that occurs on or after the specified starting index. If no such
   * bit exists then {@code -1} is returned.
   *
   * <p>To iterate over the {@code true} bits in a {@code LongBitSet},
   * use the following loop:
   *
   * <pre> {@code
   * for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
   *     // operate on index i here
   *     if (i == Long.MAX_VALUE) {
   *         break; // or (i+1) would overflow
   *     }
   * }}</pre>
   *
   * @param fromIndex the index to start checking from (inclusive)
   * @return the index of the next set bit, or {@code -1} if there
   * is no such bit
   */
  public long nextSetBit(final long fromIndex) {
    int fromBitIndex = getBitIndex(fromIndex);
    long fromSetIndex = getSetIndex(fromIndex), maxSetIndex = m_sets.isEmpty() ? 0 : Collections.max(m_sets.keySet());
    BitSet bitSet = m_sets.get(fromSetIndex);
    while(bitSet == null && fromSetIndex <= maxSetIndex) {
      bitSet = m_sets.get(++fromSetIndex);
      fromBitIndex = 0;
    }
    if(fromSetIndex > maxSetIndex) {
      return -1;
    }
    assert bitSet != null;
    int bitIndex = bitSet.nextSetBit(fromBitIndex);
    if(bitIndex < 0) {
      return -1;
    }
    return getIndex(fromSetIndex, bitIndex);
  }

  /**
   * Returns the index of the first bit that is set to {@code false}
   * that occurs on or after the specified starting index.
   *
   * @param fromIndex the index to start checking from (inclusive)
   * @return the index of the next clear bit
   */
  public long nextClearBit(final long fromIndex) {
    int fromBitIndex = getBitIndex(fromIndex);
    long fromSetIndex = getSetIndex(fromIndex), maxSetIndex = m_sets.isEmpty() ? 0 : Collections.max(m_sets.keySet());
    BitSet bitSet = m_sets.get(fromSetIndex);
    while(bitSet == null && fromSetIndex <= maxSetIndex) {
      bitSet = m_sets.get(++fromSetIndex);
      fromBitIndex = 0;
    }
    if(fromSetIndex > maxSetIndex) {
      return getIndex(maxSetIndex, 0);
    }
    assert bitSet != null;
    int bitIndex = bitSet.nextClearBit(fromBitIndex);
    return getIndex(fromSetIndex, bitIndex);
  }

  /**
   * Returns the index of the nearest bit that is set to {@code true}
   * that occurs on or before the specified starting index.
   * If no such bit exists, or if {@code -1} is given as the
   * starting index, then {@code -1} is returned.
   *
   * <p>To iterate over the {@code true} bits in a {@code LongBitSet},
   * use the following loop:
   *
   * <pre> {@code
   * for (int i = bs.length(); (i = bs.previousSetBit(i-1)) >= 0; ) {
   *     // operate on index i here
   * }}</pre>
   *
   * @param fromIndex the index to start checking from (inclusive)
   * @return the index of the previous set bit, or {@code -1} if there
   * is no such bit
   */
  public long previousSetBit(final long fromIndex) {
    int fromBitIndex = getBitIndex(fromIndex);
    long fromSetIndex = getSetIndex(fromIndex), minSetIndex = m_sets.isEmpty() ? 0 : Collections.min(m_sets.keySet());
    BitSet bitSet = m_sets.get(fromSetIndex);
    while(bitSet == null && fromSetIndex >= minSetIndex) {
      bitSet = m_sets.get(--fromSetIndex);
      fromBitIndex = VALUE_BITS - 1;
    }
    if(fromSetIndex < minSetIndex) {
      return -1;
    }
    assert bitSet != null;
    int bitIndex = bitSet.previousSetBit(fromBitIndex);
    if(bitIndex < 0) {
      return -1;
    }
    return getIndex(fromSetIndex, bitIndex);
  }

  /**
   * Returns the index of the nearest bit that is set to {@code false}
   * that occurs on or before the specified starting index.
   * If no such bit exists, or if {@code -1} is given as the
   * starting index, then {@code -1} is returned.
   *
   * @param fromIndex the index to start checking from (inclusive)
   * @return the index of the previous clear bit, or {@code -1} if there
   * is no such bit
   */
  public long previousClearBit(final long fromIndex) {
    int fromBitIndex = getBitIndex(fromIndex);
    long fromSetIndex = getSetIndex(fromIndex), minSetIndex = m_sets.isEmpty() ? 0 : Collections.min(m_sets.keySet());
    BitSet bitSet = m_sets.get(fromSetIndex);
    while(bitSet == null && fromSetIndex >= minSetIndex) {
      bitSet = m_sets.get(--fromSetIndex);
      fromBitIndex = VALUE_BITS - 1;
    }
    if(fromSetIndex < minSetIndex) {
      return -1;
    }
    assert bitSet != null;
    int bitIndex = bitSet.previousClearBit(fromBitIndex);
    if(bitIndex < 0) {
      return -1;
    }
    return getIndex(fromSetIndex, bitIndex);
  }

  /**
   * Returns the "logical size" of this {@code LongBitSet}: the index of
   * the highest set bit in the {@code LongBitSet} plus one. Returns zero
   * if the {@code LongBitSet} contains no set bits.
   *
   * @return the logical size of this {@code LongBitSet}
   */
  public long length() {
    return m_sets.values().stream().mapToLong(BitSet::length).sum();
  }

  /**
   * Returns true if this {@code LongBitSet} contains no bits that are set
   * to {@code true}.
   *
   * @return boolean indicating whether this {@code LongBitSet} is empty
   */
  public boolean isEmpty() {
    return m_sets.values().stream().allMatch(BitSet::isEmpty);
  }

  //  /**
  //   * Returns true if the specified {@code LongBitSet} has any bits set to
  //   * {@code true} that are also set to {@code true} in this {@code LongBitSet}.
  //   *
  //   * @param set {@code LongBitSet} to intersect with
  //   * @return boolean indicating whether this {@code LongBitSet} intersects
  //   * the specified {@code LongBitSet}
  //   */
  //  public boolean intersects(final LongBitSet set) {
  //    TODO
  //  }

  /**
   * Returns the number of bits set to {@code true} in this {@code LongBitSet}.
   *
   * @return the number of bits set to {@code true} in this {@code LongBitSet}
   */
  public long cardinality() {
    return m_sets.values().stream().mapToLong(BitSet::cardinality).sum();
  }

  /**
   * Performs a logical <b>AND</b> of this target bit set with the
   * argument bit set. This bit set is modified so that each bit in it
   * has the value {@code true} if and only if it both initially
   * had the value {@code true} and the corresponding bit in the
   * bit set argument also had the value {@code true}.
   *
   * @param set a bit set
   */
  public void and(final LongBitSet set) {
    for(final Map.Entry<Long, BitSet> entry : m_sets.entrySet()) {
      long setIndex = entry.getKey();
      BitSet bitSet1 = entry.getValue(), bitSet2 = set.m_sets.get(setIndex);
      if(bitSet2 != null) {
        bitSet1.and(bitSet2);
        if(bitSet1.isEmpty()) {
          m_sets.remove(setIndex);
        }
      } else {
        m_sets.remove(setIndex);
      }
    }
  }

  /**
   * Performs a logical <b>OR</b> of this bit set with the bit set
   * argument. This bit set is modified so that a bit in it has the
   * value {@code true} if and only if it either already had the
   * value {@code true} or the corresponding bit in the bit set
   * argument has the value {@code true}.
   *
   * @param set a bit set
   */
  public void or(final LongBitSet set) {
    for(final Map.Entry<Long, BitSet> entry : set.m_sets.entrySet()) {
      long setIndex = entry.getKey();
      BitSet bitSet1 = m_sets.get(setIndex), bitSet2 = entry.getValue();
      if(bitSet1 != null) {
        bitSet1.or(bitSet2);
      } else {
        m_sets.put(setIndex, bitSet2);
      }
    }
  }

  /**
   * Performs a logical <b>XOR</b> of this bit set with the bit set
   * argument. This bit set is modified so that a bit in it has the
   * value {@code true} if and only if one of the following
   * statements holds:
   * <ul>
   * <li>The bit initially has the value {@code true}, and the
   * corresponding bit in the argument has the value {@code false}.
   * <li>The bit initially has the value {@code false}, and the
   * corresponding bit in the argument has the value {@code true}.
   * </ul>
   *
   * @param set a bit set
   */
  public void xor(final LongBitSet set) {
    for(final long setIndex : Sets.union(m_sets.keySet(), set.m_sets.keySet())) {
      BitSet bitSet1 = m_sets.get(setIndex), bitSet2 = set.m_sets.get(setIndex);
      if(bitSet1 != null) {
        if(bitSet2 != null) {
          bitSet1.xor(bitSet2);
          if(bitSet1.isEmpty()) {
            m_sets.remove(setIndex);
          }
        } else {
          m_sets.put(setIndex, bitSet1);
        }
      } else {
        if(bitSet2 != null) {
          m_sets.put(setIndex, bitSet2);
        }
      }
    }
  }

  /**
   * Clears all of the bits in this {@code LongBitSet} whose corresponding
   * bit is set in the specified {@code LongBitSet}.
   *
   * @param set the {@code LongBitSet} with which to mask this
   *            {@code LongBitSet}
   */
  public void andNot(final LongBitSet set) {
    for(final Map.Entry<Long, BitSet> entry : m_sets.entrySet()) {
      long setIndex = entry.getKey();
      BitSet bitSet1 = entry.getValue(), bitSet2 = set.m_sets.get(setIndex);
      if(bitSet2 != null) {
        bitSet1.andNot(bitSet2);
        if(bitSet1.isEmpty()) {
          m_sets.remove(setIndex);
        }
      }
    }
  }

  /**
   * Returns the hash code value for this bit set. The hash code depends
   * only on which bits are set within this {@code LongBitSet}.
   *
   * <p>The hash code is defined to be the result of the following
   * calculation:
   * <pre> {@code
   * public int hashCode() {
   *     long h = 1234;
   *     long[] words = toLongArray();
   *     for (int i = words.length; --i >= 0; )
   *         h ^= words[i] * (i + 1);
   *     return (int)((h >> 32) ^ h);
   * }}</pre>
   * Note that the hash code changes if the set of bits is altered.
   *
   * @return the hash code value for this bit set
   */
  @Override
  public int hashCode() { // FIXME may have Hash collision problems
    int hash = 0;
    for(final Map.Entry<Long, BitSet> entry : m_sets.entrySet()) {
      hash ^= entry.getValue().hashCode();
    }
    System.err.println("LongBitSet.hashCode may have hash collision problems.");
    return hash;
  }

  /**
   * Returns the number of bits of space actually in use by this
   * {@code LongBitSet} to represent bit values.
   * The maximum element in the set is the size - 1st element.
   *
   * @return the number of bits currently in this bit set
   */
  public long size() {
    return m_sets.values().stream().mapToLong(BitSet::size).sum();
  }

  /**
   * Compares this object against the specified object.
   * The result is {@code true} if and only if the argument is
   * not {@code null} and is a {@code Bitset} object that has
   * exactly the same set of bits set to {@code true} as this bit
   * set. That is, for every nonnegative {@code int} index {@code k},
   * <pre>((LongBitSet)obj).get(k) == this.get(k)</pre>
   * must be true. The current sizes of the two bit sets are not compared.
   *
   * @param obj the object to compare with
   * @return {@code true} if the objects are the same;
   * {@code false} otherwise
   * @see #size()
   */
  @Override
  public boolean equals(final Object obj) {
    if(!(obj instanceof LongBitSet)) {
      return false;
    }
    if(this == obj) {
      return true;
    }

    return m_sets.equals(((LongBitSet) obj).m_sets);
  }

  /**
   * Cloning this {@code LongBitSet} produces a new {@code LongBitSet}
   * that is equal to it.
   * The clone of the bit set is another bit set that has exactly the
   * same bits set to {@code true} as this bit set.
   *
   * @return a clone of this bit set
   * @see #size()
   */
  @Override
  public Object clone() {
    LongBitSet set = new LongBitSet();
    m_sets.forEach(set.m_sets::put);
    return set;
  }

  /**
   * Returns a string representation of this bit set. For every index
   * for which this {@code LongBitSet} contains a bit in the set
   * state, the decimal representation of that index is included in
   * the result. Such indices are listed in order from lowest to
   * highest, separated by ",&nbsp;" (a comma and a space) and
   * surrounded by braces, resulting in the usual mathematical
   * notation for a set of integers.
   *
   * <p>Example:
   * <pre>
   * LongBitSet drPepper = new LongBitSet();</pre>
   * Now {@code drPepper.toString()} returns "{@code {}}".
   * <pre>
   * drPepper.set(2);</pre>
   * Now {@code drPepper.toString()} returns "{@code {2}}".
   * <pre>
   * drPepper.set(4);
   * drPepper.set(10);</pre>
   * Now {@code drPepper.toString()} returns "{@code {2, 4, 10}}".
   *
   * @return a string representation of this bit set
   */
  @Override
  public String toString() {
    List<Map.Entry<Long, BitSet>> entries = Lists.newArrayList(m_sets.entrySet());
    entries.sort((entry1, entry2) -> Long.compare(entry2.getKey(), entry1.getKey()));

    StringBuilder sb = new StringBuilder();
    for(final Map.Entry<Long, BitSet> entry : entries) {
      sb.append(entry.getKey()).append(":").append(entry.getValue());
    }
    return sb.toString();
  }

  //  /**
  //   * Returns a stream of indices for which this {@code LongBitSet}
  //   * contains a bit in the set state. The indices are returned
  //   * in order, from lowest to highest. The size of the stream
  //   * is the number of bits in the set state, equal to the value
  //   * returned by the {@link #cardinality()} method.
  //   *
  //   * <p>The bit set must remain constant during the execution of the
  //   * terminal stream operation.  Otherwise, the result of the terminal
  //   * stream operation is undefined.
  //   *
  //   * @return a stream of integers representing set indices
  //   * @since 1.8
  //   */
  //  public IntStream stream() {
  //    TODO
  //  }
}
