package rg.self.documentmanagement;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Testclass {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("admin123"));
//		Testclass tc = new Testclass();
//		tc.duplicateElementsInList();
//		tc.firstNonRepeatedCharactersInAString();
//		tc.sumOfAllDigitsInAList();
//		tc.groupByAndCountOccurences();
//		tc.minMaxInList();
//		tc.removeNullAndStringFromList();
//		tc.sortMapByValue();
	}

	public void duplicateElementsInList() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<Integer> list = Arrays.asList(1, 2, 3, 2, 4, 1, 5);
		Set<Integer> set = list.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting())).entrySet()
				.stream().filter(a -> a.getValue() > 1).map(a -> a.getKey()).collect(Collectors.toSet());
//		Set<Integer> set = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//				.entrySet().stream().filter(a -> a.getValue() > 1).map(a -> a.getKey()).collect(Collectors.toSet());
		System.out.println(methodName + " " + set);
	}

	public void firstNonRepeatedCharactersInAString() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String input = "JavaArticles";
		char ab = IntStream.range(0, input.length()).mapToObj(i -> input.charAt(i))
				.collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting())).entrySet().stream()
				.filter(e -> e.getValue() == 1).map(Map.Entry::getKey).findFirst().orElse('\0');

		char a = input.chars().mapToObj(c -> (char) c)
				.collect(Collectors.groupingBy(ch -> ch, LinkedHashMap::new, Collectors.counting())).entrySet().stream()
				.filter(e -> e.getValue() == 1).map(Map.Entry::getKey).findFirst().orElse('\0');
		System.out.println(methodName + " " + "ab=>" + ab + " a=>" + a);
	}

	public void sumOfAllDigitsInAList() {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<Integer> nums = Arrays.asList(12, 34, 56);
		int sum1 = nums.stream().collect(Collectors.summingInt(a -> a));
		System.out.println(sum1);
		int sum = nums.stream().flatMapToInt(n -> String.valueOf(n).chars()).map(Character::getNumericValue).sum();
		System.out.println(methodName + " " + sum);
	}

	public void groupByAndCountOccurences() {
		List<String> items = Arrays.asList("apple", "banana", "apple", "orange");
		Map<String, Long> mp = items.stream()
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
		mp.entrySet().forEach(entry -> {
			System.out.println("groupByAndCountOccurences , entry.getKey() " + entry.getKey() + " entry.getValue() "
					+ entry.getValue());
		});

		Map<Character, Long> mp1 = items.stream().flatMap(str -> str.chars().mapToObj(c -> (char) c))
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
		mp1.entrySet().forEach(System.out::print);
		System.out.println();
	}

	public void minMaxInList() {
		List<Integer> nums = Arrays.asList(3, 6, 2, 8);
		int max = nums.stream().max(Integer::compareTo).get();
		int min = nums.stream().min(Integer::compareTo).orElseThrow();
		System.out.println("minMaxInList :: min " + min + " max " + max);
	}

	public void removeNullAndStringFromList() {
		List<String> names = Arrays.asList("John", "", null, "Alice");
		names = names.stream().filter(str -> str != null && str != "").collect(Collectors.toList());
		System.out.println(names);
	}

	public void sortMapByValue() {
		Map<String, Integer> map = Map.of("apple", 3, "banana", 2, "orange", 5);
		map.forEach((k, v) -> System.out.println(k + v));
		Map<String, Integer> mp = map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, a) -> s, LinkedHashMap::new));
		mp.forEach((k, v) -> System.out.println(k + v));
	}

	public void twoListsAreAnagram() {
		List<String> a = Arrays.asList("a", "b", "c");
		List<String> b = Arrays.asList("c", "b", "a");
		a.equals(b);
		boolean isAnagram = a.stream().sorted().collect(Collectors.toList())
				.equals(b.stream().sorted().collect(Collectors.toList()));
		System.out.println("twoListsAreAnagram " + isAnagram);
	}

	public void flattenAListOfLists() {
		List<List<Integer>> nested = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4));

		List<Integer> flat = nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
		System.out.println(flat);
	}
}
