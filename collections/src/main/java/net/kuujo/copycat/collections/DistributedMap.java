/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.collections;

import net.kuujo.copycat.PersistenceLevel;
import net.kuujo.copycat.Resource;
import net.kuujo.copycat.Stateful;
import net.kuujo.copycat.collections.state.MapCommands;
import net.kuujo.copycat.collections.state.MapState;
import net.kuujo.copycat.raft.ConsistencyLevel;
import net.kuujo.copycat.resource.ResourceContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Distributed map.
 *
 * @param <K> The map key type.
 * @param <V> The map entry type.
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@Stateful(MapState.class)
public class DistributedMap<K, V> extends Resource {

  public DistributedMap(ResourceContext context) {
    super(context);
  }

  /**
   * Checks whether the map is empty.
   *
   * @return A completable future to be completed with a boolean value indicating whether the map is empty.
   */
  public CompletableFuture<Boolean> isEmpty() {
    return submit(MapCommands.IsEmpty.builder().build());
  }

  /**
   * Checks whether the map is empty.
   *
   * @param consistency The query consistency level.
   * @return A completable future to be completed with a boolean value indicating whether the map is empty.
   */
  public CompletableFuture<Boolean> isEmpty(ConsistencyLevel consistency) {
    return submit(MapCommands.IsEmpty.builder().withConsistency(consistency).build());
  }

  /**
   * Gets the size of the map.
   *
   * @return A completable future to be completed with the number of entries in the map.
   */
  public CompletableFuture<Integer> size() {
    return submit(MapCommands.Size.builder().build());
  }

  /**
   * Gets the size of the map.
   *
   * @param consistency The query consistency level.
   * @return A completable future to be completed with the number of entries in the map.
   */
  public CompletableFuture<Integer> size(ConsistencyLevel consistency) {
    return submit(MapCommands.Size.builder().withConsistency(consistency).build());
  }

  /**
   * Checks whether the map contains a key.
   *
   * @param key The key to check.
   * @return A completable future to be completed with the result once complete.
   */
  public CompletableFuture<Boolean> containsKey(Object key) {
    return submit(MapCommands.ContainsKey.builder()
      .withKey(key)
      .build());
  }

  /**
   * Checks whether the map contains a key.
   *
   * @param key The key to check.
   * @param consistency The query consistency level.
   * @return A completable future to be completed with the result once complete.
   */
  public CompletableFuture<Boolean> containsKey(Object key, ConsistencyLevel consistency) {
    return submit(MapCommands.ContainsKey.builder()
      .withKey(key)
      .withConsistency(consistency)
      .build());
  }

  /**
   * Gets a value from the map.
   *
   * @param key The key to get.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> get(Object key) {
    return submit(MapCommands.Get.builder()
      .withKey(key)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Gets a value from the map.
   *
   * @param key The key to get.
   * @param consistency The query consistency level.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> get(Object key, ConsistencyLevel consistency) {
    return submit(MapCommands.Get.builder()
      .withKey(key)
      .withConsistency(consistency)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key   The key to set.
   * @param value The value to set.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key The key to set.
   * @param value The value to set.
   * @param persistence The persistence in which to set the key.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value, PersistenceLevel persistence) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .withPersistence(persistence)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key The key to set.
   * @param value The value to set.
   * @param ttl The time to live in milliseconds.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value, long ttl) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key The key to set.
   * @param value The value to set.
   * @param ttl The time to live in milliseconds.
   * @param persistence The persistence in which to set the key.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value, long ttl, PersistenceLevel persistence) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl)
      .withPersistence(persistence)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key The key to set.
   * @param value The value to set.
   * @param ttl The time to live in milliseconds.
   * @param unit The time to live unit.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value, long ttl, TimeUnit unit) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl, unit)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map.
   *
   * @param key The key to set.
   * @param value The value to set.
   * @param ttl The time to live in milliseconds.
   * @param unit The time to live unit.
   * @param persistence The persistence in which to set the key.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> put(K key, V value, long ttl, TimeUnit unit, PersistenceLevel persistence) {
    return submit(MapCommands.Put.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl, unit)
      .withPersistence(persistence)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Removes a value from the map.
   *
   * @param key The key to remove.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> remove(Object key) {
    return submit(MapCommands.Remove.builder()
      .withKey(key)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Gets the value of a key or the given default value if the key does not exist.
   *
   * @param key          The key to get.
   * @param defaultValue The default value to return if the key does not exist.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> getOrDefault(Object key, V defaultValue) {
    return submit(MapCommands.GetOrDefault.builder()
      .withKey(key)
      .withDefaultValue(defaultValue)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Gets the value of a key or the given default value if the key does not exist.
   *
   * @param key          The key to get.
   * @param defaultValue The default value to return if the key does not exist.
   * @param consistency The query consistency level.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> getOrDefault(Object key, V defaultValue, ConsistencyLevel consistency) {
    return submit(MapCommands.GetOrDefault.builder()
      .withKey(key)
      .withDefaultValue(defaultValue)
      .withConsistency(consistency)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map if the given key does not exist.
   *
   * @param key   The key to set.
   * @param value The value to set if the given key does not exist.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> putIfAbsent(K key, V value) {
    return submit(MapCommands.PutIfAbsent.builder()
      .withKey(key)
      .withValue(value)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map if the given key does not exist.
   *
   * @param key   The key to set.
   * @param value The value to set if the given key does not exist.
   * @param ttl The time to live in milliseconds.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> putIfAbsent(K key, V value, long ttl) {
    return submit(MapCommands.PutIfAbsent.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map if the given key does not exist.
   *
   * @param key   The key to set.
   * @param value The value to set if the given key does not exist.
   * @param ttl The time to live in milliseconds.
   * @param persistence The persistence in which to set the key.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> putIfAbsent(K key, V value, long ttl, PersistenceLevel persistence) {
    return submit(MapCommands.PutIfAbsent.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl)
      .withPersistence(persistence)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map if the given key does not exist.
   *
   * @param key   The key to set.
   * @param value The value to set if the given key does not exist.
   * @param ttl The time to live.
   * @param unit The time to live unit.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> putIfAbsent(K key, V value, long ttl, TimeUnit unit) {
    return submit(MapCommands.PutIfAbsent.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl, unit)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Puts a value in the map if the given key does not exist.
   *
   * @param key   The key to set.
   * @param value The value to set if the given key does not exist.
   * @param ttl The time to live.
   * @param unit The time to live unit.
   * @param persistence The persistence in which to set the key.
   * @return A completable future to be completed with the result once complete.
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<V> putIfAbsent(K key, V value, long ttl, TimeUnit unit, PersistenceLevel persistence) {
    return submit(MapCommands.PutIfAbsent.builder()
      .withKey(key)
      .withValue(value)
      .withTtl(ttl, unit)
      .withPersistence(persistence)
      .build())
      .thenApply(result -> (V) result);
  }

  /**
   * Removes a key and value from the map.
   *
   * @param key   The key to remove.
   * @param value The value to remove.
   * @return A completable future to be completed with the result once complete.
   */
  public CompletableFuture<Boolean> remove(Object key, Object value) {
    return submit(MapCommands.Remove.builder()
      .withKey(key)
      .withValue(value)
      .build())
      .thenApply(result -> (boolean) result);
  }

  /**
   * Removes all entries from the map.
   *
   * @return A completable future to be completed once the operation is complete.
   */
  public CompletableFuture<Void> clear() {
    return submit(MapCommands.Clear.builder().build());
  }

}
