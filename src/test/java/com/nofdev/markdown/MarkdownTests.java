package com.nofdev.markdown;

import com.nofdev.markdown.service.MarkdownService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class MarkdownTests {

	@Autowired
	private MarkdownService markdownService;


	@Test
	void contextLoads() {
	}

	@Test
	void testHeaders() {
		Map<String, String> map = Map.of(
				"# header 1", "<h1>header 1</h1>",
				"## header 2", "<h2>header 2</h2>",
				"### header 3", "<h3>header 3</h3>",
				"#### header 4", "<h4>header 4</h4>",
				"##### header 5", "<h5>header 5</h5>",
				"###### header 6", "<h6>header 6</h6>",
				"####### header 7", "<h6># header 7</h6>"
		);

		for (String key : map.keySet()) {
			String result = markdownService.convertHeader(key);
			assertEquals(map.get(key), result);
		}
	}

	@Test
	void testLinks() {
		String test = "For examples click [Examples](https://www.example.com) or if you want to get some pizza, click this: [Pizza](http://jetspizza.com).";
		String expect = "For examples click <a href=\"https://www.example.com\">Examples</a> or if you want to get some pizza, click this: <a href=\"http://jetspizza.com\">Pizza</a>.";
		assertEquals(expect, markdownService.convertLinks(test));

	}

	@Test
	void testDocument1() {
		String test = "# Sample Document\n" +
				"\n" +
				"Hello!\n" +
				"\n" +
				"This is sample markdown for the [Links](https://www.links.com) assignment.";

		String expect = "<h1>Sample Document</h1>\n" +
				"\n" +
				"<p>Hello!</p>\n" +
				"\n" +
				"<p>This is sample markdown for the <a href=\"https://www.links.com\">Links</a> homework assignment.</p>";

		assertEquals(expect, markdownService.convertToHtml(test));

	}

	@Test
	void testDocument2() {
		String test = "# Header one\n" +
				"\n" +
				"Hello there\n" +
				"\n" +
				"How are you?\n" +
				"What's going on?\n" +
				"\n" +
				"## Another Header\n" +
				"\n" +
				"This is a paragraph [with an inline link](http://google.com). Neat, eh?\n" +
				"\n" +
				"## This is a header [with a link](http://yahoo.com)";

		String expect = "<h1>Header one</h1>\n" +
				"\n" +
				"<p>Hello there</p>\n" +
				"\n" +
				"<p>How are you?\n" +
				"What's going on?</p>\n" +
				"\n" +
				"<h2>Another Header</h2>\n" +
				"\n" +
				"<p>This is a paragraph <a href=\"http://google.com\">with an inline link</a>. Neat, eh?</p>\n" +
				"\n" +
				"<h2>This is a header <a href=\"http://yahoo.com\">with a link</a></h2>";

		assertEquals(expect, markdownService.convertToHtml(test));

	}




}
