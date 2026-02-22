package com.abhinav.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 * DTOComparisonUtil - Simple Explanation
 *
 * <p>DTOComparisonUtil compares two DTOs (expected vs actual) and reports differences.
 *
 * <p>What it does (simple) - Takes two objects (expected and actual) - Compares them field by field
 * - Finds differences - Throws an error with details if they don't match
 *
 * <p>Real-world analogy Like comparing two forms: - Expected: What you think the API should return
 * - Actual: What the API actually returned - Result: Lists all fields that don't match
 *
 * <p>How it's used // Create expected response GetConversationsResponseDTO expectedResponse =
 * GetConversationsResponseDTO.createDefault(); // Get actual response from API
 * GetConversationsResponseDTO actualResponse = controller.getConversations(headers, 200); //
 * Compare them - throws error if different DtoComparisonUtil.compareAndAssert(expectedResponse,
 * actualResponse);
 *
 * <p>What happens inside - Converts both DTOs to JSON trees - Compares each field recursively -
 * Tracks differences (missing fields, wrong values, etc.) - If differences found → throws error
 * with details - If no differences → test passes
 */
public class DtoComparisonUtil {

  private static final Logger LOGGER = LoggerUtil.getLogger(DtoComparisonUtil.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private DtoComparisonUtil() {
    // utility class
  }

  /**
   * Compares two DTOs and fails the TestNG assertion if there are differences.
   *
   * @param expected The expected DTO or object.
   * @param actual The actual DTO or object.
   * @param ignoredFields Fields/paths to ignore (e.g. "id", "data.createdAt", "users[0]").
   */
  public static void compareAndAssert(Object expected, Object actual, String... ignoredFields) {
    List<String> differences = getDifferences(expected, actual, ignoredFields);
    if (!differences.isEmpty()) {
      String errorMsg =
          "\n=== DTO Comparison Failed ===\n"
              + String.join("\n", differences)
              + "\n=============================\n";
      LOGGER.error(errorMsg);
      throw new AssertionError(errorMsg);
    }
  }

  /**
   * Compares two DTOs and logs the differences (useful for debugging, without failing).
   *
   * @param expected The expected DTO or object.
   * @param actual The actual DTO or object.
   * @param ignoredFields Fields/paths to ignore.
   * @return true if equal, false otherwise.
   */
  public static boolean logDifferences(Object expected, Object actual, String... ignoredFields) {
    List<String> differences = getDifferences(expected, actual, ignoredFields);
    if (!differences.isEmpty()) {
      LOGGER.info("\n=== DTO Differences Found ===\n{}", String.join("\n", differences));
      return false;
    }
    LOGGER.info("DTOs are identical.");
    return true;
  }

  /** Computes the list of string differences between two objects across their fields. */
  private static List<String> getDifferences(
      Object expected, Object actual, String... ignoredFields) {
    List<String> diffs = new ArrayList<>();
    List<String> ignoredList =
        ignoredFields != null ? Arrays.asList(ignoredFields) : new ArrayList<>();

    if (expected == null && actual == null) return diffs;
    if (expected == null || actual == null) {
      diffs.add(String.format("Root: Expected '%s' but was '%s'", expected, actual));
      return diffs;
    }

    try {
      JsonNode expectedNode = OBJECT_MAPPER.valueToTree(expected);
      JsonNode actualNode = OBJECT_MAPPER.valueToTree(actual);
      compareNodes("$", expectedNode, actualNode, ignoredList, diffs);
    } catch (Exception e) {
      diffs.add("Internal Object mapping exception: " + e.getMessage());
    }
    return diffs;
  }

  /** Recursively compares two JSON nodes. */
  private static void compareNodes(
      String path, JsonNode expected, JsonNode actual, List<String> ignored, List<String> diffs) {
    if (ignored.contains(path)) {
      return;
    }

    if (expected == null && actual == null) return;

    if (expected == null || actual == null || expected.isNull() || actual.isNull()) {
      if (expected != null && expected.isNull() && actual != null && actual.isNull()) {
        return; // Both are explicitly null nodes, match.
      }
      String expTxt = (expected == null || expected.isNull()) ? "null" : expected.toString();
      String actTxt = (actual == null || actual.isNull()) ? "null" : actual.toString();
      diffs.add(String.format("Path '%s': Expected '%s' but was '%s'", path, expTxt, actTxt));
      return;
    }

    if (expected.getNodeType() != actual.getNodeType()) {
      diffs.add(
          String.format(
              "Path '%s': Type mismatch. Expected %s but was %s",
              path, expected.getNodeType(), actual.getNodeType()));
      return;
    }

    if (expected.isObject()) {
      Iterator<Map.Entry<String, JsonNode>> expectedIter = expected.fields();
      while (expectedIter.hasNext()) {
        Map.Entry<String, JsonNode> entry = expectedIter.next();
        String fieldName = entry.getKey();
        String childPath = path.equals("$") ? fieldName : path + "." + fieldName;
        compareNodes(childPath, entry.getValue(), actual.get(fieldName), ignored, diffs);
      }

      Iterator<Map.Entry<String, JsonNode>> actualIter = actual.fields();
      while (actualIter.hasNext()) {
        Map.Entry<String, JsonNode> entry = actualIter.next();
        String fieldName = entry.getKey();
        String childPath = path.equals("$") ? fieldName : path + "." + fieldName;

        if (!expected.has(fieldName) && !ignored.contains(childPath)) {
          diffs.add(
              String.format(
                  "Path '%s': Extra field present in actual but not in expected.", childPath));
        }
      }
    } else if (expected.isArray()) {
      if (expected.size() != actual.size()) {
        diffs.add(
            String.format(
                "Path '%s': Array size mismatch. Expected %d items but got %d items.",
                path, expected.size(), actual.size()));
      }
      int limit = Math.min(expected.size(), actual.size());
      for (int i = 0; i < limit; i++) {
        compareNodes(path + "[" + i + "]", expected.get(i), actual.get(i), ignored, diffs);
      }
    } else {
      // Primitive / String value comparison
      if (!expected.equals(actual)) {
        diffs.add(
            String.format(
                "Path '%s': Expected value '%s' but was '%s'",
                path, expected.asText(), actual.asText()));
      }
    }
  }
}
