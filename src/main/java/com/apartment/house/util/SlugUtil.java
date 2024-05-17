package com.apartment.house.util;

public class SlugUtil {

  public static String toSlug(String title) {
    return title.toLowerCase()
        .replaceAll("\\s+", "-")
        .replaceAll("[^a-z0-9-]", "");
  }
}
