package com.simbirsoft.simchat.service.utils;

import java.util.Arrays;
import java.util.Collection;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

public class YouTubeApi {
	// private static final String CLIENT_SECRETS = "/client_secret.json";
	public static final String DEVELOPER_KEY = "AIzaSyCFcVrcD06Lhq3y8kpAVK2BD7-jZjfnq50";
	private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.readonly");

	private static final String APPLICATION_NAME = "API code samples";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/**
	 * Create an authorized Credential object.
	 *
	 * @return an authorized Credential object.
	 * @throws Exception
	 */
//	public static Credential authorize(final NetHttpTransport httpTransport) throws Exception {
//		// Load client secrets.
//		InputStream in = YouTubeApi.class.getResourceAsStream(CLIENT_SECRETS);
//		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//		// Build flow and trigger user authorization request.
//		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
//				clientSecrets, SCOPES).build();
//		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//		return credential;
//	}

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
	public static void main(String[] args) throws Exception {
		YouTube youtubeService = getService();
		// Define and execute the API request
		YouTube.Channels.List request = youtubeService.channels().list("snippet,contentDetails,statistics");
		ChannelListResponse response = request.setForUsername("GoogleDevelopers").execute();
		System.out.println(response);
	}
}
