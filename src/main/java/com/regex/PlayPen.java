package com.regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayPen {
	public static void testMatches() {
		System.out.println("one + one = 2".replace("on.", "1"));
	}

	public static void testStringMethods() {
		String string = "x and tigers and bears, oh my!";
		String replaced = string.replaceAll("q", "Lions");
		System.out.println(string == replaced);
	}

	public static void testPatternSplit() {
		final Pattern comma = Pattern.compile(",");
		String[] result = comma.split("12, 34, 56, 78");
		System.out.println(Arrays.toString(result));
	}

	public static void validateMasterCardNumber() {
		Pattern cardNumber = Pattern
				.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$");
		boolean matches = cardNumber.matcher("5112345678890123").matches();
		System.out.println(matches);
		matches = cardNumber.matcher("2221012345678901").matches();
		System.out.println(matches);
		matches = cardNumber.matcher("2721012345678901").matches();
		System.out.println(matches);
	}

	public static void validateMasterCardNumberUsingStreams() {
		Pattern cardNumber = Pattern
				.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$");
		List<String> valids = Stream.of("5112345678890123", "2221012345678901", "2721012345678901")
				.filter(cardNumber.asPredicate()).collect(Collectors.toList());
		System.out.println(valids);
	}

	public static void replaceDate() {
		Pattern fwdSlash = Pattern.compile("/");
		Matcher matcher = fwdSlash.matcher("12/21/2018");
		System.out.println(matcher.replaceAll("-"));
	}

	public static void invalidRegex() {
		try {
			Pattern.compile("\\w\\d[]");
		} catch (PatternSyntaxException e) {
			System.out.println("Index: " + e.getIndex());
			System.out.println("Pattern: " + e.getPattern());
			System.out.println("Message: " + e.getMessage());
		}
	}

	public static void testEscapeCharacter() {
		"I favor the numbers 22 and 34".replaceFirst("\\d", "4");
	}

	public static void testPhoneNumber() {
		Pattern phoneNumberPattern = Pattern.compile("^\\s*\\(\\d{3}\\)\\s*\\d{3}-\\d{4}\\s*$");
		System.out.println(phoneNumberPattern.matcher(" (212) 345-6789").matches());
	}

	public static void display(Pattern pattern, String group) {
		Matcher matcher = pattern.matcher(group);
		int count = matcher.groupCount();
		if (matcher.matches()) {
			// groups are numbered from zero, upto and including the count
			for (int i = 0; i <= count; i++) {
				System.out.printf("%s.%s%n", i, matcher.group(i));
			}
		}
	}

	public static void displaygroups(Pattern pattern, String label, String... groupNames) {
		Matcher matcher = pattern.matcher(label);
		if (matcher.matches()) {
			System.out.println(matcher.group(0));
			for (String name : groupNames) {
				System.out.printf("%s: %s%n", name, matcher.group(name));
			}
		}
	}

	public static void replaceAllWithReferences() {
		List<String> list = Arrays
				.stream(new String[] { "securities-development-equities-valuation-asia",
						"fixed-income-development-equities-emea", "fx-development-america" })
				.map(label -> label.replaceAll("(?<business>\\w+)(-(\\w+))+-(?<region>\\w+)",
						"Region:${region}, Unit:$1"))
				.collect(Collectors.toList());
		list.forEach(System.out::println);
	}

	public static void alternation() {
		String text = "The \"rain\" in Spain \"falls\" mainly on \"the plain\"";
		Pattern pattern = Pattern.compile("(\"\\w+\")|(\\w+)");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			System.out.print("Group 1: " + matcher.group(1) + ";");
			System.out.println("Group 2: " + matcher.group(2));
		}
	}

	public static void canonicalEquivalance() {
		String text = "\u006e\u0303";
		String regex = "\u00F1";
		System.out.printf("Text: '%s' Regex: '%s' %n", text, regex);
		Pattern pattern = Pattern.compile(regex, Pattern.CANON_EQ);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Match! Group 0:" + matcher.group(0));
			return;
		}
		System.out.println("No match");
	}

	public static void dotAll() {
		String regex = "abc.+def";
		String text = "abc\r\ndef";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Match!");
			return;
		}
		System.out.println("No match");
	}

	public static void unicodeCase() {
		String regex = "\u00E0"; // à
		String text = "\u00C0"; // À
		System.out.printf("Text: '%s' Regex: '%s' %n", text, regex);
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Match!");
			return;
		}
		System.out.println("No match");
	}

	public static void unicodeCharacterClass() {
		String text = "Müller";
		String regex = "^[\\w]+$";
		Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			System.out.println("Match! " + matcher.group(0));
			return;
		}
		System.out.println("No match");
	}

	public static void testMatches2() {
		boolean matches = Pattern.matches("[\\w\\s]+", "She sells sea shells");
		System.out.println(matches);
	}

	public static void testSplitGTZero() {
		Pattern pattern = Pattern.compile(";\\s*");
		String[] split = pattern.split("One; Two; Three; Four; Five; ;");
		System.out.printf("%s: Length: %d", Arrays.asList(split), split.length);
	}

	public static void testQuote() {
		String quoted = Pattern.quote("[^(");
		Pattern pattern = Pattern.compile(quoted);
		String text = "[^(";
		System.out.printf("Text: '%s' Quoted: '%s'%n", text, quoted);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			System.out.println("Match!");
			return;
		}
		System.out.println("No Match");
	}

	public static void testAsPredicate() {
		// Filter out empty characters from List
		List<String> list = Arrays.asList("One", "Two", " ", "Buckle", "  ", "My_Shoe");
		Pattern pattern = Pattern.compile("(\\w)");
		List<String> cleanList = list.stream().filter(pattern.asPredicate()).collect(Collectors.toList());
		System.out.println(cleanList);
	}

	public static void testSplitAsStream() {
		// Filter out empty characters from List
		String text = "One, Two,  , Buckle,		, My_Shoe";
		Pattern pattern = Pattern.compile("(,\\s+)");
		List<String> list = pattern.splitAsStream(text).map(String::toUpperCase).collect(Collectors.toList());
		System.out.println(list);
	}

	public static void testEmbeddedVsCompiledFlags() {
		Pattern pattern = Pattern.compile("\\w", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		System.out.println("flags():" + Integer.toHexString(pattern.flags()));
		System.out.println("pattern(): " + pattern.pattern());
		System.out.println("toString(): " + pattern.toString());
	}

	public static void main(String[] args) {
		// Pattern pattern = Pattern.compile("(\\w+)(-(\\w+))+-(\\w+)");
		// display(pattern, "securities-development-equities-valuation-asia");
		// display(pattern, "fixed-income-development-equities-emea");
		// display(pattern, "fx-development-america");

		// Pattern pattern =
		// Pattern.compile("(?<business>\\w+)(-(\\w+))+-(?<region>\\w+)");
		// String[] groupNames = new String[] { "business", "region" };
		// displaygroups(pattern, "securities-development-equities-valuation-asia",
		// groupNames);
		// displaygroups(pattern, "fixed-income-development-equities-emea", groupNames);
		// displaygroups(pattern, "fx-development-america", groupNames);

		// replaceAllWithReferences();
		testEmbeddedVsCompiledFlags();
	}
}
