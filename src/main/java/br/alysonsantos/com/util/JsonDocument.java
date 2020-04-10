package br.alysonsantos.com.util;

import com.google.gson.*;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

@Getter
public class JsonDocument {

	public static JsonDocument of(File parent, String name, Gson gson) throws IOException {
		return new JsonDocument(parent, name, gson);
	}

	public static JsonDocument of(File parent, String name) throws IOException {
		return of(parent, name, new GsonBuilder()
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.create());
	}

	public static JsonDocument safeOf(File parent, String name, Gson gson) {
		try {
			return of(parent, name, gson);
		} catch (IOException e) {
			return null;
		}
	}

	public static JsonDocument safeOf(File parent, String name) {
		try {
			return of(parent, name);
		} catch (IOException e) {
			return null;
		}
	}

	private File file;
	private JsonObject json;
	private Gson gson;
	private Map<String, Object> defaultValues = new WeakHashMap<>();

	private JsonDocument(File parent, String name, Gson gson) throws IOException {
		if(!parent.exists() && !parent.mkdir()) {
			throw new RuntimeException("Could not create parent directory");
		}

		this.file = new File(parent.getAbsolutePath() + File.separator + name);
		if(!file.exists()) {
			if(!file.createNewFile()) throw new RuntimeException("Could not create file");
		}

		this.gson = gson;

		FileReader fileReader = new FileReader(file);
		JsonElement jsonElement = new JsonParser().parse(fileReader);

		fileReader.close();

		if(jsonElement instanceof JsonNull) {
			json = new JsonObject();
		} else {
			json = jsonElement.getAsJsonObject();
		}
	}

	public void reload() throws IOException {
		if(!file.exists()) {
			if(!file.createNewFile()) throw new RuntimeException("Could not create file");
		}

		FileReader fileReader = new FileReader(file);
		JsonElement jsonElement = new JsonParser().parse(fileReader);

		fileReader.close();

		if(jsonElement instanceof JsonNull) {
			json = new JsonObject();
		} else {
			json = jsonElement.getAsJsonObject();
		}
	}

	public void loadDefaultValues() throws IOException {
		defaultValues.forEach((key, value) -> {
			if(!contains(key))
				set(key, value);
		});

		save();
	}

	public void loadDefaultValue(String name) throws IOException {
		Object value = defaultValues.get(name);
		if(value == null) return;
		if(contains(name)) return;

		set(name, value);
		save();
	}

	public JsonDocument addDefaultValue(String key, Object value) {
		defaultValues.put(key, value);
		return this;
	}

	public boolean contains(String key) {
		if(!key.contains(".")) {
			return json.has(key);
		}

		String[] path = key.split("\\.");
		JsonElement currentElement = json;

		for (String subPath : path) {
			if(!currentElement.isJsonObject()) {
				return false;
			} else if(!currentElement.getAsJsonObject().has(subPath)) {
				return false;
			}

			currentElement = currentElement.getAsJsonObject().get(subPath);
		}

		return true;
	}

	public JsonElement get(String key) {
		if(!contains(key)) {
			return JsonNull.INSTANCE;
		} else if(!key.contains(".")) {
			return json.get(key);
		}

		String[] path = key.split("\\.");
		JsonElement currentElement = json;

		for (String subPath : path) {
			if(!currentElement.isJsonObject()) {
				return JsonNull.INSTANCE;
			} else if(!currentElement.getAsJsonObject().has(subPath)) {
				return JsonNull.INSTANCE;
			}

			currentElement = currentElement.getAsJsonObject().get(subPath);
		}

		return currentElement;
	}

	public JsonDocument set(String key, Object value) {
		if(!key.contains(".")) {
			set(json, key, value);
			return this;
		}

		String[] path = key.split("\\.");
		JsonObject currentElement = json;

		for (int i = 0; i < path.length; i++) {
			String subPath = path[i];

			if(i == path.length - 1) {
				set(currentElement, subPath, value);
				break;
			}

			if(!currentElement.has(subPath)) {
				currentElement.add(subPath, new JsonObject());
				currentElement = currentElement.get(subPath).getAsJsonObject();
				continue;
			}

			currentElement = currentElement.get(subPath).getAsJsonObject();
		}

		return this;
	}

	private boolean set(JsonObject jsonObject, String key, Object value) {
		if(value == null) {
			jsonObject.remove(key);
		} else {
			jsonObject.add(key, gson.toJsonTree(value));
		}

		return true;
	}

	public void save() throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		gson.toJson(json, fileWriter);

		fileWriter.flush();
		fileWriter.close();
	}

	public <T> T from(String path, Class<T> type) {
		return gson.fromJson(get(path), type);
	}

}