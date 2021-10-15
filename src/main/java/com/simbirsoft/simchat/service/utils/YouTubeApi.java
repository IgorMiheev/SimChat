package com.simbirsoft.simchat.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

@Component
public class YouTubeApi {

	public static String DEVELOPER_KEY;

	@Value("${youtube.developerkey}")
	private void setDeveloperKey(String key) {
		YouTubeApi.DEVELOPER_KEY = key;
	}

	// private static final Collection<String> SCOPES =
	// Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

	private static final String APPLICATION_NAME = "API code samples";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/**
	 * Build and return an authorized API client service.
	 *
	 * @return an authorized API client service
	 * @throws Exception
	 */
	public static YouTube getService() throws Exception {
		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		// Credential credential = authorize(httpTransport);
		// return new YouTube.Builder(httpTransport, JSON_FACTORY,
		// credential).setApplicationName(APPLICATION_NAME)
		// .build();
		return new YouTube.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Call function to create API service object. Define and execute API request.
	 * Print API response.
	 * 
	 * @throws Exception
	 */
//	public static void main(String[] args) throws Exception {
//		YouTube youtubeService = getService();
//		// Define and execute the API request
//		YouTube.Channels.List request = youtubeService.channels().list("snippet,contentDetails,statistics");
//		ChannelListResponse response = request.setForUsername("GoogleDevelopers").execute();
//		System.out.println(response);
//	}
}
