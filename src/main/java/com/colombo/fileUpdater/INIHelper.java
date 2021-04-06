package com.colombo.fileUpdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INIHelper {
	private Pattern _section = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
	private Pattern _keyValue = Pattern.compile("\\s*([^=]*)=(.*)");
	private Map<String, Map<String, String>> _entries = new HashMap<>();
	private String path = null;

	public Map<String, Map<String, String>> get_entries() {
		return _entries;
	}

	public INIHelper(String path) throws IOException {
		this.path = path;
		load(this.path);
	}

	public void load(String path) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			String section = null;
			while ((line = br.readLine()) != null) {
				Matcher m = _section.matcher(line);
				if (m.matches()) {
					section = m.group(1).trim();
				} else if (section != null) {
					m = _keyValue.matcher(line);
					if (m.matches()) {
						String key = m.group(1).trim();
						String value = fixDoubleBackSlash(m.group(2).trim());
						Map<String, String> kv = _entries.get(section);
						if (kv == null) {
							_entries.put(section, kv = new HashMap<>());
						}
						kv.put(key, value);
					}
				}
			}
		}

	}

	public void setValue(String section, String key, String value) throws Exception {
		if (!_entries.containsKey(section) && _entries.get(section).containsValue(value)) {
			throw new Exception();
		}
		StringBuilder resultINI = new StringBuilder();

		for (Map.Entry<String, Map<String, String>> sections : _entries.entrySet()) {
			resultINI.append("[" + sections.getKey() + "]" + System.lineSeparator());
			for (Map.Entry<String, String> keysAndValues : _entries.get(sections.getKey()).entrySet()) {
				if (sections.getKey().equals(section) && keysAndValues.getKey().equals(key)) {
					resultINI.append(keysAndValues.getKey() + "=" + value + System.lineSeparator());
					continue;

				}
				resultINI.append(keysAndValues.getKey() + "=" + keysAndValues.getValue() + System.lineSeparator());
			}
		}
		try {
			FileWriter fw = new FileWriter(new File(this.path));
			BufferedWriter out = new BufferedWriter(fw);
			out.write(resultINI.toString());
			out.close();
		} catch (Exception e) {
		}

	}

	public String getString(String section, String key, String defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return kv.get(key);
	}

	public int getInt(String section, String key, int defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Integer.parseInt(kv.get(key));
	}

	public float getFloat(String section, String key, float defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Float.parseFloat(kv.get(key));
	}

	public double getDouble(String section, String key, double defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Double.parseDouble(kv.get(key));
	}

	public String fixDoubleBackSlash(String stringToFix) {
		stringToFix.replace("\\\\", "\\");
		return stringToFix;
	}
}
