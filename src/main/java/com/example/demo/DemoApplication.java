package com.example.demo;

import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DemoApplication {
	private static FileWriter file;
	public static void main(String[] args) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.parse(new File(args[0]), "utf-8");
		JSONObject data = new JSONObject();
		JSONObject buyer =  new JSONObject();
		buyer.put("lead_name",doc.select("meta[name=lead_name]").first().attr("content"));
		buyer.put("lead_email",doc.select("meta[name=lead_email]").first().attr("content"));
		buyer.put("lead_phone",doc.select("meta[name=lead_phone]").first().attr("content"));
		data.put("Lead Details", buyer);
		data.put("Property Details", new JSONObject());

		System.out.println(doc.select("meta[name=lead_email]").first().attr("content"));
		System.out.println(doc.select("meta[name=lead_name]").first().attr("content"));
		System.out.println(doc.select("meta[name=lead_phone]").first().attr("content"));
		long start = 8;
		int i = 0;
		while (true) {
			JSONObject seller = new JSONObject();
			String baseTag = "body > table > tbody > tr:nth-child(" + start + ") > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(5) > td";
			Element baseElement = doc.selectFirst(baseTag);
			if(baseElement == null || TextUtils.isEmpty(baseElement.toString()) || TextUtils.isEmpty(baseElement.text())){
				break;
			}
			Element name = baseElement.select(" font:nth-child(5) > strong > a").first();
			Element number = doc.select("font:nth-child(7) > strong > a").first();
			start += 4;
			seller.put("name", name.text());
			seller.put("number",number.text());
			data.optJSONObject("Property Details").put(String.valueOf(i), seller);
			i++;
		}
		try {

			// Constructs a FileWriter given a file name, using the platform's default charset
			file = new FileWriter(args[1]);
			file.write(data.toString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("JSON Object: " + data);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
