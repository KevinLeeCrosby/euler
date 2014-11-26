package net.euler;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by kevin on 11/26/14.
 */
public class P067 {
  public static void main(String[] args) throws IOException {
    String file = "/net/euler/problem67.txt";
    InputStream is = P018.class.getResourceAsStream(file);
    List<List<Integer>> triangle = P018.loadTriangle(is);

    int total = P018.getMaxTotal(triangle);

    System.out.println("The maximum total from top to bottom of the triangle is " + total);
  }
}
