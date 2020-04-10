package br.alysonsantos.com.core.data;

import br.alysonsantos.com.util.JsonDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Credentials {

	public static Credentials of(JsonDocument document) {
		try {
			if(!document.contains("db.host")) {
				document.addDefaultValue("db.host", "localhost");
				document.loadDefaultValue("db.host");
			}
			if(!document.contains("db.port")) {
				document.addDefaultValue("db.port", "3306");
				document.loadDefaultValue("db.port");
			}
			if(!document.contains("db.username")) {
				document.addDefaultValue("db.username", "root");
				document.loadDefaultValue("db.username");
			}
			if(!document.contains("db.password")) {
				document.addDefaultValue("db.password", "password");
				document.loadDefaultValue("db.password");
			}
			if(!document.contains("db.database")) {
				document.addDefaultValue("db.database", "test");
				document.loadDefaultValue("db.database");
			}

			String host = document.get("db.host").getAsString(),
					username = document.get("db.username").getAsString(),
					password = document.get("db.password").getAsString(),
					database = document.get("db.database").getAsString();
			int port = document.get("db.port").getAsInt();

			return Credentials.builder()
					.host(host)
					.port(port)
					.username(username)
					.password(password)
					.database(database)
					.build();
		} catch (Exception e) {
			return null;
		}
	}

	private String host;
	private int port;
	private String username, password, database;

}
