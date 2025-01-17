package com.sanjuthomas.gcp.bigtable;

/**
 *
 * @author Sanju Thomas
 *
 */
@FunctionalInterface
public interface Parser<T, R> {

  public R parse(T t);

}
