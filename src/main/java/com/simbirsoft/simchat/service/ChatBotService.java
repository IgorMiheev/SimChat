package com.simbirsoft.simchat.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.ChatBotCommand;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.exception.AccessNotFoundException;
import com.simbirsoft.simchat.exception.ChatAlreadyExistException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrAlreadyExistException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;
import com.simbirsoft.simchat.service.utils.StringParse;
import com.simbirsoft.simchat.service.utils.YouTubeApi;

@Service
public class ChatBotService {

	@Autowired
	private UsrService usrService;

	@Autowired
	private ChatService chatService;

	@Autowired
	private AccessService accessService;

	@Autowired
	private PartyService partyService;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private PartyRepository partyRepository;

	Long currentUserId = 1L;

	enum BaseCommand {
		ROOM_CREATE("//room create"), ROOM_REMOVE("//room remove"), ROOM_RENAME("//room rename"),
		ROOM_CONNECT("//room connect"), ROOM_DISCONNECT("//room disconnect"), USER_RENAME("//user rename"),
		USER_BAN("//user ban"), USER_MODERATOR("//user moderator"), YBOT_FIND("//yBot find"), YBOT_HELP("//yBot help"),
		HELP("//help");

		private String text;

		private BaseCommand(String name) {
			this.text = name;
		}

		public String getText() {
			return text;
		}

		public static BaseCommand getEnumByText(String text) {
			for (BaseCommand bc : BaseCommand.values()) {
				if (bc.getText().equals(text)) {
					return bc;
				}
			}
			return null;
		}
	}

	public ResponseEntity parseBotCommand(ChatBotCommand chatBotCommand) throws Exception {

		String cmd = chatBotCommand.getText();
		String[] splitBase = cmd.split(" ");
		String baseText = splitBase[0];

		BaseCommand baseCommand = BaseCommand.getEnumByText(baseText);
		String additionCommand = "";

		if (baseCommand == null && splitBase.length > 1) {
			baseText = splitBase[0] + " " + splitBase[1];
			baseCommand = BaseCommand.getEnumByText(baseText);
		}
		if (baseCommand == null) {
			return ResponseEntity.badRequest()
					.body("Такой команды не существует. Введите //help для получения списка комманд");
		}

		if (cmd.split(baseText).length > 1) {
			additionCommand = cmd.split(baseText)[1].trim();
		}
		if (checkString(additionCommand) == false) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}

		switch (baseCommand) {
		case ROOM_CREATE:
			return parseCmdAndCreateRoom(additionCommand, currentUserId);
		case ROOM_CONNECT:
			return parseCmdAndConnectToRoom(additionCommand, currentUserId);
		case ROOM_DISCONNECT:
			return parseCmdAndDisconnectFromRoom(additionCommand, currentUserId);
		case ROOM_REMOVE:
			return parseCmdAndRemoveRoom(additionCommand, currentUserId);
		case ROOM_RENAME:
			return parseCmdAndRenameRoom(additionCommand, currentUserId);
		case USER_BAN:
			return parseCmdAndUserBan(additionCommand);
		case USER_MODERATOR:
			return parseCmdAndUserModerator(additionCommand);
		case USER_RENAME:
			return parseCmdAndUserRename(additionCommand);
		case YBOT_FIND:
			return yBotFind(additionCommand);
		case YBOT_HELP:
			return cmdYBotHelp();
		case HELP:
			return cmdHelp();
		default:
			break;
		}

		return ResponseEntity.ok(null);
	}

	/**
	 * @param additionCommand
	 * @return
	 * @throws UsrNotFoundException
	 * @throws ChatAlreadyExistException
	 */
	public ResponseEntity parseCmdAndCreateRoom(String additionCommand, Long currentUserId)
			throws UsrNotFoundException, ChatAlreadyExistException {
		String roomName = "";
		String chatType = "";
		try {
			// получаем имя чата между первым символом { и последним }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.lastIndexOf("}"));

		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {

			if (additionCommand.substring(additionCommand.lastIndexOf("}") + 1).equals(" -c")) {
				chatType = "private";
			} else {
				return ResponseEntity.badRequest()
						.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
			}
		} else {
			chatType = "public";
		}
		ChatCreate modelCreate = new ChatCreate(roomName, currentUserId, chatType);
		chatService.create(modelCreate);
		return ResponseEntity.ok("Чат с именем " + roomName + " успешно создан");
	}

	public ResponseEntity parseCmdAndConnectToRoom(String additionCommand, Long currentUserId)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException {
		additionCommand = additionCommand.trim();
		String roomName = "";
		String userName = "";
		try {
			// получаем имя чата между первым символом { и первым }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));
			// получаем имя пользователя между последним символом { и последним }
			if (additionCommand.contains("-l {")) {
				userName = additionCommand.substring(additionCommand.lastIndexOf("{") + 1,
						additionCommand.lastIndexOf("}"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");

		}

		ChatEntity chatEntity = null;

		if (!roomName.equals("")) {
			chatEntity = chatRepository.findByName(roomName);
		}

		UsrEntity userEntity = null;

		if (additionCommand.contains("-l {")) {
			userEntity = usrRepository.findByUsername(userName);
		} else {
			userEntity = usrRepository.findById(currentUserId).orElse(null);
		}

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким именем не найден");
		}
		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с именем " + userName + " не найден");

		}

		PartyCreate modelCreate = new PartyCreate(chatEntity.getChat_id(), userEntity.getUser_id(), "member",
				java.sql.Timestamp.valueOf(LocalDateTime.now()));
		partyService.create(modelCreate);

		return ResponseEntity.ok("Введена команда " + BaseCommand.ROOM_CREATE.getText() + " " + additionCommand
				+ ". Команда выполнена успешно.");
	}

	public ResponseEntity parseCmdAndDisconnectFromRoom(String additionCommand, Long currentUserId)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException, PartyNotFoundException {
		String roomName = null;
		String userName = null;
		Long banTime = null;
		try {
			// получаем имя чата между первым символом { и первым }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));

			// получаем имя пользователя
			if (additionCommand.contains("-l {")) {
				String sub = additionCommand.substring(additionCommand.indexOf("-l {") + 4);
				userName = sub.substring(0, sub.indexOf("}"));

				// получаем время бана в минутах
				if (additionCommand.contains("-m {")) {
					String subs = additionCommand.substring(additionCommand.indexOf("-m {") + 4);
					banTime = Long.parseLong(subs.substring(0, subs.indexOf("}")));
				}
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}
		if (!additionCommand.contains("-l {") && additionCommand.contains("-m {")) {
			return ResponseEntity.badRequest().body(
					"Ошибка в синтаксисе команды. Нельзя использовать параметр -m без параметра -l. Введите //help для получения списка комманд");
		}

		ChatEntity chatEntity = null;

		if (!roomName.equals("")) {
			chatEntity = chatRepository.findByName(roomName);
		}

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким именем не найден");
		}

		if (additionCommand.contains("-l {")) {
			UsrEntity userEntity = null;

			userEntity = usrRepository.findByUsername(userName);

			if (userEntity == null) {
				throw new UsrNotFoundException("Пользователь с именем " + userName + " не найден");
			}

			PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

			if (partyEntity == null) {

				if (banTime != null) {

					PartyCreate modelCreate = new PartyCreate(chatEntity.getChat_id(), userEntity.getUser_id(),
							"banned_member", java.sql.Timestamp.valueOf(LocalDateTime.now()));
					partyService.create(modelCreate);
					partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

				} else {

					throw new PartyNotFoundException("Пользователь " + userEntity.getUsername() + " в чате "
							+ chatEntity.getName() + " не состоит");
				}
			}

			if (banTime != null) {

				partyService.banUserByIdAndChatId(userEntity.getUser_id(), chatEntity.getChat_id(), banTime);

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " не сможет войти в этот чат " + banTime + " минут");
			} else {

				partyService.delete(partyEntity.getParty_id());

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " отключен от чата " + chatEntity.getName());
			}
		} else {
			// выход текущего пользователя из комнаты = room disconnect

			UsrEntity userEntity = usrRepository.findById(currentUserId).orElse(null);

			PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

			if (partyEntity == null) {
				throw new PartyNotFoundException(
						"Пользователь " + userEntity.getUsername() + " в чате " + chatEntity.getName() + " не состоит");
			} else {
				partyService.delete(partyEntity.getParty_id());

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " отключен от чата " + chatEntity.getName());
			}

		}
	}

	public ResponseEntity parseCmdAndRemoveRoom(String additionCommand, Long currentUserId)
			throws ChatNotFoundException {
		String roomName = null;
		try {
			// получаем имя чата между первым символом { и первым }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));

		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}

		ChatEntity chatEntity = null;

		chatEntity = chatRepository.findByName(roomName);

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с именем " + roomName + " не найден");
		}

		chatService.delete(chatEntity.getChat_id());
		return ResponseEntity.ok("Готово. Чат с именем " + roomName + " удален");
	}

	public ResponseEntity parseCmdAndRenameRoom(String additionCommand, Long currentUserId)
			throws ChatNotFoundException, UsrNotFoundException {

		String roomNameOld = null;
		String roomNameNew = null;

		try {
			// получаем имя чата между первым символом { и первым }
			roomNameOld = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));

			if (additionCommand.contains("-r {")) {
				String subs = additionCommand.substring(additionCommand.indexOf("-r {") + 4);
				roomNameNew = subs.substring(0, subs.indexOf("}"));
			} else {
				return ResponseEntity.badRequest()
						.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
			}

		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}

		if (roomNameOld.equals("") || roomNameNew.equals("") || roomNameNew == null
				|| !additionCommand.startsWith("{")) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}

		ChatEntity chatEntity = null;

		chatEntity = chatRepository.findByName(roomNameOld);

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с именем " + roomNameOld + " не найден");
		}

		chatService.rename(chatEntity.getChat_id(), roomNameNew);
		return ResponseEntity.ok("Готово. Чат с именем " + roomNameOld + " переименован в " + roomNameNew + ".");
	}

	public ResponseEntity parseCmdAndUserBan(String additionCommand)
			throws ChatNotFoundException, UsrNotFoundException {

		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCommand, leftBracketPos, rightBracketPos);

		LinkedList<String> args = new LinkedList<String>();
		readArgs(additionCommand, args);

		if (args.size() != 2) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (args.get(0) == null || args.get(0).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (!(StringParse.isLong(args.get(1)))) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (leftBracketPos.get(0) != 3) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (!additionCommand.substring(leftBracketPos.get(0) - 3, leftBracketPos.get(0)).equals("-l ")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (!additionCommand.substring(leftBracketPos.get(1) - 5, leftBracketPos.get(1)).equals("} -m ")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}

		String usrName = args.get(0);
		Long banTime = Long.parseLong(args.get(1));

		UsrEntity usrEntity = usrRepository.findByUsername(usrName);

		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь с именем " + usrName + " не найден");
		}

		usrService.ban(usrEntity.getUser_id(), banTime);

		return ResponseEntity.ok("Готово. Пользователь с именем " + usrName + " забанен на " + banTime + " минут(у).");
	}

	public ResponseEntity parseCmdAndUserModerator(String additionCommand)
			throws ChatNotFoundException, UsrNotFoundException, AccessNotFoundException {

		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCommand, leftBracketPos, rightBracketPos);

		LinkedList<String> args = new LinkedList<String>();
		readArgs(additionCommand, args);

		if (args.size() != 1) {
			return ResponseEntity.badRequest()
					.body("Неверное количество аргументов. Введите //help для получения списка комманд.");
		}
		if (args.get(0) == null || args.get(0).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (leftBracketPos.get(0) != 0) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}

		boolean isModerator = false;

		if (additionCommand.substring(rightBracketPos.get(0) + 1).equals(" -n")) {
			isModerator = true;
		} else if (additionCommand.substring(rightBracketPos.get(0) + 1).equals(" -d")) {
			isModerator = false;
		} else {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд. ");

		}

		String usrName = args.get(0);

		UsrEntity usrEntity = usrRepository.findByUsername(usrName);

		if (usrEntity == null) {
			throw new UsrNotFoundException("Пользователь с именем " + usrName + " не найден");
		}

		accessService.setModerator(usrEntity.getAccess().getAccess_id(), isModerator);

		if (isModerator) {
			return ResponseEntity.ok("Готово. Пользователь с именем " + usrName + " назначен модератором.");
		} else {
			return ResponseEntity.ok("Готово. Пользователь с именем " + usrName + " больше не модератор.");

		}
	}

	public ResponseEntity parseCmdAndUserRename(String additionCommand)
			throws ChatNotFoundException, UsrNotFoundException, AccessNotFoundException, UsrAlreadyExistException {

		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCommand, leftBracketPos, rightBracketPos);

		LinkedList<String> args = new LinkedList<String>();
		readArgs(additionCommand, args);

		if (args.size() != 2) {
			return ResponseEntity.badRequest()
					.body("Неверное количество аргументов. Введите //help для получения списка комманд.");
		}
		if (args.get(0) == null || args.get(0).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (args.get(1) == null || args.get(1).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (leftBracketPos.get(0) != 0) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (rightBracketPos.get(1) != additionCommand.length() - 1) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд. ");
		}

		String usrNameOld = args.get(0);
		String usrNameNew = args.get(1);

		usrService.rename(usrNameOld, usrNameNew);

		return ResponseEntity.ok("Готово. Пользователь с именем " + usrNameOld + " сменил имя на " + usrNameNew);

	}

	public ResponseEntity yBotFind(String additionCommand) throws Exception {
		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCommand, leftBracketPos, rightBracketPos);

		LinkedList<String> args = new LinkedList<String>();
		readArgs(additionCommand, args);

		if (args.size() != 2) {
			return ResponseEntity.badRequest()
					.body("Неверное количество аргументов. Введите //help для получения списка комманд.");
		}
		if (args.get(0) == null || args.get(0).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (args.get(1) == null || args.get(1).equals("")) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (leftBracketPos.get(0) != 0) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}
		if (!(additionCommand.substring(leftBracketPos.get(1) - 3, leftBracketPos.get(1)).equals("}||"))) {
			return ResponseEntity.badRequest().body("Ошибка в команде. Введите //help для получения списка комманд.");
		}

		String channelName = args.get(0);
		String videoName = args.get(1);
		// videoName = "%22" + videoName + "%22";

		YouTube youtubeService = YouTubeApi.getService();

		// Define and execute the API request
		YouTube.Channels.List request = youtubeService.channels().list("id");
		ChannelListResponse response = request.setForUsername(channelName).setKey(YouTubeApi.DEVELOPER_KEY).execute();

		if (response.getPageInfo().getTotalResults() != 1) {
			return ResponseEntity.ok("Не найдено точного совпадения по имени канала");
		}

		String channelId = response.getItems().get(0).getId();

		YouTube.Search.List requestSearchVideo = youtubeService.search().list("snippet");
		SearchListResponse responseSearchVideo = requestSearchVideo.setChannelId(channelId).setMaxResults(25L)
				.setQ(videoName).setKey(YouTubeApi.DEVELOPER_KEY).execute();

		List<String> videoIdsList = new LinkedList<String>();
		for (SearchResult sr : responseSearchVideo.getItems()) {
			String videoId = sr.getId().getVideoId();
			videoIdsList.add(videoId);
		}

		if (responseSearchVideo.getPageInfo().getTotalResults() == 0) {
			return ResponseEntity.ok("Видео с именем " + videoName + " на канале " + channelName + " не найдено");
		}

		String videoIdsString = String.join(",", videoIdsList);

		YouTube.Videos.List requestVideo = youtubeService.videos().list("snippet,contentDetails,statistics");
		VideoListResponse responseVideo = requestVideo.setId(videoIdsString).setKey(YouTubeApi.DEVELOPER_KEY).execute();

		StringBuilder result = new StringBuilder();
		result.append(
				"Найдено " + responseSearchVideo.getPageInfo().getTotalResults() + " видео. Первые 25 результатов:");

		for (Video sr : responseVideo.getItems()) {
			String videoTitle = sr.getSnippet().getTitle();
			String videoId = sr.getId();
			BigInteger views = sr.getStatistics().getViewCount();
			BigInteger likes = sr.getStatistics().getLikeCount();

			result.append("\r\n https://www.youtube.com/watch?v=" + videoId);
			if (additionCommand.contains(" -l")) {
				result.append(" лайков: " + likes);
			}
			if (additionCommand.contains(" -v")) {
				result.append(" просмотров: " + views);
			}
			result.append(" " + videoTitle);

		}

		return ResponseEntity.ok(result);

//		if (responseVideo.getItems().size() == 0) {
//			return ResponseEntity.ok("Видео не найдено");
//		} else {
//			return ResponseEntity.ok(responseVideo);
//		}

//		return ResponseEntity.ok(responseVideo);

		// System.out.println(response);

		// return ResponseEntity.ok("Поиск видео с именем: " + videoName + " на канале:
		// " + channelName);
	}

	public ResponseEntity cmdHelp() {
		// LinkedList<String> helpNote = new LinkedList<String>();

		StringBuilder sb = new StringBuilder();
		sb.append("Команды:");
		sb.append("\r\nКомнаты:");
		sb.append("\r\n1. //room create {Название комнаты} - создает комнаты; -c закрытая комната. "
				+ "Только (владелец, модератор и админ) может добавлять/удалять пользователей из комнаты.");
		sb.append("\r\n2. //room remove {Название комнаты} - удаляет комнату (владелец и админ);");
		sb.append("\r\n3. //room rename {Название комнаты} - переименование комнаты (владелец и админ)");
		sb.append("\r\n4. //room connect {Название комнаты} - войти в комнату; "
				+ "-l {login пользователя} - добавить пользователя в комнату");
		sb.append("\r\n5. //room disconnect {Название комнаты} - выйти из заданной комнаты;"
				+ "-l {login пользователя} - выгоняет пользователя из комнаты (для владельца, модератора и админа); "
				+ "-m {Количество минут} - время на которое пользователь не сможет войти (для владельца, модератора и админа).");
		sb.append("\r\nПользователи:");
		sb.append("\r\n1. //user rename {login пользователя} {новое имя} (владелец и админ);");
		sb.append("\r\n2. //user ban -l {login пользователя} - выгоняет пользователя из всех комнат; "
				+ "-m {Количество минут} - время на которое пользователь не сможет войти.");
		sb.append("\r\n3. //user moderator {login пользователя} - действия над модераторами. "
				+ "-n - назначить пользователя модератором. -d - “разжаловать” пользователя.");
		sb.append("\r\nБоты:");
		sb.append("\r\n1. //yBot find {название канала}||{название видео} - в ответ бот присылает ссылку на ролик; "
				+ "-v - выводит количество текущих просмотров. -l - выводит количество лайков под видео.");
		sb.append("\r\n2. //yBot help - список доступных команд для взаимодействия.");
		sb.append("\r\nДругие:");
		sb.append("\r\n1. //help - выводит список доступных команд.");

		return ResponseEntity.ok(sb);

	}

	public ResponseEntity cmdYBotHelp() {
		// LinkedList<String> helpNote = new LinkedList<String>();

		StringBuilder sb = new StringBuilder();
		sb.append("Команды:");
		sb.append("\r\n1. //yBot find {название канала}||{название видео} - в ответ бот присылает ссылку на ролик; "
				+ "-v - выводит количество текущих просмотров. -l - выводит количество лайков под видео.");
		sb.append("\r\n2. //yBot help - список доступных команд для взаимодействия.");

		return ResponseEntity.ok(sb);

	}

	public Boolean checkString(String additionCmdString) {
		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCmdString, leftBracketPos, rightBracketPos);

		// проверка на то, что количество левых и правых фигурных скобок совпадает
		if (leftBracketPos.size() != rightBracketPos.size()) {
			return false;
		}

		if (leftBracketPos.size() > 0) {
			// проверка на то, что фигурные скобки идут в правильном порядке
			for (int i = 0; i < leftBracketPos.size(); i++) {
				if (leftBracketPos.get(i) > rightBracketPos.get(i)) {
					return false;
				}
			}
			// проверка на то, что после скобок нет ничего лишнего
			if (!((Character) additionCmdString.charAt(additionCmdString.length() - 2)).equals('-')) {
				if (rightBracketPos.get(rightBracketPos.size() - 1) != additionCmdString.length() - 1) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Получаем позиции всех фигурных скобок
	 * 
	 * @param additionCmdString
	 * @param leftBracketPos
	 * @param rightBracketPos
	 */
	public void readBracketPos(String additionCmdString, LinkedList<Integer> leftBracketPos,
			LinkedList<Integer> rightBracketPos) {
		for (int i = 0; i < additionCmdString.length(); i++) {
			Character currChar = additionCmdString.charAt(i);
			if (currChar.equals('{')) {
				leftBracketPos.add(i);
			}
			if (currChar.equals('}')) {
				rightBracketPos.add(i);
			}
		}
	}

	public void readArgs(String additionCmdString, LinkedList<String> args) {
		LinkedList<Integer> leftBracketPos = new LinkedList<Integer>();
		LinkedList<Integer> rightBracketPos = new LinkedList<Integer>();
		readBracketPos(additionCmdString, leftBracketPos, rightBracketPos);

		for (int i = 0; i < leftBracketPos.size(); i++) {
			args.add(additionCmdString.substring(leftBracketPos.get(i) + 1, rightBracketPos.get(i)));
		}
	}

}
